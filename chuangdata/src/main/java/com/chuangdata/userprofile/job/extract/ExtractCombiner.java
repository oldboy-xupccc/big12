package com.chuangdata.userprofile.job.extract;

import com.chuangdata.userprofile.model.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 *@author luxiaofeng
 *@description combiner
 *
 */
public class ExtractCombiner extends Reducer<Text, MultipleModel, Text, MultipleModel> {
	private static final Logger LOG = Logger.getLogger(ExtractCombiner.class);

	private Text outKey;

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		outKey = new Text();
		
	}

	@Override
	public void reduce(Text key, Iterable<MultipleModel> value, Context context)
			throws IOException, InterruptedException {
	
			// out models
			Map<Integer, AppModel> appMap = new HashMap<Integer, AppModel>();
			Map<Long, ActionModel> actiomMap = new HashMap<Long, ActionModel>();
			Map<Long, Map<String, ParamModel>> paramMap = new HashMap<Long, Map<String, ParamModel>>();
			Map<Integer, AppDomainModel> appDomainMap = new HashMap<Integer, AppDomainModel>();
			boolean isFirstEntry=true;
			for (MultipleModel model : value) {
				try {
					for (Writable val : model.getSet()) {
				    if( isFirstEntry && val instanceof Text){
				   	 MultipleModel outPutModel  = new MultipleModel();
				    	outPutModel.add((Text)val);
						outKey.set(key);
						context.write(outKey, outPutModel);
						isFirstEntry = false;
				    }else if (val instanceof AppDomainModel) {
						AppDomainModel appDomainModel = (AppDomainModel) val;
						if (appDomainMap.containsKey(appDomainModel.getAppDomainId().get())) {
							AppDomainModel appDomain = appDomainMap
									.get(appDomainModel.getAppDomainId().get());
							appDomain.setLogCount(appDomain.getLogCount().get()
									+ appDomainModel.getLogCount().get());
						} else {
							appDomainMap.put(appDomainModel.getAppDomainId().get(), appDomainModel);
						}
					} 
				    else if (val instanceof AppModel) {
                        AppModel appModel = (AppModel) val;
                        if (appMap.containsKey(appModel.getAppId().get())) {
                            AppModel app = appMap.get(appModel.getAppId().get());
                            app.setLogCount(app.getLogCount().get() + appModel.getLogCount().get());
                        } else {
                            appMap.put(appModel.getAppId().get(), appModel);
                        }
                    } else if (val instanceof ActionModel) {
                        ActionModel actionModel = (ActionModel) val;
                        if (actiomMap.containsKey(actionModel.getId())) {
                            ActionModel action = actiomMap.get(actionModel.getId());
                            action.setLogCount(action.getLogCount().get() + actionModel.getLogCount().get());
                        } else {
                            actiomMap.put(actionModel.getId(), actionModel);
                        }
                    } else if (val instanceof ParamMapModel) {
                        ParamMapModel paramMapModel = (ParamMapModel)val;
                        for (ParamModel paramModel : paramMapModel.getParamModels()) {
                            if (paramMap.containsKey(paramModel.getId())) {
                                Map<String, ParamModel> paramValueMap = paramMap.get(paramModel.getId());
                                if (paramValueMap.containsKey(paramModel.getParamValue())) {
                                    paramValueMap.get(paramModel.getParamValue()).setLogCount(
                                            paramValueMap.get(paramModel.getParamValue()).getLogCount().get() + paramModel.getLogCount().get());
                                } else {
                                    paramValueMap.put(paramModel.getParamValue(), paramModel);
                                }
                            } else {
                                Map<String, ParamModel> paramValueMap = new HashMap<String, ParamModel>();
                                paramValueMap.put(paramModel.getParamValue(), paramModel);
                                paramMap.put(paramModel.getId(), paramValueMap);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error for MultipleModel=" + model.toString(), e);
            }
        }
			// output to reduce
			 MultipleModel outPutModel  = new MultipleModel();
			outKey.set(key.toString());
			for (Integer appDomainId : appDomainMap.keySet()) {
				outPutModel.add(appDomainMap.get(appDomainId));
			}
			
			for (Integer appId : appMap.keySet()) {
				outPutModel.add(appMap.get(appId));
			}
			for (Long behaviourId : actiomMap.keySet()) {
				outPutModel.add(actiomMap.get(behaviourId));
			
			}
		
			for (Long paramId : paramMap.keySet()) {
				ParamMapModel  paramMapModel = new ParamMapModel();
				for (ParamModel param : paramMap.get(paramId).values()) {
					paramMapModel.addParam(param);
				}
				outPutModel.add(paramMapModel);
			}
			context.write(outKey, outPutModel);

	}



	public void cleanup(Context context) throws IOException,
			InterruptedException {
	//TODO NOTHING
	}

}
