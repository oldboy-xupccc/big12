package com.chuangdata.userprofile.job.ct.kvjob;

import com.chuangdata.framework.resource.tag.TagMatcher;
import com.chuangdata.userprofile.utils.HdfsPathUtil;
import com.chuangdata.userprofile.utils.NameToIdCovertor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author luxiaofeng
 * @date 2016-10-04
 */
public class KvAppAactionMapper extends
        Mapper<Object, Text, Text, ValueGenericWritable> {
    private static Logger LOG = Logger.getLogger(KvAppAactionMapper.class);
    private static SimpleDateFormat ORIGIN_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyyMMdd");
    private final String FIELD_SEPERATOR = "|";
    private TagMatcher tagMatcher;
    private int provinceId;
    private int networkTypeId;
    private boolean isResourceFileEncrypted = false;

    public void setup(Context context) throws IOException {
        Configuration configuration = context.getConfiguration();
        String tagFilePath = configuration
                .get("chuangdata.dmu.userprofile.tag.resource");
        isResourceFileEncrypted = configuration.getBoolean(
                "chuangdata.dmu.userprofile.resource.encrypted", true);
        tagMatcher = new TagMatcher(tagFilePath, isResourceFileEncrypted);
        LOG.info("the proviceName is "
                + HdfsPathUtil.getProvinceNameFromPath(context));
        provinceId = NameToIdCovertor.getProvinceIdByName(HdfsPathUtil
                .getProvinceNameFromPath(context));
        LOG.info("the netWorkType is "
                + HdfsPathUtil.getNetWorkTypeNameFromPath(context));
        networkTypeId = NameToIdCovertor.getNetWorkTypeIdByName(HdfsPathUtil
                .getNetWorkTypeNameFromPath(context));
        LOG.info("the province id is " + provinceId + " and networkTypeId is "
                + networkTypeId);
    }

    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] logs = value.toString().split(
                AppActionLogFields.RECORD_SEPERTOR);
        try {

            List<Integer> tagList = tagMatcher.getAllTagIdsByAction(Integer.valueOf(logs[AppActionLogFields.APP_ID]), Integer.valueOf(logs[AppActionLogFields.ACTION_TYPE_ID]), Integer.valueOf(logs[AppActionLogFields.DETAIL_ACTION_ID]));
            //LOG.info("the tag list size is " + tagList.size());
            if (tagList != null && 5 == tagList.size()) {
                int actionId = tagList.get(3);
                int detailActionId = tagList.get(4);
                // tag user count
                TagToUserCountModel tagToUserCountModel = buildTagToUserCountModel(logs);
                context.write(
                        new Text(buildTagToUserCountKey(logs, actionId)),
                        new ValueGenericWritable(tagToUserCountModel));
                context.getCounter("kvAppActionMapper",
                        "APP_ACTION_TAG_USER_COUNT").increment(1l);
                context.write(
                        new Text(buildTagToUserCountKey(logs,
                                detailActionId)), new ValueGenericWritable(
                                tagToUserCountModel));
                context.getCounter("kvAppActionMapper",
                        "APP_DETAIL_ACTION_TAG_USER_COUNT").increment(1l);

                // tag to userId
                Text imeiId = new Text(logs[AppActionLogFields.IMEI]);
                context.write(
                        new Text(buildTagToUserIdKey(logs, actionId)),
                        new ValueGenericWritable(imeiId));
                context.getCounter("kvAppActionMapper",
                        "APP_ACTION_IMEI_COUNT").increment(1l);
                context.write(
                        new Text(buildTagToUserIdKey(logs, detailActionId)),
                        new ValueGenericWritable(imeiId));
                context.getCounter("kvAppActionMapper",
                        "APP_DETAIL_ACTION_IMEI_COUNT").increment(1l);

                // userId to tag
                Text userId2tagKey = new Text();
                userId2tagKey.set(buildUserIdToTagKey(logs));
                context.write(userId2tagKey, new ValueGenericWritable(
                        buildUserIdToTagLogCountModel(logs, actionId)));
                context.getCounter("kvAppActionMapper",
                        "APP_ACTION_USERID_TO_TAG_COUNT").increment(1l);
                context.write(
                        userId2tagKey,
                        new ValueGenericWritable(
                                buildUserIdToTagLogCountModel(logs,
                                        detailActionId)));
                context.getCounter("kvAppActionMapper",
                        "APP_DETAIL_ACTION_USERID_TO_TAG_COUNT").increment(
                        1l);
            }
        } catch (Exception e) {
            // e.printStackTrace();
            LOG.error(e.toString());
            context.getCounter("kvAppActionMapper", "ERROR_RECORD_COUNT")
                    .increment(1l);
        }
    }

    TagToUserCountModel buildTagToUserCountModel(String[] logs) {
        TagToUserCountModel tagToUserCountModel = new TagToUserCountModel();
        tagToUserCountModel.setImeiId(new Text(logs[AppActionLogFields.IMEI]));
        tagToUserCountModel.setProvinceId(new IntWritable(provinceId));
        tagToUserCountModel.setLogCount(new LongWritable(1));
        return tagToUserCountModel;
    }

    String buildTagToUserCountKey(String[] logs, int tagId) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppActionLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(tagId);
        return builder.toString();
    }

    String buildTagToUserIdKey(String[] logs, int tagId) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppActionLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(tagId).append(FIELD_SEPERATOR);
        builder.append(Integer.valueOf(provinceId));
        return builder.toString();
    }

    UserIdToTagLogCountModel buildUserIdToTagLogCountModel(String[] logs,
                                                           int tagId) {
        UserIdToTagLogCountModel userIdToTagLogCountModel = new UserIdToTagLogCountModel();
        userIdToTagLogCountModel.setTagId(new IntWritable(tagId));
        userIdToTagLogCountModel.setLogCount(new LongWritable(1));
        // TODO if have phone or qq number to set to otherParam
        return userIdToTagLogCountModel;
    }

    String buildUserIdToTagKey(String[] logs) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppActionLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(Integer.valueOf(provinceId)).append(FIELD_SEPERATOR);
        builder.append(logs[AppActionLogFields.IMEI]);
        return builder.toString();
    }

    private String convertTime(String dateStr) {
        Date date = null;
        try {
            date = ORIGIN_DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            LOG.info("fail to convert time " + e.toString());
        }
        return DATE_FORMAT.format(date);
    }
}
