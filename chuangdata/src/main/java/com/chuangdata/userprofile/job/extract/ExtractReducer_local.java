package com.chuangdata.userprofile.job.extract;

import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.decrypt.row.*;
import com.chuangdata.userprofile.model.*;
import org.apache.hadoop.hive.ql.io.orc.OrcSerde;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.Task.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * changed by luxiaofeng
 * 
 * @date 2016-04-20 output the trelation table of id for MD5 encrypting and AES
 *       encrypting
 * 
 */
public class ExtractReducer_local extends Reducer<Text, MultipleModel, NullWritable,Writable > {
	private static final Logger LOG = Logger.getLogger(ExtractReducer_local.class);

	private MultipleOutputs<NullWritable,Writable> multipleOutputs;

	private Text outKey;
	private Text outValue;

	private String hivePath;

	private final NullWritable nw = NullWritable.get();

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		outKey = new Text();
		outValue = new Text();
		hivePath = context.getConfiguration().get("userprofile.result.hive.path","");
		multipleOutputs = new MultipleOutputs<NullWritable,Writable>(context);
	}

	@Override
	public void reduce(Text key, Iterable<MultipleModel> value, Context context)
			throws IOException, InterruptedException {

		// out models
		Map<String, UserAgentModel> userAgentMap = new HashMap<String, UserAgentModel>();
		Map<Integer, AppModel> appMap = new HashMap<Integer, AppModel>();
		Map<Long, ActionModel> actiomMap = new HashMap<Long, ActionModel>();
		Map<Long, Map<String, ParamModel>> paramMap = new HashMap<Long, Map<String, ParamModel>>();
		Map<Integer, AppDomainModel> appDomainMap = new HashMap<Integer, AppDomainModel>();
		boolean isFirstEntry = true;
		for (MultipleModel model : value) {
			try {
				for (Writable val : model.getSet()) {
					if (isFirstEntry && val instanceof Text) {
						outKey.set(key);
						outValue.set(val.toString());
//						multipleOutputs.write(outKey, outValue, "Md5toAES"
//								+ "/part");
						isFirstEntry = false;
					} else if (val instanceof UserAgentModel) {
						//userAgent
						UserAgentModel userAgentModel = (UserAgentModel) val;
						if(userAgentMap.containsKey(userAgentModel.getPreImei().toString()+userAgentModel.getUserAgent().toString())){
							UserAgentModel  userAgent = userAgentMap.get(userAgentModel.getPreImei().toString()+userAgentModel.getUserAgent().toString());
							userAgent.setLogCount(userAgent.getLogCount().get()+userAgentModel.getLogCount().get());

						}else{
							userAgentMap.put(userAgentModel.getPreImei().toString()+userAgentModel.getUserAgent().toString(),userAgentModel);
						}

					} else if (val instanceof AppDomainModel) {
						AppDomainModel appDomainModel = (AppDomainModel) val;
						if (appDomainMap.containsKey(appDomainModel.getAppDomainId().get())) {
							AppDomainModel appDomain = appDomainMap
									.get(appDomainModel.getAppDomainId().get());
							appDomain.setLogCount(appDomain.getLogCount().get()
									+ appDomainModel.getLogCount().get());
						} else {
							appDomainMap.put(appDomainModel.getAppDomainId().get(), appDomainModel);
						}
					} else if (val instanceof AppModel) {
						AppModel appModel = (AppModel) val;
						if (appMap.containsKey(appModel.getAppId().get())) {
							AppModel app = appMap
									.get(appModel.getAppId().get());
							app.setLogCount(app.getLogCount().get()
									+ appModel.getLogCount().get());
						} else {
							appMap.put(appModel.getAppId().get(), appModel);
						}
					} else if (val instanceof ActionModel) {
						ActionModel actionModel = (ActionModel) val;
						if (actiomMap.containsKey(actionModel.getId())) {
							ActionModel action = actiomMap.get(actionModel
									.getId());
							action.setLogCount(action.getLogCount().get()
									+ actionModel.getLogCount().get());
						} else {
							actiomMap.put(actionModel.getId(), actionModel);
						}
					} else if (val instanceof ParamMapModel) {
						ParamMapModel paramMapModel = (ParamMapModel) val;
						for (ParamModel paramModel : paramMapModel
								.getParamModels()) {
							if (paramMap.containsKey(paramModel.getId())) {
								Map<String, ParamModel> paramValueMap = paramMap
										.get(paramModel.getId());
								if (paramValueMap.containsKey(paramModel
										.getParamValue())) {
									paramValueMap
											.get(paramModel.getParamValue())
											.setLogCount(
													paramValueMap
															.get(paramModel
																	.getParamValue())
															.getLogCount()
															.get()
															+ paramModel
																	.getLogCount()
																	.get());
								} else {
									paramValueMap.put(
											paramModel.getParamValue(),
											paramModel);
								}
							} else {
								Map<String, ParamModel> paramValueMap = new HashMap<String, ParamModel>();
								paramValueMap.put(paramModel.getParamValue(),
										paramModel);
								paramMap.put(paramModel.getId(), paramValueMap);
							}
						}
					}
				}
			} catch (Exception e) {
				LOG.error("Error for MultipleModel=" + model.toString(), e);
			}
		}

		try{

		// output to different dir
		outKey.set(key.toString());

			// output userAgent
			for (String userAgentId : userAgentMap.keySet()) {
				String keys[] = key.toString().split("\\|");
				UserAgentModel userAgentModel = userAgentMap.get(userAgentId);
				String tmp[] = new String[5];
				tmp[0]=keys[0]; //log_time
				tmp[1]=keys[2]; //imei
				tmp[2]=userAgentModel.getUserAgent().toString(); //useragent
				tmp[3]=userAgentModel.getPreImei().toString();//preimei
				tmp[4]=userAgentModel.getLogCount().toString();//log_count

				OrcSerde orcSerde = new OrcSerde();
				StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
						.getReflectionObjectInspector(UserAgentRow.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

				Writable row = orcSerde.serialize(new UserAgentRow(tmp), inspector);

				multipleOutputs.write(nw, row, hivePath
						+ "/userAgent");

				context.getCounter(
						UserProfileCounter.REDUCE_OUTPUT_USERAGENT)
						.increment(1L);
				context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);

			}

		// output domain 
		for (Integer appDomainId : appDomainMap.keySet()) {

			outValue.set(appDomainMap.get(appDomainId).toString());
//			multipleOutputs.write(outKey, outValue, appDomainMap.get(appDomainId).getName()
//					+ "/part");

           //2016-10-10 Changed by luxiaofeng: write with orc file type
			String tmp[] = (outKey.toString()+"\\|"+outValue.toString()).split("\\|");
			OrcSerde orcSerde = new OrcSerde();
			StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
					.getReflectionObjectInspector(DecryptDomainRow.class,ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

			Writable row = orcSerde.serialize(new DecryptDomainRow(tmp), inspector);

			multipleOutputs.write(nw, row,  hivePath
					+ "/domain");

			context.getCounter(
		  UserProfileCounter.REDUCE_OUTPUT_DOMAIN)
					.increment(1L);
			context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
		}
		for (Integer appId : appMap.keySet()) {
			outValue.set(appMap.get(appId).toString());
//			multipleOutputs.write(outKey, outValue, appMap.get(appId).getName()
//					+ "/part");

			//2016-10-10 Changed by luxiaofeng: write with orc file type
			String tmp[] = (outKey.toString()+"\\|"+outValue.toString()).split("\\|");
			OrcSerde orcSerde = new OrcSerde();
			StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
					.getReflectionObjectInspector(DecryptAppRow.class,ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

			Writable row = orcSerde.serialize(new DecryptAppRow(tmp), inspector);

			multipleOutputs.write(nw, row, hivePath
					+ "/app");

			context.getCounter(
					UserProfileCounter.REDUCE_OUTPUT_APP)
					.increment(1L);
			context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
		}
		for (Long behaviourId : actiomMap.keySet()) {
			outValue.set(actiomMap.get(behaviourId).toString());
//			multipleOutputs.write(outKey, outValue, actiomMap.get(behaviourId)
//					.getName() + "/part");


			//2016-10-10 Changed by luxiaofeng: write with orc file type
			String tmp[] = (outKey.toString()+"\\|"+outValue.toString()).split("\\|");
			OrcSerde orcSerde = new OrcSerde();
			StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
					.getReflectionObjectInspector(DecryptActionRow.class,ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

			Writable row = orcSerde.serialize(new DecryptActionRow(tmp), inspector);

			multipleOutputs.write(nw, row,   hivePath
					+ "/actiom");

			context.getCounter(
					UserProfileCounter.REDUCE_OUTPUT_ACTION)
					.increment(1L);
			context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
		}
		for (Long paramId : paramMap.keySet()) {
			for (ParamModel param : paramMap.get(paramId).values()) {
				outValue.set(param.toString());
//				multipleOutputs.write(outKey, outValue, param.getName()
//						+ "/part");

				//2016-10-10 Changed by luxiaofeng: write with orc file type
				String tmp[] = (outKey.toString()+"\\|"+outValue.toString()).split("\\|");
				OrcSerde orcSerde = new OrcSerde();
				StructObjectInspector inspector = (StructObjectInspector) ObjectInspectorFactory
						.getReflectionObjectInspector(DecryptParamsRow.class,ObjectInspectorFactory.ObjectInspectorOptions.JAVA);

				Writable row = orcSerde.serialize(new DecryptParamsRow(tmp), inspector);

				multipleOutputs.write(nw, row,  hivePath
						+ "/param");

				context.getCounter(
						UserProfileCounter.REDUCE_OUTPUT_PARAM)
						.increment(1L);
				context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
			}
		}

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	@Override
	public void cleanup(Context context) throws IOException,
			InterruptedException {
		multipleOutputs.close();
	}

}
