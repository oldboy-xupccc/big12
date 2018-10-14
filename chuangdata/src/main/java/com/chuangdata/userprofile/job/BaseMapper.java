package com.chuangdata.userprofile.job;


import com.chuangdata.framework.encrypt.MD5Encrypt;
import com.chuangdata.framework.encrypt.transform.IntToInt;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.logmodel.parser.LogModelParser;
import com.chuangdata.framework.logmodel.parser.LogModelParserException;
import com.chuangdata.framework.logmodel.parser.LogModelParserFactory;
import com.chuangdata.framework.resource.UrlMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.model.ParamMapModel;
import com.chuangdata.userprofile.model.ParamModel;
import com.chuangdata.userprofile.utils.IPConvertor;
import com.chuangdata.userprofile.utils.SpecialParamHandler;
import com.chuangdata.userprofile.utils.Strings;
import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.net.URLDecoder;
import org.apache.hadoop.hive.serde2.typeinfo.*;
import org.apache.hadoop.hive.serde2.objectinspector.*;
import java.util.*;
/**
 * 从话单中提取所需要的字段
 *
 * @author luxiaofeng
 */
public abstract class BaseMapper<K extends KeyModel> extends Mapper<Object, OrcStruct, K, Metrics> {
    private static final Logger LOG = Logger.getLogger(BaseMapper.class);

    protected SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final Set<Integer> userIdParamTypeSet = new HashSet<Integer>();


    private static final String LOG_TYPE = "1"; // just set 1 for zj_ct_m

    protected LogModelParser logModeParser;

    protected UrlMatcher urlMatcher;

    @VisibleForTesting
    public LogModelParser getLogModelParser() {
        return this.logModeParser;
    }

    @VisibleForTesting
    public void setUrlMatcher(UrlMatcher matcher) {
        this.urlMatcher = matcher;
    }

    public void setup(Context context) throws IOException {
        Configuration configuration = context.getConfiguration();
        String logConfigFilePath = configuration.get("chuangdata.log.config");
        if (logConfigFilePath != null) {
            try {
                this.logModeParser = LogModelParserFactory
                        .getTextLogModelParserFromExternalConfig(logConfigFilePath);
                LOG.info("Initialized logModelParser");
            } catch (LogModelParserException e) {
                LOG.error("Init logModelParser error: ", e);
            }
        }
        String appHostFilePath = configuration.get("chuangdata.dmu.userprofile.app.host");
        String appActionFilePath = configuration.get("chuangdata.dmu.userprofile.app.action");
        String appParamFilePath = configuration.get("chuangdata.dmu.userprofile.app.param");
        String domainFilePath = configuration.get("chuangdata.dmu.userprofile.app.domain");
        String resourceEncrypted = configuration.get("chuangdata.dmu.userprofile.resource.encrypted", "true");

        LOG.info("Initializing url matcher...");
        urlMatcher = new UrlMatcher(appHostFilePath, appActionFilePath, appParamFilePath, domainFilePath, Boolean.parseBoolean(resourceEncrypted));
        LOG.info("UrlMatcher: " + urlMatcher.toString());
    }

    @VisibleForTesting
    public void setLogModelParser(LogModelParser logModelParser) {
        this.logModeParser = logModelParser;
    }

    public void map(Object key, OrcStruct value, Context context) {
        try {
            String SCHEMA = "struct<imsi:string,imei:string,msisdn:string,begin_time:string,end_time:string,prot_category:string,prot_type:string,host:string," +
                    "fst_uri:string,user_agent:string,server_ip:string,server_port:string,l4_ul_throughput:float,l4_dw_throughput:float,eci:string>";
            OrcStruct struct = (OrcStruct)value;
            TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(SCHEMA);
            StructObjectInspector inspector = (StructObjectInspector)OrcStruct.createObjectInspector(typeInfo);

            String log = this.getLog(new String[]{"imsi", "imei", "msisdn", "begin_time", "end_time", "prot_category", "prot_type","host", "fst_uri", "user_agent", "server_ip", "server_port", "l4_ul_throughput", "l4_dw_throughput", "eci"},inspector,struct);

//            Configuration configuration = context.getConfiguration();
//            Long index=Long.parseLong(configuration.get("chuangdata.dmu.userprofile.logIndex"));
//            if(index<20) {
//                LOG.error("orc记录：" + value.toString());
//                LOG.error("log记录：" + log);
//                index++;
//                configuration.set("chuangdata.dmu.userprofile.logIndex", index.toString());
//            }

            LogModel logModel = logModeParser.parse(log);
            // 1. filter by something
            if (filterOut(logModel, context)) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT).increment(1);
                return;
            }

            // 2. build key
            KeyModel keyModel = buildKeyModel();
            buildKey(logModel, keyModel);

