package com.chuangdata.userprofile.job.ct.jt;

import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.model.*;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.Task.Counter;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DPIReducer extends Reducer<Text, MultipleModel, Text, Text> {
    private static final Logger LOG = Logger.getLogger(DPIReducer.class);

    private MultipleOutputs<Text, Text> multipleOutputs;

    private Text outKey;
    private Text outValue;

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        outKey = new Text();
        outValue = new Text();
        multipleOutputs = new MultipleOutputs<Text, Text>(context);
    }

    @Override
    public void reduce(Text key, Iterable<MultipleModel> value, Context context) throws IOException, InterruptedException {
        // out models
        Map<Integer, AppModel> appMap = new HashMap<Integer, AppModel>();
        Map<Long, ActionModel> actiomMap = new HashMap<Long, ActionModel>();
        Map<Long, Map<String, ParamModel>> paramMap = new HashMap<Long, Map<String, ParamModel>>();

        for (MultipleModel model : value) {
            try {
                for (Writable val : model.getSet()) {
                    if (val instanceof AppModel) {
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
                        ParamMapModel paramMapModel = (ParamMapModel) val;
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
        // output to different dir
        outKey.set(key.toString());
        for (Integer appId : appMap.keySet()) {
            outValue.set(appMap.get(appId).toString());
            multipleOutputs.write(outKey, outValue, appMap.get(appId).getName() + "/part");
            context.getCounter(UserProfileCounter.REDUCE_OUTPUT_APP).increment(1L);
            context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
        }
        for (Long behaviourId : actiomMap.keySet()) {
            outValue.set(actiomMap.get(behaviourId).toString());
            multipleOutputs.write(outKey, outValue, actiomMap.get(behaviourId).getName() + "/part");
            context.getCounter(UserProfileCounter.REDUCE_OUTPUT_ACTION).increment(1L);
            context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
        }
        for (Long paramId : paramMap.keySet()) {
            for (ParamModel param : paramMap.get(paramId).values()) {
                outValue.set(param.toString());
                multipleOutputs.write(outKey, outValue, param.getName() + "/part");
                context.getCounter(UserProfileCounter.REDUCE_OUTPUT_PARAM).increment(1L);
                context.getCounter(Counter.REDUCE_OUTPUT_RECORDS).increment(1L);
            }
        }
    }

    public void cleanup(Context context) throws IOException, InterruptedException {
        multipleOutputs.close();
    }

}
