package com.chuangdata.userprofile.bj.job;


import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.job.BaseMapper;
import com.chuangdata.userprofile.job.KeyModel;
import com.chuangdata.userprofile.job.Metrics;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.apache.hadoop.hive.ql.io.orc.OrcStruct;
import org.apache.hadoop.conf.Configuration;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author luxiaofeng
 */
public class UserAppMapper<K extends UserAppKeyModel> extends BaseMapper<UserAppKeyModel> {
    private static final Logger LOG = Logger.getLogger(UserAppMapper.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

    @Override
    public void map(Object key, OrcStruct value, Context context) {
        try {

            String SCHEMA = "struct<imsi:string,imei:string,msisdn:string,begin_time:string,end_time:string,prot_category:string,prot_type:string,host:string," +
                    "fst_uri:string,user_agent:string,server_ip:string,server_port:string,l4_ul_throughput:float,l4_dw_throughput:float,eci:string,reportdate:string,hour:int>";
            OrcStruct struct = (OrcStruct)value;
            TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(SCHEMA);
            StructObjectInspector inspector = (StructObjectInspector)OrcStruct.createObjectInspector(typeInfo);

            String log = this.getLog(new String[]{"imsi", "imei", "msisdn", "begin_time", "end_time", "prot_category", "prot_type","host", "fst_uri", "user_agent", "server_ip", "server_port", "l4_ul_throughput", "l4_dw_throughput", "eci"},inspector,struct);

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
                context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_DOMAIN).increment(1);
                return;
            }
            // 3. calculate QoS metrics
            Metrics metricsModel = new Metrics();
            buildValue(logModel, metricsModel);

//            Configuration configuration = context.getConfiguration();
//            Long index=Long.parseLong(configuration.get("chuangdata.dmu.userprofile.logIndex"));
//            if(index<10) {
////                LOG.error("orc记录：" + value.toString());
//                LOG.error("log记录：" + metricsModel.toString("^"));
//                index++;
//                configuration.set("chuangdata.dmu.userprofile.logIndex", index.toString());
//            }

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


    protected void buildKey(LogModel logModel, KeyModel keyModel) {
        try {
            keyModel.setUserId(logModel.getMeid());
        } catch (FieldNotFoundException e) {
        }
        try {
            DomainUnit unit = urlMatcher.getDomain(getHost(logModel), getUri(logModel));
            //changed group by domainid+host, instead of domainid, luxiaofeng 2016-12-15
            if(unit != null){
                keyModel.setAppId(unit.getId());
                keyModel.setHost(getHost(logModel));
                String tmpS = logModel.getProcedureEndTime();
                String dFmt = tmpS.substring(0,4)+"-"+tmpS.substring(4,6)+"-"+tmpS.substring(6,8)
                        +" "+tmpS.substring(8,10)+":00:00";
                Date tmpD = sdf.parse(dFmt);
                keyModel.setTimeStamp(tmpD.getTime());
            }else{
                keyModel.setAppId(-1);
                keyModel.setHost("");
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
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
                if (!Strings.isNotEmpty(getUri(logModel))) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1);
                    return true;
                }
                if (urlMatcher.getDomain(host, getUri(logModel)) == null) {
                    context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_APP).increment(1);
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

    protected boolean filterByApp(KeyModel keyModel) {
        return keyModel.getAppId().get() < 0;
    }

    protected KeyModel buildKeyModel() {
        return new UserAppKeyModel();
    }
}