            if (filterByApp(keyModel)) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_ACTION).increment(1);
                return;
            }
            // 3. calculate QoS metrics
            Metrics metricsModel = new Metrics();
            buildValue(logModel, metricsModel);
            context.write((K) keyModel, metricsModel);
        } catch (Exception e) {
            LOG.error(e);
            context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR).increment(1);
        }
    }

    private String getLog(String[] fields,StructObjectInspector inspector,OrcStruct struct){
        String log="";
        try{
        for(Integer i=0;i<fields.length-1;i++) {
            try {
                if (fields[i] == "l4_ul_throughput" || fields[i] == "l4_dw_throughput") {
                    String value="";
                    try {
                        value = inspector.getStructFieldData(struct, inspector.getStructFieldRef(fields[i])).toString().trim();
                    }
                    catch (Exception e){
                        value="0";
                        log = log + "0^";
                        continue;
                    }
                    if (value == null || value.length() <= 0) {
                        log = log + "0^";
                    } else {
                        log = log + value.substring(0, value.indexOf(".")) + "^";
                    }
                }else if(fields[i] == "begin_time"|| fields[i] == "end_time"){
                    Long millionSeconds=Long.parseLong(inspector.getStructFieldData(struct, inspector.getStructFieldRef(fields[i])).toString().trim());
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(millionSeconds);
                    Date date = c.getTime();
                    log = log + DATE_FORMAT.format(date)+ "^";
                }
                else {
                    log = log + inspector.getStructFieldData(struct, inspector.getStructFieldRef(fields[i])).toString().trim() + "^";
                }
            }
            catch (Exception e){
                log = log +"^";
            }
        }

        String lastfield=inspector.getStructFieldData(struct, inspector.getStructFieldRef(fields[fields.length-1])).toString().trim();
        if(lastfield == null || lastfield.length() <= 0){
            log =log+ "0";
        }else {
            log = log + lastfield;
        }
        }catch (Exception e){
            LOG.error(e);
        }
        return log;
// return "460020010058974^353286079470801^ec4791a3507127a78fdf64600c93e0b5^1506775823395^1506775823465^1^9^wx.qlogo.cn^http://wx.qlogo.cn/mmhead/Q3auHgzwzM7hAwf9eS5vaY9S8txxnaF5MB3Q5ZHj1CsvEkKQAIMtWQ/96^Mozilla/5.0 (iPhone; CPU iPhone OS 10_2_1 like Mac OS X) AppleWebKit/602.4.6 (KHTML, like Gecko) Mobile/14D27 MicroMessenger/6.5.16 NetType/4G Language/zh_CN^111.30.131.122^80^525^4103^75342009";
    }

    protected boolean filterByApp(KeyModel keyModel) {
        return keyModel.getAppId().get() < 0 || keyModel.getAppActionId().get() < 0;
    }

    protected KeyModel buildKeyModel() {
        return new KeyModel();
    }

    protected boolean filterOut(LogModel logModel, Context context) {
        try {
            if (!Strings.isNotEmpty(getHost(logModel))) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(logModel.getMeid())) {
                // only in zj ct
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_MEID).increment(1);
                return true;
            }
            try {
                String host = getHost(logModel);
                if (!Strings.isNotEmpty(host)) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1);
                    return true;
                }
                if (urlMatcher.getApp(host) == null) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_APP).increment(1);
                    return true;
                }
            } catch (FieldNotFoundException e) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1);
                return true;
            }
            if (!Strings.isNotEmpty(getUri(logModel))) {
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1);
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

    protected void buildKey(LogModel logModel, KeyModel keyModel)
            throws Exception {
        keyModel.setLogTypeId(LOG_TYPE);
        try {
            keyModel.setUserId(logModel.getMeid());
        } catch (FieldNotFoundException e) {
        }
        try {
            /*long time = 0L;
            try {
				time = DATE_FORMAT.parse(logModel.getProcedureEndTime()).getTime();
			} catch (ParseException e) {
				// parse error, then it should be UTC
				time = Long.parseLong(logModel.getProcedureEndTime());
			}*/
            // For zhejiang data, just set time
            keyModel.setTimeStamp(Long.parseLong(logModel.getProcedureEndTime()));
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


            String v_host = getHost(logModel);
            String v_uri = getUri(logModel);
            ActionUnit action=urlMatcher.getAction(v_host, v_uri);
            if(action == null) {
                v_host = v_host.substring(v_host.indexOf(".") + 1);
                while (v_host.indexOf(".") != -1) {
                    action = urlMatcher.getAction(v_host, v_uri);
                    if (null != action && action.getId() > 0) {
                        break;
                    }
                    v_host = v_host.substring(v_host.indexOf(".") + 1);
                }
            }

            if (action != null) {
                keyModel.setAppActionId(action.getId());
                ParamMapModel paramMap = new ParamMapModel();
                //boolean isSepecialParam = SpecialParamHandler.paramHandle((int) action.getId(), getUri(logModel), paramMap);
//                if (isSepecialParam) {
//                    StringBuilder builder = new StringBuilder();
//                    // TODO append paramMap to builder
//                    for (ParamModel m : paramMap.getParamModels()) {
//                        if (builder.length() > 0) {
//                            builder.append("&");
//                        }
//                        String paramValue = m.getParamValue();
//                        int actionId = m.getActionId();
//                        int paramTypeId = m.getParamTypeId();
//                        builder.append(IntToInt.encrypt(String.valueOf(actionId)) + "\002"
//                                + IntToInt.encrypt(String.valueOf(paramTypeId)) + "=" + paramValue);
//                    }
//                    keyModel.setUrlParams(builder.toString());
//                } else {
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
//                }
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
		/*try {
			String userZoneId = logModel.getUserZone();
			if (Strings.isNotEmpty(userZoneId)) {
				if (otherParams.length() > 0) {
					otherParams.append("&");
				}
				otherParams.append("2=").append(userZoneId);
			}
		} catch (FieldNotFoundException e) {
		}
		try {
			String bsid = logModel.getBsId();
			if (Strings.isNotEmpty(bsid)) {
				if (otherParams.length() > 0) {
					otherParams.append("&");
				}
				otherParams.append("3=").append(bsid);
			}
		} catch (FieldNotFoundException e) {
		}*/
        keyModel.setOtherParams(otherParams.toString());
    }

    @VisibleForTesting
    protected String getHost(LogModel logModel) throws FieldNotFoundException {
        if (Strings.isNotEmpty(logModel.getHost()) && (logModel.getHost().indexOf(".") != -1)) {//host非空、有效host
            String host = logModel.getHost();
            int index = host.indexOf(":");
            if (index > 0) {
                host =  host.substring(0, index);
            }
            host = host.replaceAll("/","");
            return host;
            //还可以判斷是否存在点的情况，确定是否有效
        }else if (Strings.isNotEmpty(logModel.getUri())) {
            String uri = logModel.getUri();
            if (uri.toLowerCase().startsWith("http://")) {
                uri = uri.substring(7);
                if (uri.toLowerCase().startsWith("http://")) {
                    uri = uri.substring(7);
                }
            }
            int index = uri.indexOf("/");
            if (index > 0) {
                String temp = uri.substring(0, index);
                if(temp.indexOf(".") != -1){//有效host
                    return  temp;
                }
            }
            //还可以判斷是否存在点的情况，确定是否有效
        }
        return null;
    }

    @VisibleForTesting
    protected String getUri(LogModel logModel) throws FieldNotFoundException {
        if (!Strings.isNotEmpty(logModel.getUri())) {// uri为空
            if(Strings.isNotEmpty(logModel.getHost())){//host不为空，则设为/
                return "/";
            }
        } else{//uri不为空
            // host, uri are not empty, 去掉uri中的host
            String uri = null;
            try {
                uri = URLDecoder.decode(logModel.getUri(),"UTF-8");
            } catch (Exception e) {
                try {
                    uri = URLDecoder.decode(logModel.getUri().replaceAll("%","%25"),"UTF-8");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            //二次去掉前面部分
            if (uri.toLowerCase().startsWith("http://")) {
                uri = uri.substring(7);
                if (uri.toLowerCase().startsWith("http://")) {
                    uri = uri.substring(7);
                }
            }

            int index = uri.indexOf("/");
            if (index > 0) {
                // uri中包含host
                String newhost = uri.substring(0,index);
                //host去掉端口号
                String oldhost = logModel.getHost();
                int oldindex = oldhost.indexOf(":");
                if (oldindex > 0) {
                    oldhost =  oldhost.substring(0, oldindex);
                }
                oldhost = oldhost.replaceAll("/","");
                if(newhost.toLowerCase().equals(oldhost.toLowerCase())){
                    uri = uri.substring(index);//如果host部分相同才截取后面部分
                }
            }
            if (!uri.startsWith("/")) {
                return "/".concat(uri);//取整个做为uri
            }else{
                return uri;//取整个做为uri
            }
        }
        return null;
    }

    protected String getRefererHost(String referer) {
        if (!Strings.isNotEmpty(referer)) {
            return "";
        }
        if (referer.startsWith("http://")) {
            referer = referer.substring(7);
        }
        int index = referer.indexOf("/");
        if (index >= 0) {
            return referer.substring(0, index);
        }
        return referer;
    }

    protected String[] getInfoFromUA(String userAgent) {
        String[] info = new String[4];
        // TODO extract info from user agent

        String tmp[] = userAgent.split("\\;");
        for (int i = 0; i < 4; i++) {
            if (tmp.length > i && null != tmp[i]) {
                info[i] = tmp[i];
            } else {
                info[i] = "";
            }
        }

        return info;
    }

    protected String getInfoFromCookie(String cookie) {
        // TODO get cookie info
        return "";
    }

    protected void buildValue(LogModel logModel, Metrics metrics) {
        /**
         * private LongWritable logCount; private LongWritable upByte; private
         * LongWritable dnByte;
         */
        metrics.setLogCount(1L);
        try {
            metrics.setUpByte(Long.parseLong(logModel.getULData()));
        } catch (FieldNotFoundException e) {
        }
        try {
            metrics.setDnByte(Long.parseLong(logModel.getDLData()));
        } catch (FieldNotFoundException e) {
        }
    }
}
