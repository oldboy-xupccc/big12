package com.chuangdata.userprofile.job.ct.kvjob;

import com.chuangdata.framework.encrypt.resource.Encrypter;
import com.chuangdata.userprofile.utils.JsonUtil;
import com.chuangdata.userprofile.utils.TagListUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @author luxiaofeng
 * @date 2016-10-05
 */
public class KvReducer extends Reducer<Text, ValueGenericWritable, Text, Text> {
    private static final Logger LOG = Logger.getLogger(KvReducer.class);

    private MultipleOutputs<Text, Text> multipleOutputs;
    private final String FIELD_SEPERATOR = "|";
    private Text outKey;
    private Text outValue;
    private int USER_LOG_COUNT_LIMIT = 10; // api1 value limit
    private int TAG_IMEI_LIMIT = 5;// api2 value limit
    private int TAG_USER_COUNT_LIMIT = 16;// api3 value limit
    private Map<Integer, HashSet<Text>> proImeiSetMap = new HashMap<Integer, HashSet<Text>>();
    private Map<Integer, Long> proLogCountMap = new HashMap<Integer, Long>();
    private Set<Text> imeiSet = new HashSet<Text>();
    private Map<Integer, Long> tagLogCountMap = new HashMap<Integer, Long>();

    private Map<Integer, String> detailMap = new HashMap<Integer, String>();

    @Override
    public void setup(Context context) throws IOException, InterruptedException {
        outKey = new Text();
        outValue = new Text();
        multipleOutputs = new MultipleOutputs<Text, Text>(context);
        Configuration configuration = context.getConfiguration();
        USER_LOG_COUNT_LIMIT = configuration.getInt("chuangdata.key.api.one.limit", 10);
        TAG_IMEI_LIMIT = configuration.getInt("chuangdata.key.api.two.limit", 5);
        TAG_USER_COUNT_LIMIT = configuration.getInt("chuangdata.key.api.three.limit", 16);

        detailMap.put(700599, "t"); // taobao
        detailMap.put(700615, "t"); // tmall
        detailMap.put(700576, "j"); // jd
        detailMap.put(700501, "v"); // vip
        detailMap.put(700543, "s"); // suning
        detailMap.put(701363, "z"); // z.cn

    }

