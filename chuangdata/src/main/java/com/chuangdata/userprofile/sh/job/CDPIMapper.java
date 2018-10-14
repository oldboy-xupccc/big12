package com.chuangdata.userprofile.sh.job;

import com.chuangdata.framework.encrypt.transform.IntToInt;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.BaseMapper;
import com.chuangdata.userprofile.job.CDPIKeyModel;
import com.chuangdata.userprofile.job.KeyModel;
import com.chuangdata.userprofile.model.ParamMapModel;
import com.chuangdata.userprofile.model.ParamModel;
import com.chuangdata.userprofile.utils.IPConvertor;
import com.chuangdata.userprofile.utils.SpecialParamHandler;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.userprofile.utils.TimeConvertor;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author luxiaofeng
 */
public class CDPIMapper extends BaseMapper<CDPIKeyModel> {
    private static final Logger LOG = Logger.getLogger(CDPIMapper.class);
    private Map<String, String[]> meidMap = new HashMap<String, String[]>();

    private void buildMeidMap(String file) throws FileNotFoundException, IOException {
        InputStream in = null;
        if (file.endsWith(".gz")) {
            in = new GZIPInputStream(new FileInputStream(file));
        } else {
            in = new FileInputStream(file);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf8"));
        long count = 0;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] info = line.split("\t");
            if (info.length == 3) {
                meidMap.put(info[0], info);
                count++;
            }
        }
        LOG.info("MEID Map Load " + count + " MEID.");
        reader.close();
        in.close();
    }

    public void setup(Context context) throws IOException {
        super.setup(context);
        String meidResourceFile = context.getConfiguration().get("chuangdata.dmu.userprofile.sh.meid");
        buildMeidMap(meidResourceFile);
    }

    private static final String LOG_TYPE = "5"; // just set 5 for shanghai ct cdpi

    protected boolean filterOut(LogModel logModel, Context context) {
        try {
            // meid15 maybe null, but sha meid should be there
            if (Strings.isEmpty(logModel.getMeid())) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_MEID).increment(1);
                return true;
            }
            if (Strings.isEmpty(getMEID15(logModel.getMeid()))) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_MEID15).increment(1);
                //return true;
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
                String uri = getUri(logModel);
                if (urlMatcher.getDomain(host, uri) == null) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_DOMAIN).increment(1);
                    return true;
                }
            } catch (FieldNotFoundException e) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                return true;
            }
            // 时间格式  20160314204208
            if (!Strings.isNotEmpty(logModel.getProcedureEndTime())) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_TIME).increment(1);
                return true;
            }
        } catch (FieldNotFoundException e) {

        }
        return false;
    }

    private String getMEID14(String sha) {
        if (this.meidMap.containsKey(sha)) {
            String[] meid = this.meidMap.get(sha);
            return meid[1].toLowerCase();
        } else {
            return "";
        }
    }

    private String getMEID15(String sha) {
        if (this.meidMap.containsKey(sha)) {
            String[] meid = this.meidMap.get(sha);
            return meid[2].toLowerCase();
        } else {
            return "";
        }
    }

    protected KeyModel buildKeyModel() {
        return new CDPIKeyModel();
    }

    /**
     * TODO imei,msisdn,imsi need MD5?
     */
    protected void buildKey(LogModel logModel, KeyModel key)
            throws Exception {
        CDPIKeyModel keyModel = (CDPIKeyModel) key;
        keyModel.setLogTypeId(LOG_TYPE);
        try {
            keyModel.setUserId(getMEID15(logModel.getMeid()));
        } catch (FieldNotFoundException e) {
        }
        try {
            keyModel.setMsisdn(logModel.getMsisdn());
        } catch (FieldNotFoundException e) {
        }
        try {
            keyModel.setImsi(logModel.getImsi());
        } catch (FieldNotFoundException e) {
        }
        try {
            // For shanghai mobile data, the format of time is yyyyMMddHHmmss
            keyModel.setTimeStamp(TimeConvertor.convertTime(logModel.getProcedureEndTime(), "yyyyMMddHHmmss") / 1000);
        } catch (FieldNotFoundException e) {
            keyModel.setTimeStamp(0);
        }
        try {
            AppUnit unit = urlMatcher.getApp(getHost(logModel));
            keyModel.setHost(unit == null ? "-1" : String.valueOf(unit.getId())); // actually it's app_host id
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
            DomainUnit domain = urlMatcher.getDomain(getHost(logModel), getUri(logModel));
            if (domain != null) {
                keyModel.setAppId(domain.getId()); // set domain id as app id
            } else {
                keyModel.setAppId(-1);
            }
            ActionUnit action = urlMatcher.getAction(getHost(logModel), getUri(logModel));
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
                            // We don't need to encrypt params
							/*if (userIdParamTypeSet.contains(unit.getId()) || unit.isUserId()) {
								paramValue = MD5Encrypt.getInstance().encryptWithSaltValue(paramValue);
							}*/
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
        StringBuilder otherParams = new StringBuilder();
        try {
            String nai = logModel.getNai();
            int index = nai.indexOf("@");
            if (index > 0) {
                nai = nai.substring(0, index);
                if (Strings.isNotEmpty(nai)) {
                    otherParams.append("1=").append(nai);
                }
            }
        } catch (FieldNotFoundException e) {
        }
        otherParams.append("&").append("2=").append(logModel.getMeid().toLowerCase());
        otherParams.append("&").append("3=").append(getMEID14(logModel.getMeid()));
        keyModel.setOtherParams(otherParams.toString());
    }
}
