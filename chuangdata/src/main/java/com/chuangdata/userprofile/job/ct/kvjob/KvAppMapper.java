package com.chuangdata.userprofile.job.ct.kvjob;

import com.chuangdata.framework.resource.tag.TagMatcher;
import com.chuangdata.framework.resource.tag.TagUnit;
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

/**
 * @author luxiaofeng
 * @date 2016-10-04
 */
public class KvAppMapper extends
        Mapper<Object, Text, Text, ValueGenericWritable> {
    private static Logger LOG = Logger.getLogger(KvAppMapper.class);
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
        String tagFilePath = configuration.get("chuangdata.dmu.userprofile.tag.resource");
        isResourceFileEncrypted = configuration.getBoolean(
                "chuangdata.dmu.userprofile.resource.encrypted", true);
        tagMatcher = new TagMatcher(tagFilePath, isResourceFileEncrypted);
        LOG.info("the proviceName is " + HdfsPathUtil
                .getProvinceNameFromPath(context));
        provinceId = NameToIdCovertor.getProvinceIdByName(HdfsPathUtil
                .getProvinceNameFromPath(context));
        LOG.info("the netWorkType is " + HdfsPathUtil
                .getNetWorkTypeNameFromPath(context));
        networkTypeId = NameToIdCovertor.getNetWorkTypeIdByName(HdfsPathUtil
                .getNetWorkTypeNameFromPath(context));
        LOG.info("the province id is " + provinceId + " and networkTypeId is " + networkTypeId);
    }

    public void map(Object key, Text value, Context context) {
        String[] logs = value.toString().split(AppLogFields.RECORD_SEPERTOR);
        try {
            // tag user count
            TagToUserCountModel tagToUserCountModel = buildTagToUserCountModel(logs);
            TagUnit tagUnit = tagMatcher.getTagUnitById(Integer.valueOf(logs[AppLogFields.APP_ID]));
            if (tagUnit != null) {
                context.write(
                        new Text(buildTagToUserCountKey(logs, tagUnit.getTagId())),
                        new ValueGenericWritable(tagToUserCountModel));
                context.getCounter("kvAppMapper", "APP_NAME_TAG_TO_USER_COUNT").increment(1l);
                context.write(
                        new Text(buildTagToUserCountKey(logs,
                                tagUnit.getTagTypeId())),
                        new ValueGenericWritable(tagToUserCountModel));
                context.getCounter("kvAppMapper", "APP_TYPE_TAG_TO_USER_COUNT").increment(1l);

                //output tag id 0
                context.write(
                        new Text(buildTagToUserCountKey(logs,
                                0)),
                        new ValueGenericWritable(tagToUserCountModel));
                context.getCounter("kvAppMapper", "APP_ZERO_TAG_TO_USER_COUNT").increment(1l);

                // tag to userId
                Text imeiId = new Text(logs[AppLogFields.IMEI]);
                context.write(
                        new Text(buildTagToUserIdKey(logs, tagUnit.getTagId())),
                        new ValueGenericWritable(imeiId));
                context.getCounter("kvAppMapper", "APP_NAME_TAG_TO_USERID_COUNT").increment(1l);
                context.write(
                        new Text(
                                buildTagToUserIdKey(logs, tagUnit.getTagTypeId())),
                        new ValueGenericWritable(imeiId));
                context.getCounter("kvAppMapper", "APP_TYPE_TAG_TO_USERID_COUNT").increment(1l);


                //out put tag id 0

                context.write(
                        new Text(
                                buildTagToUserIdKey(logs, 0)),
                        new ValueGenericWritable(imeiId));
                context.getCounter("kvAppMapper", "APP_ZERO_TAG_TO_USERID_COUNT").increment(1l);

                // userId to tag
                Text userId2tagKey = new Text();
                userId2tagKey.set(buildUserIdToTagKey(logs));
                context.write(userId2tagKey, new ValueGenericWritable(
                        buildUserIdToTagLogCountModel(logs, tagUnit.getTagId())));
                context.getCounter("kvAppMapper", "APP_NAME_USERID_TO_TAG_COUNT").increment(1l);

                context.write(
                        userId2tagKey,
                        new ValueGenericWritable(buildUserIdToTagLogCountModel(
                                logs, tagUnit.getParentTagId())));
                context.getCounter("kvAppMapper", "APP_TYPE_USERID_TO_TAG_COUNT").increment(1l);
            }
        } catch (Exception e) {
            LOG.error(e.getCause().toString());
            context.getCounter("kvAppMapper", "ERROR_RECORD_COUNT").increment(1l);
        }
    }

    TagToUserCountModel buildTagToUserCountModel(String[] logs) {
        TagToUserCountModel tagToUserCountModel = new TagToUserCountModel();
        tagToUserCountModel.setImeiId(new Text(logs[AppLogFields.IMEI]));
        tagToUserCountModel.setProvinceId(new IntWritable(provinceId));
        tagToUserCountModel.setLogCount(new LongWritable(1));
        return tagToUserCountModel;
    }

    String buildTagToUserCountKey(String[] logs, int tagId) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(tagId);
        return builder.toString();
    }

    String buildTagToUserIdKey(String[] logs, int tagId) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(Integer.valueOf(provinceId)).append(FIELD_SEPERATOR);
        builder.append(tagId);
        return builder.toString();
    }

    UserIdToTagLogCountModel buildUserIdToTagLogCountModel(String[] logs,
                                                           int tagId) {
        UserIdToTagLogCountModel userIdToTagLogCountModel = new UserIdToTagLogCountModel();
        userIdToTagLogCountModel.setTagId(new IntWritable(tagId));
        userIdToTagLogCountModel.setLogCount(new LongWritable(1));
        // TODO if have phone or qq number to set to otherParam
        userIdToTagLogCountModel.setOtherParam(new Text(logs[AppLogFields.MSISDN]));
        return userIdToTagLogCountModel;
    }

    String buildUserIdToTagKey(String[] logs) {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(logs[AppLogFields.TIME])).append(
                FIELD_SEPERATOR);// get the day
        builder.append(networkTypeId).append(FIELD_SEPERATOR);
        builder.append(Integer.valueOf(provinceId)).append(FIELD_SEPERATOR);
        builder.append(logs[AppLogFields.IMEI]);
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