    @Override
    public void reduce(Text key, Iterable<ValueGenericWritable> values,
                       Context context) throws IOException, InterruptedException {
        proImeiSetMap.clear();
        proLogCountMap.clear();
        imeiSet.clear();
        tagLogCountMap.clear();
        String tagStr = null;
        for (ValueGenericWritable value : values) {
            if (value.get() instanceof TagToUserCountModel) {
                TagToUserCountModel tagToUserCountModel = (TagToUserCountModel) value
                        .get();
                if (proLogCountMap.containsKey(tagToUserCountModel
                        .getProvinceId().get())) {
                    long logCount = proLogCountMap.get(tagToUserCountModel
                            .getProvinceId().get());
                    logCount += tagToUserCountModel.getLogCount().get();
                    proLogCountMap.put(tagToUserCountModel.getProvinceId()
                            .get(), logCount);
                } else {
                    proLogCountMap.put(tagToUserCountModel.getProvinceId()
                            .get(), tagToUserCountModel.getLogCount().get());
                }

                if (proImeiSetMap.containsKey(tagToUserCountModel
                        .getProvinceId().get())) {
                    Set<Text> imeiSet = proImeiSetMap.get(tagToUserCountModel
                            .getProvinceId().get());
                    if (!imeiSet.contains(tagToUserCountModel.getImeiId())) {
                        imeiSet.add(new Text(tagToUserCountModel.getImeiId()));
                    }
                } else {
                    HashSet<Text> imeiSet = new HashSet<Text>();
                    imeiSet.add(new Text(tagToUserCountModel.getImeiId()));
                    proImeiSetMap.put(
                            tagToUserCountModel.getProvinceId().get(), imeiSet);

                }

            } else if (value.get() instanceof UserIdToTagLogCountModel) {
                UserIdToTagLogCountModel userIdToTagLogCountModel = (UserIdToTagLogCountModel) value
                        .get();
                if (tagLogCountMap.containsKey(userIdToTagLogCountModel
                        .getTagId().get())) {
                    long logCount = userIdToTagLogCountModel.getLogCount()
                            .get();
                    logCount += tagLogCountMap.get(userIdToTagLogCountModel
                            .getTagId().get());
                    tagLogCountMap.put(userIdToTagLogCountModel.getTagId()
                            .get(), logCount);
                } else {
                    tagLogCountMap.put(userIdToTagLogCountModel.getTagId()
                            .get(), userIdToTagLogCountModel.getLogCount()
                            .get());
                }

                //get tagstr
                if (null == tagStr) {
                    tagStr = userIdToTagLogCountModel.getOtherParam().toString();
                }
            } else {
                Text imei = (Text) value.get();
                if (!imeiSet.contains(imei)) {
                    imeiSet.add(imei);
                    LOG.info("the imei id is " + imei.toString());
                }
            }
        }
        // output the kv1
        if (proImeiSetMap.size() > 0 && proLogCountMap.size() > 0
                && proImeiSetMap.size() == proLogCountMap.size()) {
            int count = 0;
            int groupCount = 0;
            Map<Object, Object> result = new HashMap<Object, Object>();
            for (Integer proId : proLogCountMap.keySet()) {
                if (count >= USER_LOG_COUNT_LIMIT
                        && count % USER_LOG_COUNT_LIMIT == 0) {
                    groupCount++;
                    String keyStr = key.toString() + FIELD_SEPERATOR
                            + groupCount;
                    outKey.set(keyStr);
                    outValue.set(JsonUtil.<Object>mapToJson(result));
                    multipleOutputs.write(outKey, outValue, "UserTagLogCount"
                            + "/part");
                    context.getCounter("kvReducer", "API1_COUNT").increment(1l);
                    if (count == proImeiSetMap.size()) {
                        result.put("EndFlag", new Boolean(true));
                    }
                    result.clear();
                }
                count++;
                if (count == proImeiSetMap.size()) {
                    result.put("EndFlag", new Boolean(true));
                }
                result.put(proId,
                        new Object[]{proImeiSetMap.get(proId).size(),
                                proLogCountMap.get(proId)});
            }
            groupCount++;
            String keyStr = key.toString() + FIELD_SEPERATOR + groupCount;
            outKey.set(keyStr);
            outValue.set(JsonUtil.<Object>mapToJson(result));
            multipleOutputs
                    .write(outKey, outValue, "UserTagLogCount" + "/part");
            context.getCounter("kvReducer", "COUNT1").increment(1l);
        }
        // output the kv2 data

        if (imeiSet.size() > 0) {
            Map<Object, Object> result = new HashMap<Object, Object>();
            List<String> tagListTypes = new ArrayList<String>(this.detailMap.values());
            int index = imeiSet.size() % this.detailMap.size();
            String tagListType = tagListTypes.get(index);
            int count = 0;
            int groupCount = 0;
            List<String> list = new ArrayList<String>();
            for (Text imei : imeiSet) {
                if (count >= TAG_IMEI_LIMIT && count % TAG_IMEI_LIMIT == 0) {
                    groupCount++;
                    String keyStr = getKeyStr2(key.toString(), groupCount);
                    outKey.set(keyStr);
                    result.put("tagList", listToArray(list));
                    outValue.set(JsonUtil.<Object>mapToJson(result));
                    multipleOutputs.write(outKey, outValue, "UserIdToTagList" + "/part");
                    context.getCounter("kvReducer", "COUNT2").increment(1l);
                    list.clear();
                    result.clear();
                }
                list.addAll(arrayToList(TagListUtil.getTagList(imei.toString(), tagListType)));
                count++;
            }
            groupCount++;
            String keyStr = getKeyStr2(key.toString(), groupCount);
            outKey.set(keyStr);
            result.put("tagList", listToArray(list));
            outValue.set(JsonUtil.<Object>mapToJson(result));
            multipleOutputs.write(outKey, outValue, "UserIdToTagList" + "/part");
            context.getCounter("kvReducer", "COUNT2").increment(1l);
        }
        // output kv3 data
        if (tagLogCountMap.size() > 0) {
            Map<Object, Object> result = new HashMap<Object, Object>();
            if (tagLogCountMap.size() <= TAG_USER_COUNT_LIMIT) {
                result.put("tag", tagLogCountMap);
            } else {
                // tog get top20
                class HashMapComparator implements
                        Comparator<Map.Entry<Integer, Long>> {
                    @Override
                    public int compare(Map.Entry<Integer, Long> o1,
                                       Map.Entry<Integer, Long> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                }
                List<Map.Entry<Integer, Long>> sortList = new ArrayList<Map.Entry<Integer, Long>>(
                        tagLogCountMap.entrySet());
                Collections.sort(sortList, new HashMapComparator());
                Map<Integer, Long> resultMap = new HashMap<Integer, Long>();
                for (int i = 0; i < TAG_USER_COUNT_LIMIT; i++) {
                    Map.Entry<Integer, Long> score = sortList.remove(0);
                    resultMap.put(score.getKey(), score.getValue());
                }
                result.put("tag", resultMap);
            }
            Map<Integer, Long> tagMap = (Map<Integer, Long>) result.get("tag");
            String tagListType = "t"; // by default
            for (Integer tagId : tagMap.keySet()) {
                if (this.detailMap.containsKey(tagId)) {
                    tagListType = this.detailMap.get(tagId);
                    break;
                }
            }
            // output tag list

            if (null != tagStr && tagStr.length() == 32) {
                result.put("tagList", TagListUtil.getTagList(tagStr, tagListType));
            }
            outKey.set(key);
            outValue.set(JsonUtil.<Object>mapToJson(result));
            multipleOutputs.write(outKey, outValue, "UserIdToTagList" + "/part");
            context.getCounter("kvReducer", "COUNT3").increment(1l);

        }

    }

    private List<String> arrayToList(String[] array) {
        List<String> list = new ArrayList<String>();
        if (array != null) {
            for (String a : array) {
                list.add(a);
            }
        }
        return list;
    }

    private String[] listToArray(List<String> list) {
        String[] array = new String[list.size()];
        if (list != null) {
            int i = 0;
            for (String l : list) {
                array[i++] = l;
            }
        }
        return array;
    }

    private String getKeyStr2(String key, int groupCount) {
        String[] fields = key.split("\\|");
        if (fields != null && fields.length == 4) {
            String keyOut = fields[0] + FIELD_SEPERATOR + fields[1] + FIELD_SEPERATOR
                    + fields[2] + FIELD_SEPERATOR
                    + Encrypter.encodeByMD5(fields[3] + FIELD_SEPERATOR + groupCount);
            return keyOut;
        }
        return null;
    }

    public void cleanup(Context context) throws IOException,
            InterruptedException {
        multipleOutputs.close();
    }
}
