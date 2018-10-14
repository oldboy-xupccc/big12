package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.resource.Utils;
import com.chuangdata.userprofile.utils.Strings;
import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamMatcher implements MatcherInitializer {
    private static final Logger LOG = Logger.getLogger(ParamMatcher.class);
    //private Map<Integer, Map<Pattern, ParamUnit>> patternParamMap;
    private Map<Integer, Map<String, ParamUnit>> actionParamMap;
    private Map<Integer, ParamUnit> actionParamMapById;
    private Map<Integer, Set<Integer>> paramTypeMap;
    private static final int PARAM_TYPE_133 = 133;
    private static final int PARAM_TYPE_93 = 93;

    @VisibleForTesting
    protected Map getActionParamMap() {
        return actionParamMap;
    }

    @VisibleForTesting
    protected void setActionParamMap(
            Map<Integer, Map<String, ParamUnit>> actionParamMap) {
        this.actionParamMap = actionParamMap;
    }

    @VisibleForTesting
    protected ParamMatcher() {
    }

    public ParamMatcher(String paramConfigFile, boolean isEncrypted)
            throws IOException {
        LOG.info("Initializing paramMatcher. Config file is " + paramConfigFile);
        actionParamMap = new HashMap<Integer, Map<String, ParamUnit>>();
        //patternParamMap = new HashMap<Integer, Map<Pattern, ParamUnit>>();
        actionParamMapById = new HashMap<Integer, ParamUnit>();
        paramTypeMap = new HashMap<Integer, Set<Integer>>();
        ConfigReader.read(paramConfigFile, this, isEncrypted);
    }

    public void init(String[] args) {
        if (args != null && args.length == 6) {
            try{
            Integer id = Integer.parseInt(args[0]);
            Integer actionId = Integer.parseInt(args[1]);
            String param = args[2];
            //Pattern paramPattern = Pattern.compile(param);
            Integer paramTypeId = Integer.parseInt(args[3]);
            Boolean isUserId = Integer.parseInt(args[4]) == 1;
            Boolean isSpecial = false;
            if (null != args[5] && args[5].equalsIgnoreCase("t")) {
                isSpecial = true;
            }

            Map<String, ParamUnit> subMap = actionParamMap.get(actionId);
            if (subMap == null) {
                subMap = new LinkedHashMap<String, ParamUnit>();
            }
            //特殊参数，使用正则表达式匹配
//            Map<Pattern, ParamUnit> subPatternMap = patternParamMap.get(actionId);
//            if (subPatternMap == null) {
//                subPatternMap = new LinkedHashMap<Pattern, ParamUnit>();
//            }

            ParamUnit unit = new ParamUnit(id, actionId, param, null, paramTypeId,
                    false, isUserId, isSpecial);

//            if (param.endsWith("=")) {
//                unit = new ParamUnit(id, actionId, param, null, paramTypeId,
//                        false, isUserId, isSpecial);
//            } else if (param.endsWith(":")) {
//                unit = new ParamUnit(id, actionId, param, null, paramTypeId,
//                        false, isUserId, isSpecial);
//            } else {
//                unit = new ParamUnit(id, actionId, param, null, paramTypeId,
//                        true, isUserId, isSpecial);
//            }

            if (unit == null)
                return;
            subMap.put(param, unit);
            //subPatternMap.put(paramPattern, unit);
            // paramValue would be set for each data line
            actionParamMap.put(actionId, subMap);
            //patternParamMap.put(actionId, subPatternMap);
            actionParamMapById.put(id, unit);

            if (paramTypeId == PARAM_TYPE_133 || paramTypeId == PARAM_TYPE_93) {
                HashSet<Integer> paraTypeActionSet = (HashSet) paramTypeMap
                        .get(paramTypeId);
                if (null == paraTypeActionSet) {
                    paraTypeActionSet = new HashSet<Integer>();
                    paramTypeMap.put(paramTypeId, paraTypeActionSet);
                }
                if (!paraTypeActionSet.contains(actionId)) {
                    paraTypeActionSet.add(actionId);
                }
            }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public ParamUnit getParam(int paramId) {
        return actionParamMapById.get(paramId);
    }

    public Set<Integer> getActionIdSet(int actionTypeId) {
        return paramTypeMap.get(actionTypeId);
    }


    public List<ParamUnit> getParam(long actionId, LogModel logModel) {

        List<ParamUnit> result = new ArrayList<ParamUnit>();
        Map<String, ParamUnit> paramMap = actionParamMap.get((int) actionId);
        //Map<Pattern, ParamUnit> patternMap = patternParamMap.get((int) actionId);
        // action
        // id
        // is
        // long,
        // but
        // the
        // key
        // of
        // actionParamMap
        // is
        // integer
        if (paramMap != null) {

            try {
                String uri = getUri(logModel);
                String host = getHost(logModel);
                int paramStartIndex = uri.indexOf("?");
                String paramString = uri.substring(paramStartIndex + 1);
                paramString = URLDecoder.decode(paramString, "UTF-8"); // not sure whether it's always UTF-8
                String url = URLDecoder.decode(host+uri, "UTF-8");

                for (ParamUnit paramUnit : paramMap.values()) {

                    if (paramUnit.isSpecial()) {
                        //特殊参数处理
                        Pattern tmp = Pattern.compile(paramUnit.getParam());
                        Matcher m = tmp.matcher(url);
                        if (m.find(1)) {
                            ParamUnit newUnit = new ParamUnit(
                                    paramUnit.getId(),
                                    paramUnit.getActionId(),
                                    paramUnit.getParam(),
                                    //Utils.replaceSeparator(m.group(1)),
                                    getUrlDecode(Utils.replaceSeparator(m.group(1)).toUpperCase()),
                                    paramUnit.getParamTypeId(),
                                    paramUnit.isJsonType(),
                                    paramUnit.isUserId(),
                                    paramUnit.isSpecial());
                            if (!filterOut(newUnit)) {
                                result.add(newUnit);
                            }

                        }

                    } else {
                        //正常参数处理
                        if (paramUnit.isJsonType()) {
                            // if is json, then should be like "paramName":
                            int start_index = paramString.indexOf(paramUnit
                                    .getParam());
                            if (start_index <= 0) {
                                continue; // start_index should not be zero
                            }
                            // check whether surrounded by ""
                            if (paramString.charAt(start_index - 1) != '\"'
                                    || paramString.charAt(start_index
                                    + paramUnit.getParam().length()) != '\"') {
                                continue;
                            }
                            start_index = start_index
                                    + paramUnit.getParam().length() + 1;
                            // check whether there is a ':' after it
                            if (paramString.charAt(start_index) != ':') {
                                continue;
                            }
                            start_index++;
                            int end_index = -1;
                            int end_index1 = paramString.indexOf(",",
                                    start_index);
                            int end_index2 = paramString.indexOf("}",
                                    start_index);
                            if (end_index1 < 0 && end_index2 < 0) {
                                continue;
                            } else if (end_index1 < 0 && end_index2 > 0) {
                                end_index = end_index2;
                            } else if (end_index2 < 0 && end_index1 > 0) {
                                end_index = end_index2;
                            } else {
                                end_index = end_index1 > end_index2 ? end_index2
                                        : end_index1; // min
                            }
                            if (end_index <= 0 || end_index <= start_index) {
                                continue;
                            }
                            String value = paramString.substring(start_index,
                                    end_index);
                            if (value.startsWith("\"") && value.endsWith("\"")) {
                                value = value.substring(1, value.length() - 1);
                            }
                            if (!value.isEmpty()) {
                                result.add(new ParamUnit(paramUnit.getId(),
                                        paramUnit.getActionId(), paramUnit
                                        .getParam(), Utils
                                        .replaceSeparator(value),
                                        paramUnit.getParamTypeId(), paramUnit
                                        .isJsonType(), paramUnit
                                        .isUserId(),
                                        paramUnit.isSpecial()));
                            }

                        } else {
                            // & and = separate
                            int start_index = paramString.indexOf(paramUnit
                                    .getParam());
                            if (start_index >= 0) { // start index can be zero
                                String value = null;
                                start_index += paramUnit.getParam().length();
                                // paramString[start_index] can be "&" if the
                                // value is empty
                                // so we should get index of "&" from
                                // start_index - 1)
                                int end_index = paramString.indexOf("&",
                                        start_index - 1);
                                if (end_index < 0) {
                                    // last param?
                                    value = paramString.substring(start_index);
                                } else if (end_index > start_index) {
                                    // matches
                                    value = paramString.substring(start_index,
                                            end_index);
                                }
                                // if (value != null && !value.isEmpty()) {
                                // result.add(new ParamUnit(paramUnit.getId(),
                                // paramUnit.getActionId(),
                                // paramUnit.getParam(), value,
                                // paramUnit.getParamTypeId(),
                                // paramUnit.isJsonType(),
                                // paramUnit.isUserId()));
                                // }
                                ParamUnit newUnit = new ParamUnit(
                                        paramUnit.getId(),
                                        paramUnit.getActionId(),
                                        paramUnit.getParam(),
                                        getUrlDecode(Utils.replaceSeparator(value).toUpperCase()),
                                        paramUnit.getParamTypeId(),
                                        paramUnit.isJsonType(),
                                        paramUnit.isUserId(),
                                        paramUnit.isSpecial());
                                if (!filterOut(newUnit)) {
                                    result.add(newUnit);
                                }
                            }
                        }
                    }

                }//正常处理结束

            } catch (Exception e) {
                // TODO log something
            }
        }
        return result.isEmpty() ? null : result;
    }

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
                String h = uri.substring(0, index);
                if (Strings.isNotEmpty(h) && h.indexOf(":") > 0) {
                    // if uri contains port
                    return h.substring(0, h.indexOf(":"));
                }
                return h;
            }
        }
        return logModel.getHost();
    }

    protected String getUri(LogModel logModel) throws FieldNotFoundException {
        // 若host不为空，但uri为空，则设为/
        if (Strings.isNotEmpty(getHost(logModel)) && !Strings.isNotEmpty(logModel.getUri())) {
            return "/";
        } else if (Strings.isNotEmpty(getHost(logModel)) && Strings.isNotEmpty(logModel.getUri())) {
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

    private boolean filterOut(ParamUnit paramUnit) {
        if (paramUnit.getParamValue() == null
                || paramUnit.getParamValue().isEmpty()) {
            return true;
        }
        // some hard code here
        // 1. houseid for fang.com should be long
        if (paramUnit.getParamTypeId() == 57
                && paramUnit.getParam().equals("houseid=")) { // fang.com
            // houseid
            try {
                // houseid should be long
                Long.parseLong(paramUnit.getParamValue());
            } catch (NumberFormatException e) {
                return true;
            }
        }
        // 2. 经纬度必须为数字
        if (paramUnit.getParamTypeId() == 28
                || paramUnit.getParamTypeId() == 29) { // 纬度, 经度, 只有数字类型的才有意义
            try {
                Double.parseDouble(paramUnit.getParamValue());
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return actionParamMap.size();
    }

    private boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    private String getUrlDecode(String Keywords) {
        //It just decode string for the string that contains "%" -- add by luxiaofeng
        if(Keywords.indexOf("%") < 0){
            return Keywords;
        }

        String chinese = null;
        String tmp = null;
        String[] codeFormat = {"utf-8", "gb18030", "unicode", "big5", "gbk", "gb2312", "utf-16", "utf-32"};

        for (String code : codeFormat) {
            String letters = Keywords;
            try {
                chinese = URLDecoder.decode(letters, code);
            } catch (Exception e) {

                try {
                    letters = letters.substring(0, letters.length() - 1);
                    chinese = URLDecoder.decode(letters, code);
                } catch (Exception e1) {

                    try {
                        letters = letters.substring(0, letters.length() - 1);
                        chinese = URLDecoder.decode(letters, code);
                    } catch (Exception e2) {
                    }
                }
            }

if(chinese!=null)
            //鉴别需要两次解码的情况
            if (chinese.contains("%")) {
                //utf-8编码汉子由三组%ww组合而成，若单字长度不为3的倍数，则抛出异常，再次进行截取处理；
                try {
                    chinese = URLDecoder.decode(chinese, code);
                } catch (Exception e) {

                    try {
                        chinese = chinese.substring(0, chinese.length() - 1);
                        chinese = URLDecoder.decode(chinese, code);
                    } catch (Exception e1) {

                        try {
                            chinese = chinese.substring(0, chinese.length() - 1);
                            chinese = URLDecoder.decode(chinese, code);
                        } catch (Exception e2) {
                        }
                    }
                }
            }

            //锟叫断凤拷锟斤拷锟斤拷锟角凤拷锟斤拷确
            tmp = chinese;
            int len = tmp.length();
            //乱码取舍方法1：去除所有关键字中除结尾外仍包含乱码的情况
            //避免关键字中正常包含$的记录被删除
            tmp.replace("$", "");
            //若最后一位单字为乱码，则删除
            if (len > 1) {
                if (isMessyCode(tmp.substring(len - 1, len)))
                    tmp = tmp.substring(0, len - 1);
            }
            //若结果不为乱码，则翻译正确，跳出循环
            if (!isMessyCode(tmp))
                break;

            //循环结束之后仍为乱码的则置为空；
            chinese = "";
            //乱码取舍方法2：保留关键字为乱码但结尾不为乱码的情况
            //锟斤拷锟斤拷锟揭晃伙拷锟斤拷锟轿拷锟斤拷耄拷锟缴撅拷锟�
                        /*if(len!=0){
                            while(ChineseUtil.isMessyCode(tmp.substring(len-1, len))){
								tmp = tmp.substring(0, len-1);
								len--;
								if(len==0)
									break;
							}
						}
						//避免输出乱码
						if(tmp.length()==0)
							chinese="";
						//锟斤拷锟斤拷锟轿拷锟斤拷耄拷锟斤拷锟斤拷锟饺凤拷锟斤拷锟斤拷循锟斤拷
						if(tmp.length()>0)
							break;*/

        }
        return chinese;

    }
}
