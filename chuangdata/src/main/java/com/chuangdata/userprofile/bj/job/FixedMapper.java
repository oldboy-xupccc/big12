package com.chuangdata.userprofile.bj.job;

import com.chuangdata.framework.encrypt.MD5Encrypt;
import com.chuangdata.framework.encrypt.transform.IntToInt;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.BaseMapper;
import com.chuangdata.userprofile.job.KeyModel;
import com.chuangdata.userprofile.model.ParamMapModel;
import com.chuangdata.userprofile.model.ParamModel;
import com.chuangdata.userprofile.utils.IPConvertor;
import com.chuangdata.userprofile.utils.SpecialParamHandler;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author luxiaofeng
 */
public class FixedMapper<K extends KeyModel> extends BaseMapper<K> {
    private static final Logger LOG = Logger.getLogger(FixedMapper.class);
    private static final String LOG_TYPE = "2"; // just set 2 for zj_ct_m

    protected boolean filterOut(LogModel logModel, Context context) {
        try {
            if (!Strings.isNotEmpty(logModel.getMeid())) {
                // only in zj ct
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_MEID).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(logModel.getUri())) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1);
                return true;
            }
            try {
                String host = getHost(logModel);
                if (!Strings.isNotEmpty(host)) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1);
                    return true;
                }
                if (urlMatcher.getApp(host) == null) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                    return true;
                }
            } catch (FieldNotFoundException e) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(logModel.getProcedureEndTime()) || Long.parseLong(logModel.getProcedureEndTime()) == 0) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1);
                return true;
            }
        } catch (FieldNotFoundException e) {

        }
        return false;
    }

    protected String getHost(LogModel logModel) throws FieldNotFoundException {
        // String host = logModel.getHost();
        // ZJ CT Fixed log doesn't contain host, we should extract host from uri
        String uri = logModel.getUri();
        if (!Strings.isNotEmpty(uri)) {
            return "";
        }
        if (uri.startsWith("http://")) {
            uri = uri.substring(7);
        }
        int index = uri.indexOf("/");
        if (index > 0) {
            return uri.substring(0, index);
        }
        return "";
    }

    protected void buildKey(LogModel logModel, KeyModel keyModel)
            throws Exception {
        keyModel.setLogTypeId(LOG_TYPE);
        try {
            keyModel.setUserId(logModel.getMeid());
        } catch (FieldNotFoundException e) {
        }
        try {
            // for zj fixed log, convert from ms to s
            keyModel.setTimeStamp(Long.parseLong(logModel.getProcedureEndTime()) / 1000);
        } catch (FieldNotFoundException e) {
            keyModel.setTimeStamp(0);
        }
        try {
            AppUnit unit = urlMatcher.getApp(getHost(logModel));
            keyModel.setHost(unit == null ? "-1" : String.valueOf(unit.getId()));
        } catch (FieldNotFoundException e) {
        }
        try {
            String referer = logModel.getReferer();
            AppUnit unit = urlMatcher.getApp(getRefererHost(referer));
            keyModel.setRefererHost(unit == null ? "-1" : String.valueOf(unit.getId()));
        } catch (FieldNotFoundException e) {
        }
        try {
            String userAgent = logModel.getUserAgent();
            String[] info = getInfoFromUA(userAgent);
            keyModel.setOs(info[0]);
            keyModel.setTerminal(info[1]);
            keyModel.setTerminalProducer(info[2]);
            keyModel.setBrowser(info[3]);
        } catch (FieldNotFoundException e) {
        }
        try {
            String cookie = logModel.getCookie();
            keyModel.setCookie(getInfoFromCookie(cookie));
        } catch (FieldNotFoundException e) {
        }
        /*try {
			keyModel.setUserIp(logModel.getClientIP());
		} catch (FieldNotFoundException e) {
		}*/
        try {
            keyModel.setServerIp(String.valueOf(IPConvertor.ipToLong(logModel.getServerIP())));
        } catch (FieldNotFoundException e) {
        }
        /**
         * Set appId, appActionId, urlParams
         */
        try {
            AppUnit app = urlMatcher.getApp(getHost(logModel));
            if (app != null) {
                keyModel.setAppId(app.getId());
            } else {
                keyModel.setAppId(-1);
            }
            ActionUnit action = urlMatcher.getAction(getHost(logModel), logModel.getUri());
            if (action != null) {
                keyModel.setAppActionId(action.getId());
                ParamMapModel paramMap = new ParamMapModel();
                boolean isSepecialParam = SpecialParamHandler.paramHandle((int) action.getId(), getUri(logModel), paramMap);
                if (isSepecialParam) {
                    StringBuilder builder = new StringBuilder();
                    // TODO append paramMap to builder
                    for (ParamModel m : paramMap.getParamModels()) {
                        if (builder.length() > 0) {
                            builder.append("&");
                        }
                        String paramValue = m.getParamValue();
                        int actionId = m.getActionId();
                        int paramTypeId = m.getParamTypeId();
                        builder.append(IntToInt.encrypt(String.valueOf(actionId)) + "\002"
                                + IntToInt.encrypt(String.valueOf(paramTypeId)) + "=" + paramValue);
                    }
                    keyModel.setUrlParams(builder.toString());
                } else {
                    List<ParamUnit> params = urlMatcher.getParam(action.getId(), logModel);
                    if (params != null) {
                        StringBuilder builder = new StringBuilder();
                        for (ParamUnit unit : params) {
                            if (builder.length() > 0) {
                                builder.append("&");
                            }
                            String paramValue = unit.getParamValue();
                            if (userIdParamTypeSet.contains(unit.getId()) || unit.isUserId()) {
                                paramValue = MD5Encrypt.getInstance().encrypt(paramValue);
                            }
                            builder.append(IntToInt.encrypt(String.valueOf(unit.getId())) + "=" + paramValue);
                        }
                        keyModel.setUrlParams(builder.toString());
                    }
                }
            } else {
                keyModel.setAppActionId(-1);
            }
        } catch (FieldNotFoundException e) {
        }
        /** Set other params */
        // currently other params is empty
        StringBuilder otherParams = new StringBuilder();
        keyModel.setOtherParams(otherParams.toString());
    }
}
