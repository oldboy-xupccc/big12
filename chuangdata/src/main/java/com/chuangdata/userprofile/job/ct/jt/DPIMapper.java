package com.chuangdata.userprofile.job.ct.jt;

import com.chuangdata.framework.resource.UrlMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.model.*;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.userprofile.utils.TimeConvertor;
import com.google.common.annotations.VisibleForTesting;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.logmodel.parser.LogModelParser;
import com.chuangdata.framework.logmodel.parser.LogModelParserException;
import com.chuangdata.framework.logmodel.parser.LogModelParserFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


/**
 * Almost copy & paste from ExtractMapper, it's terrible!!!
 * But time is the criminal
 *
 * @author luxiaofeng
 */
public class DPIMapper extends Mapper<LongWritable, Text, Text, MultipleModel> {
    private static final Logger LOG = Logger.getLogger(DPIMapper.class);
    protected LogModelParser logModeParser;

    protected UrlMatcher urlMatcher;

    protected Text outkey = new Text();

    public void setup(Context context) throws IOException {
        Configuration configuration = context.getConfiguration();
        String logConfigFilePath = configuration.get("chuangdata.log.config");
        if (logConfigFilePath != null) {
            try {
                this.logModeParser = LogModelParserFactory
                        .getTextLogModelParserFromExternalConfig(logConfigFilePath);
            } catch (LogModelParserException e) {
                LOG.error("Init logModelParser error: ", e);
            }
        }
        String appHostFilePath = configuration.get("chuangdata.dmu.userprofile.app.host");
        String appActionFilePath = configuration.get("chuangdata.dmu.userprofile.app.action");
        String appParamFilePath = configuration.get("chuangdata.dmu.userprofile.app.param");
        urlMatcher = new UrlMatcher(appHostFilePath, appActionFilePath, appParamFilePath, true);
    }

    @VisibleForTesting
    protected void setLogModelParser(LogModelParser parser) {
        this.logModeParser = parser;
    }

    @VisibleForTesting
    protected void setUrlMatcher(UrlMatcher matcher) {
        this.urlMatcher = matcher;
    }

    public void map(LongWritable key, Text value, Context context) {
        try {
            // key is time
            LogModel logModel = logModeParser.parse(value.toString());
            // 1. filter by something
            if (filterOut(logModel, context)) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT).increment(1);
                return;
            }
            ExtractMd5EncrypterKeyModel keyModel = buildKey(logModel);
            MultipleModel valueModel = buildValue(logModel);

