package com.chuangdata.userprofile.model;

import com.chuangdata.userprofile.utils.TimeConvertor;
import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import org.apache.hadoop.io.IntWritable;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luxiaofeng
 */
public class ExtractMd5EncrypterDomainKeyModel extends
        ExtractMd5EncrypterKeyModel {
    private static Logger LOG = Logger
            .getLogger(ExtractMd5EncrypterDomainKeyModel.class);
    private static SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:00:00");
    // 1 stand for not clicked ,1 clicked,2 is not recognise
    protected IntWritable clickedCode;
    private static Map<String, Integer> httpMethodMap = new HashMap<String, Integer>();

    static {
        httpMethodMap.put("connetc", 1);
        httpMethodMap.put("head", 2);
        httpMethodMap.put("put", 3);
        httpMethodMap.put("delete", 4);
        httpMethodMap.put("post", 5);
        httpMethodMap.put("get", 6);
        httpMethodMap.put("trace", 7);
        httpMethodMap.put("options", 8);
        httpMethodMap.put("propfind", 9);
        httpMethodMap.put("proppatch", 10);
        httpMethodMap.put("mkcol", 11);
        httpMethodMap.put("copy", 12);
        httpMethodMap.put("move", 13);
        httpMethodMap.put("lock", 14);
        httpMethodMap.put("unlock", 15);
        httpMethodMap.put("patch", 16);
        httpMethodMap.put("bpropfind", 17);
        httpMethodMap.put("bproppatch", 18);
        httpMethodMap.put("bcopy", 19);
        httpMethodMap.put("bdelete", 20);
        httpMethodMap.put("bmove", 21);
        httpMethodMap.put("notify", 22);
        httpMethodMap.put("poll", 23);
        httpMethodMap.put("search", 24);
        httpMethodMap.put("subscribe", 25);
        httpMethodMap.put("unsubscribe", 26);
        httpMethodMap.put("orderpatch", 27);
        httpMethodMap.put("acl", 28);
        httpMethodMap.put("update", 29);
        httpMethodMap.put("merge", 30);
    }


    public ExtractMd5EncrypterDomainKeyModel() {
        super();
        clickedCode = new IntWritable();
    }

    public IntWritable getIsClicked() {
        return clickedCode;
    }

    public void setIsClicked(IntWritable isClicked) {
        this.clickedCode = isClicked;
    }

    public void setIsClicked(int isClicked) {
        this.clickedCode.set(isClicked);
    }

    @Override
    public String toString() {
        super.toString();
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(DATE_HOUR_FORMAT)).append(SEPARATOR);
        builder.append(msisdn.toString()).append(SEPARATOR);
        builder.append(imei.toString()).append(SEPARATOR);
        builder.append(imsi.toString()).append(SEPARATOR);
        builder.append(userIp.toString()).append(SEPARATOR);
        builder.append(clickedCode.toString());
        return builder.toString();
    }

    public static ExtractMd5EncrypterDomainKeyModel buildEncrypterDomainKeyModel(
            LogModel logModel) {
        ExtractMd5EncrypterDomainKeyModel key = new ExtractMd5EncrypterDomainKeyModel();
        try {
            try {
                key.setTime(Long.parseLong(logModel.getProcedureEndTime()));
            } catch (NumberFormatException e) {
                try {
                    key.setTime(Double
                            .valueOf(
                                    Double.parseDouble(logModel
                                            .getProcedureEndTime()) * 1000)
                            .longValue());
                } catch (NumberFormatException ex) {
                    try {
                        key.setTime(TimeConvertor.convertTime(logModel
                                .getProcedureEndTime()));
                    } catch (ParseException pex) {
                        // DO NOTHING?
                    }
                }
            }
        } catch (FieldNotFoundException e) {
            // TODO time should not be null
        }
        try {
            key.setMsisdn(logModel.getMsisdn());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            key.setImei(logModel.getImei());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            key.setImsi(logModel.getImsi());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        try {
            key.setUserIp(logModel.getClientIP());
        } catch (FieldNotFoundException e) {
            // DO NOTHING
        }
        key.setIsClicked(getClickedCode(logModel));
        return key;
    }

    /**
     * reconstructed to java version by luxiaofeng
     * origin code -- hive script
     * <p>
     * 添加is_click字段，判断特定请求是否为点击行为
     * <p>
     * use data_research; drop table if exists hn_sw_newtype_tmp; create table
     * hn_sw_newtype_tmp as select clientip, timestamp, hostip, host, uri,
     * referer_url, useragent, cookie, title, keywords, content_type,
     * http_method, user_method, case when http_method==5 or http_method==3 then
     * '点击' when http_method==2 then '非点击' when lower(content_type) rlike
     * '.*(text/htm).*' or (content_type=='' and lower(uri) rlike
     * '.*(\\.s?htm).*') or (content_type=='' and host!='' and uri='/') then
     * '点击' when (lower(content_type) like '%text/%' and not lower(content_type)
     * like '%htm%') or (lower(content_type) like '%application/%' and
     * lower(content_type) like '%javascript%') or (lower(content_type) rlike
     * '.*(image/|video/|audio/).*') or (content_type=='' and not lower(uri)
     * like '%.json%' and lower(uri) rlike '.*\\.(php|js|jsp|asp|cgi).*') or
     * (content_type=='' and lower(uri) rlike
     * '.*\\.(jpg|png|gif|ico|jpeg|mp4|rmvb|mp3|wmv|css|xml).*') then '非点击' else
     * '不确定' end as is_click, log_day, province, isp, collect_env from ( select
     * * from hn_sw_http_day_newtype where log_day='2016-01-06' and
     * province='hn' and isp='cm' and collect_env='sw' limit 100000 ) tmp;
     */
    /*
	 * 
	 * @param logModel
	 * 
	 * @return
	 */
    private static int getClickedCode(LogModel logModel) {
        int isclicked = 2;
        int httpMethodCode = -1;
        try {
            httpMethodCode = Integer.valueOf(logModel.getTransactionType());
        } catch (Exception e) {
            // if content is str, to get value from httpmethod map
            try {
                String method = logModel
                        .getTransactionType().toLowerCase();
                if (method != null) {
                    httpMethodCode = httpMethodMap.get(method);
                }
            } catch (FieldNotFoundException e1) {
                LOG.warn(e1.toString());
            }


        }

        try {
            String contendType = logModel.getHttpContentType();
            String host = logModel.getHost();
            String uri = logModel.getUri();

            if (httpMethodCode == 3 || httpMethodCode == 5) {
                isclicked = 1;// clicked
                return isclicked;
            }
            if (httpMethodCode == 2) {
                isclicked = 0; // not clicked
                return isclicked;
            }

            if (contendType == " " && host != " " && uri == "/") {
                isclicked = 1;// clicked;
                return isclicked;
            }
            if ((contendType.contains("text/") && !contendType.contains("htm"))
                    || contendType.contains("application/")
                    && contendType.contains("javascript")) {
                isclicked = 0; // noclick
                return isclicked;
            }

            Pattern ckP1 = Pattern.compile(".*(text/htm).*");
            Matcher ckM1 = ckP1.matcher(contendType.toLowerCase());

            Pattern ckP2 = Pattern.compile(".*(\\.s?htm).*");
            Matcher ckM2 = ckP2.matcher(uri.toLowerCase());
            if ((contendType == " " && ckM1.find())
                    || (contendType == " " && host != " " && ckM2.find())) {
                isclicked = 1;// clicked
                return isclicked;
            }

            Pattern nckP1 = Pattern.compile(".*(image/|video/|audio/).*");
            Matcher nckM1 = nckP1.matcher(contendType.toLowerCase());

            Pattern nckP2 = Pattern.compile(".*\\.(php|js|jsp|asp|cgi).*");
            Matcher nckM2 = nckP2.matcher(uri.toLowerCase());

            Pattern nckP3 = Pattern
                    .compile(".*\\.(jpg|png|gif|ico|jpeg|mp4|rmvb|mp3|wmv|css|xml).*");
            Matcher nckM3 = nckP3.matcher(uri.toLowerCase());

            if (nckM1.find()
                    || ((contendType == " " && !uri.contains(".json") && nckM2
                    .find()) || (contendType == " " && nckM3.find()))) {
                isclicked = 0; // noclick
                return isclicked;
            }

        } catch (Exception e) {
            LOG.warn(e.toString());
        }

        return isclicked;

    }

}