            outkey.set(keyModel.toString());
            context.write(outkey, valueModel);
        } catch (Exception e) {
            LOG.error(e);
            context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
        }
    }

    private MultipleModel buildValue(LogModel logModel) {
        MultipleModel value = new MultipleModel();
        try {
            AppModel appModel = extractApp(logModel);
            if (appModel.getAppId().get() > 0) {
                value.add(appModel);
                ActionModel actionModel = extractAction(logModel);
                if (actionModel.getId() > 0) {
                    value.add(actionModel);
                    // Disable param extraction as we are not able to output params
                    /*ParamMapModel paramMap = extractParam(logModel, actionModel.getId());
                    value.add(paramMap);*/
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return value;
    }

    private AppModel extractApp(LogModel logModel) throws FieldNotFoundException {
        AppModel appModel = new AppModel();
        AppUnit appUnit = urlMatcher.getApp(getHost(logModel));
        if (appUnit != null) {
            appModel.setAppId(appUnit.getAppId());
            appModel.setAppTypeId(appUnit.getAppTypeId());
            appModel.setLogCount(1L);
        }
        return appModel;
    }

    private ActionModel extractAction(LogModel logModel) throws FieldNotFoundException {
        ActionModel actionModel = new ActionModel();
        ActionUnit unit = urlMatcher.getAction(getHost(logModel), getUri(logModel));
        if (unit != null) {
            actionModel.setId(unit.getId());
            actionModel.setHost(unit.getHost());
            actionModel.setUrlPatternStr(unit.getUrlPatternStr());
            actionModel.setDetailActionId(unit.getDetailActionId());
            actionModel.setActionTypeId(unit.getActionTypeId());
            actionModel.setInterestTag(unit.getInterestTag());
            actionModel.setAppId(unit.getAppId());
            actionModel.setAppTypeId(unit.getAppTypeId());
            actionModel.setLogCount(1L);
        }
        return actionModel;
    }

    private ParamMapModel extractParam(LogModel logModel, long actionId) throws FieldNotFoundException {
        ParamMapModel paramMap = new ParamMapModel();
        List<ParamUnit> paramUnits = urlMatcher.getParam(actionId, logModel);
        if (paramUnits != null && !paramUnits.isEmpty()) {
            for (ParamUnit unit : paramUnits) {
                ParamModel paramModel = new ParamModel();
                paramModel.setId(unit.getId());
                paramModel.setActionId(unit.getActionId());
                paramModel.setParam(unit.getParam());
                paramModel.setParamValue(unit.getParamValue(), unit.isUserId());
                paramModel.setParamTypeId(unit.getParamTypeId());
                paramModel.setLogCount(1L);
                paramMap.addParam(paramModel);
            }
        }
        return paramMap;
    }

    private ExtractMd5EncrypterKeyModel buildKey(LogModel logModel) {
        ExtractMd5EncrypterKeyModel key = new ExtractMd5EncrypterKeyModel();
        try {
            try {
                key.setTime(getTime(logModel));
            } catch (NumberFormatException e) {
                // DO NOTHING?
                key.setTime(0);
            } catch (ParseException pex) {
                // DO NOTHING?
                key.setTime(0);
            }
        } catch (FieldNotFoundException e) {
            // TODO time should not be null
        }
        try {
            // For ct cloud data, use to lowerCase
            key.setEncryptedMsisdn(logModel.getMsisdn().toLowerCase());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            // For ct cloud data, use to lowerCase
            key.setEncryptedImei(logModel.getImei().toLowerCase());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            // For ct cloud data, use to lowerCase
            key.setEncryptedImsi(logModel.getImsi().toLowerCase());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            key.setUserIp(logModel.getClientIP());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        return key;
    }

    private long getTime(LogModel logModel) throws FieldNotFoundException, ParseException {
        // TODO to find a better way to parse time with different format
        try {
            String timeStr = logModel.getProcedureEndTime();
            long time = Long.parseLong(timeStr);
            if (logModel.getProcedureEndTime().startsWith("20") && timeStr.length() == 14) {
                time = TimeConvertor.convertTime(logModel.getProcedureEndTime(), "yyyyMMddHHmmss");
                return time;
            }
            if (time > 4000000000L) {
                // ms
                return time / 1000;
            }
            return time;
        } catch (NumberFormatException e) {
            try {
                long time = TimeConvertor.convertTime(logModel.getProcedureEndTime());
                return time;
            } catch (ParseException ex) {
                throw ex;
            }
        }
    }

    private boolean filterOut(LogModel logModel, Context context) {
        // 1. Host should not be null
        try {
            String host = getHost(logModel);
            if (!Strings.isNotEmpty(host)) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1L);
                return true;
            }
            if (urlMatcher.getApp(host) == null) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_APP).increment(1L);
                return true;
            }
        } catch (FieldNotFoundException e) {
            // Host not found, then nothing can do for userprofile
            context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1L);
            return true;
        }
        // 2. Url should not be null
        try {
            String url = getUri(logModel);
            if (!Strings.isNotEmpty(url)) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1L);
                return true;
            }
        } catch (FieldNotFoundException e) {
            context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1L);
            // url is a must field for userprofile system
            return true;
        }
        // 3. time is 0
        try {
            long time = getTime(logModel);
            if (time <= 0) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1L);
                return true;
            }
        } catch (FieldNotFoundException e) {
            context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1L);
            // time is a must field for userprofile system
            return true;
        } catch (ParseException e) {
            context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1L);
            // time is a must field for userprofile system
            return true;
        }
        return false;
    }

    @VisibleForTesting
    protected String getHost(LogModel logModel) throws FieldNotFoundException {
        if (Strings.isNotEmpty(logModel.getUri())) {
            // uri不为空，检查uri中是否包含host，是则以uri为准
            String uri = logModel.getUri();
            if (uri.startsWith("http://")) {
                uri = uri.substring(7);
            }
            int index = uri.indexOf("/");
            if (index > 0) {
                // uri中包含host
                return uri.substring(0, index);
            }
        }
        return logModel.getHost();
    }

    @VisibleForTesting
    protected String getUri(LogModel logModel) throws FieldNotFoundException {
        // 若host不为空，但uri为空，则设为/
        if (Strings.isNotEmpty(logModel.getHost()) && !Strings.isNotEmpty(logModel.getUri())) {
            return "/";
        } else if (Strings.isNotEmpty(logModel.getHost()) && Strings.isNotEmpty(logModel.getUri())) {
            // host, uri are not empty, 去掉uri中的host
            String uri = logModel.getUri();
            if (uri.startsWith("http://")) {
                uri = uri.substring(7);
            }
            int index = uri.indexOf("/");
            if (index > 0) {
                // uri中包含host
                uri = uri.substring(index);
            }
            return uri;
        }
        return logModel.getUri();
    }
}
