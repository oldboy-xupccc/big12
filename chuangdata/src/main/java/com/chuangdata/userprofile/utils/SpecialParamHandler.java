package com.chuangdata.userprofile.utils;

import com.chuangdata.userprofile.model.ParamMapModel;
import com.chuangdata.userprofile.model.ParamModel;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class SpecialParamHandler {
    private static final Logger LOG = Logger
            .getLogger(SpecialParamHandler.class);

    public static boolean paramHandle(int actionId, String uri,
                                      ParamMapModel paramMap) {
        boolean isSpecialParam = false;
        String paraStr = null;
        try {
            paraStr = URLDecoder.decode(uri, "UTF-8");

            switch (actionId) {
                case 14383:
                case 14379:
                case 14385:
                case 14384:
                case 14381:
                case 14386:
                case 14380:
                case 14378:
                case 14382:
                case 14553:
                    // 易车处理，取/后第二个字段
                    int startIndex = paraStr.indexOf("/", 1);
                    int endIndex = paraStr.indexOf("/", startIndex + 1);
                    if (endIndex > startIndex) {
                        String paramValue = paraStr.substring(startIndex + 1, endIndex);
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(47);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车关键字搜索  action id" + actionId + " param value is "
                                + paramValue + " uri is" + uri + " param type is" + 47);
                    }
                    isSpecialParam = true;
                    break;
                // param type 48
                case 14510:
                case 14650:
                case 14575: {
                    String paramValue = null;
                    // find if have character p , then to get the number after
                    // price
                    // 14650
                    int indexP = paraStr.indexOf("p");
                    String value = "";
                    if (indexP != -1) {
                        for (int i = indexP + 1; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }
                    }
                    if (!value.equals("")) {
                        paramValue = value;
                    }
                    if (paramValue == null) {
                        startIndex = paraStr.indexOf("/", 1);
                        endIndex = paraStr.indexOf("/", startIndex + 1);

                        if (endIndex > startIndex) {
                            String str = paraStr.substring(startIndex + 1, endIndex);
                            String[] params = str.split("-");
                            if (params.length > 2) {
                                try {// 14510
                                    // if is num，get
                                    int price = Integer.valueOf(params[0]);
                                    paramValue = String.valueOf(price);

                                } catch (NumberFormatException e) {
                                    LOG.warn(e.toString());
                                }
                            }
                        }
                    }
                    if (paramValue != null) {
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(48);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车价格 action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 48);
                    }
                }
                isSpecialParam = true;
                break;
                // 第三个参数，p的数字
                case 14422:
                    String[] strArr = paraStr.split("/");
                    if (strArr.length >= 3) {
                        String paramValue = null;
                        int indexP = strArr[2].indexOf("p");
                        String value = "";
                        if (indexP != -1) {
                            for (int i = indexP + 1; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);

                                } else {
                                    break;
                                }

                            }
                        }
                        if (!value.equals("")) {
                            paramValue = value;
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(48);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车价格 action id" + actionId + " param value is"
                                    + paramValue + " uri is" + uri + " param type is"
                                    + 48);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // “/”第三个参数,再取"-"分隔的第二个参数
                case 14606:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String[] params = strArr[2].split("-");
                        String paramValue = params[1];
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(params[1]);
                        param.setParamTypeId(48);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车国家action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 48);
                    }
                    isSpecialParam = true;
                    break;

                case 11218:
                    // 搜狐汽车
                    strArr = paraStr.split("/");
                    if (strArr.length >= 3) {
                        String[] params = strArr[3].split("-");
                        try {
                            Integer.valueOf(params[2]);
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(params[2]);
                            param.setParamTypeId(95);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车国家action id" + actionId + " param value is"
                                    + params[2] + " uri is" + uri + " param type is"
                                    + 95);
                        } catch (NumberFormatException e) {
                            LOG.warn("the action id is " + actionId
                                    + " and the value want to get is" + params[2]);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // 参数类型115，第二个参数，b后面的数字为汽车品牌
                case 9442:
                case 9445:
                case 14647:
                case 10464:
                case 9540:
                case 14420:
                case 14571:
                case 14572:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String paramValue = null;
                        int indexB = strArr[2].indexOf("b");
                        String value = "";
                        if (indexB != -1) {
                            for (int i = indexB + 1; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }

                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(115);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 115);
                    }
                    isSpecialParam = true;
                    break;
                // 第二个参数全取
                case 14529:
                case 14601:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String paramValue = strArr[2];
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(115);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 115);
                    }
                    isSpecialParam = true;
                    break;
                case 9350:
                case 14448:
                    // 第二个参数，brand-后面的数字
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String paramValue = null;
                        int indexB = strArr[2].indexOf("brand-");
                        String value = "";
                        if (indexB != -1) {
                            for (int i = indexB + 6; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }

                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(115);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 115);
                    }
                    isSpecialParam = true;
                    break;
                case 14494:
                case 14028:
                    // 第二个参数
                    strArr = paraStr.split("/");

                    if (strArr.length > 2) {
                        String paramValue = null;
                        String[] params = strArr[2].split("_");
                        if (params != null && params.length == 2) {
                            // 取第一个数字
                            paramValue = params[0];
                        } else {
                            // brand- 后数字
                            int indexB = strArr[2].indexOf("brand-");
                            String value = "";
                            if (indexB != -1) {
                                for (int i = indexB + 6; i < strArr[2].length(); i++) {
                                    if (Character.isDigit(strArr[2].charAt(i))) {
                                        value += strArr[2].charAt(i);
                                    } else {
                                        break;
                                    }

                                }

                                if (!value.equals("")) {
                                    paramValue = value;
                                }
                            }

                            if (paramValue == null) {
                                // 取第一个数字
                                indexB = strArr[2].indexOf("p");
                                value = "";
                                if (indexB != -1) {
                                    for (int i = indexB + 1; i < strArr[1].length(); i++) {
                                        if (Character.isDigit(strArr[1].charAt(i))) {
                                            value += strArr[1].charAt(i);
                                        } else {
                                            break;
                                        }

                                    }
                                    if (!value.equals("")) {
                                        paramValue = value;
                                    }
                                }
                            }
                        }
                        if (paramValue != null) {
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(115);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 115);
                        }

                    }
                    isSpecialParam = true;
                    break;
                case 14070: {
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String paramValue = null;
                        // 取第一个数字
                        int indexB = strArr[2].indexOf("p");
                        String value = "";
                        if (indexB != -1) {
                            for (int i = indexB + 1; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);
                                } else {
                                    break;
                                }

                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }
                        if (paramValue != null) {
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(115);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 115);
                        }
                    }
                }
                isSpecialParam = true;
                break;
                // 第三个参数，nb后面
                case 14013:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        int indexNb = strArr[3].indexOf("nb");
                        String value = "";
                        String paramValue = null;

                        if (indexNb != -1) {
                            for (int i = indexNb + 2; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(115);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 115);
                    }
                    isSpecialParam = true;
                    break;
                // param type 116, host 后第一个参数，数字或数字字母组合

                case 14496:
                case 14632:
                case 14463:
                case 14619:
                case 14498:
                case 14503:
                case 14506:
                case 14497:
                case 14505:
                case 14499:
                case 14502:
                case 14504:
                case 14501:
                case 14500:
                case 14634:
                case 14633:
                case 14641:
                case 14640:
                case 14642:
                case 14639:
                case 14635:
                case 14636:
                case 14638:
                case 14365:
                case 14368:
                case 14367:
                case 14366:
                case 14369:
                case 14361:
                case 14363:
                case 14372:
                case 14364:
                case 14360:
                case 14362:
                case 14346:
                case 14347:
                case 14348:
                case 14351:
                case 14350:
                case 14353:
                case 14349:
                case 14352:
                case 14540:
                case 14541:
                case 14539:
                case 14542:
                case 14544:
                case 14533:
                case 14534:
                case 14531:
                case 14535:
                case 14528:
                case 14483:
                case 14473:
                case 14474:
                case 14475:
                case 14480:
                case 14479:
                case 14478:
                case 14481:
                case 14476:
                case 14477:
                case 14482:
                case 14617:
                case 14622:
                case 14629:
                case 14620:
                case 14621:
                case 14124:
                case 14125:
                case 14126:

                    strArr = paraStr.split("/");
                    if (strArr.length > 1) {
                        String paramValue = strArr[1];
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("车系" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);
                    }
                    isSpecialParam = true;
                    break;

                // sg后面的数字
                case 14435:
                case 14576:
                case 14427:
                case 14403:
                case 14432:
                case 14430:
                case 14407:
                case 14425:
                case 14416:
                case 14433:
                case 14426:
                case 14402:
                case 14423:
                case 14431:
                case 14428:
                case 14424:
                case 14429:
                case 14434:
                case 14580:
                case 14568:
                case 14581:
                case 14559:
                case 14585:
                case 14578:
                case 14573:
                case 14566:
                case 14561:
                case 14558:
                case 14584:
                case 14582:
                case 14577:
                case 14579:
                case 14586:
                case 14569:
                case 14008:
                case 14010:
                case 14011:
                case 14012:
                case 14015:
                case 14093:
                case 14094:
                case 14095:
                case 14096:
                case 14097:
                case 14111: {
                    int indexNb = paraStr.indexOf("sg");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 2; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                // series 紧跟数字
                case 14520: {
                    int indexNb = paraStr.indexOf("serise");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 6; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }

                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                case 14610: {
                    int indexNb = paraStr.indexOf("series");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 6; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }

                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                case 14516:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        int indexNb = strArr[2].indexOf("ps");
                        String value = "";
                        String paramValue = null;

                        if (indexNb != -1) {
                            for (int i = indexNb + 2; i < strArr[2].length(); i++) {
                                if (Character.isDigit(strArr[2].charAt(i))) {
                                    value += strArr[2].charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }

                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);
                    }
                    isSpecialParam = true;
                    break;
                // ”/“，第3个字段的内容，数字或字母加数字
                case 14651:
                case 14643:
                case 14376:
                case 14546:
                case 14599:
                case 14162:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String paramValue = strArr[2];
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);
                    }
                    isSpecialParam = true;
                    break;
                // nb后的数字
                case 14374:
                case 14547:
                case 14549: {
                    int indexNb = paraStr.indexOf("nb");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 2; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                case 14419: {
                    // sid后的数字，可能多组
                    int indexSid = paraStr.indexOf("sid");
                    String value = "";
                    String paramValue = null;
                    for (int i = indexSid + 4; i < paraStr.length(); i++) {
                        if (Character.isDigit(paraStr.charAt(i))) {
                            value += paraStr.charAt(i);
                        } else if (paraStr.charAt(i) == '-' || paraStr.charAt(i) == '.') {
                            if (value != null && !value.equals("")) {
                                paramValue = value;
                                value = "";
                                if (Integer.valueOf(paramValue) > 0) {
                                    ParamModel param = new ParamModel();
                                    param.setActionId(actionId);
                                    param.setParamValue(paramValue);
                                    param.setParamTypeId(116);
                                    param.setLogCount(1L);
                                    paramMap.addParam(param);
                                    LOG.info("汽车品牌" + "action id" + actionId
                                            + " param value is" + paramValue
                                            + " uri is" + uri + " param type is" + 116);
                                }
                            }
                        } else {
                            break;
                        }

                    }
                }
                isSpecialParam = true;
                break;
                // -后的前缀数字
                case 14389:
                case 14388:
                case 14391:
                case 14457:
                case 14612:
                case 14587:
                case 14588:
                case 14591: {
                    int index = paraStr.indexOf("-");
                    String value = "";
                    String paramValue = null;
                    if (index != -1) {
                        for (int i = index + 1; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);
                    }
                }
                isSpecialParam = true;
                break;
                // “/”第三个参数,再取"-"分隔的第二个参数前缀数字
                case 14451:
                case 14139:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String[] params = strArr[2].split("-");
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < params[1].length(); i++) {
                            if (Character.isDigit(params[1].charAt(i))) {
                                value += params[1].charAt(i);
                            } else {
                                break;
                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }

                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(params[1]);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);
                    }
                    isSpecialParam = true;
                    break;

                // “/”第三个参数,再取"-"分隔的第3个参数前缀数字
                case 14462:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String[] params = strArr[3].split("-");
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < params[1].length(); i++) {
                            if (Character.isDigit(params[1].charAt(i))) {
                                value += params[1].charAt(i);
                            } else {
                                break;
                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }

                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(params[1]);
                        param.setParamTypeId(48);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车国家action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 48);
                    }
                    isSpecialParam = true;
                    break;
                // ”/“后面 _分隔,第2个数字
                case 14614:
                case 14613:
                    strArr = paraStr.split("/");
                    if (strArr.length > 1) {
                        if (strArr[1].split("_").length > 1) {
                            String paramValue = strArr[1].split("_")[2];
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;

                // 第3个字段，”_“,第二部分数字前缀
                case 14456:
                case 14455:
                case 14454:
                case 13987:
                case 13989:
                case 14032:
                case 14035:
                case 14036:
                case 14158:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        if (strArr[2].split("_").length > 1) {
                            String str = strArr[2].split("_")[1];
                            String value = "";
                            String paramValue = null;
                            for (int i = 0; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }
                                if (!value.equals("")) {
                                    paramValue = value;
                                }
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;

                // 第二个参数，数字前缀
                case 14492:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String str = strArr[2];
                        String value = "";
                        String paramValue = null;

                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else {
                                break;
                            }

                        }
                        if (!value.equals("")) {
                            paramValue = value;
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);

                    }
                    isSpecialParam = true;
                    break;
                // 第三个参数，前缀数字
                case 14602:
                case 14605:
                case 14443:
                case 14450:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else {
                                break;
                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(116);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 116);

                    }
                    isSpecialParam = true;
                    break;

                case 14452:
                case 14607:
                case 14608:
                    // 第二个参数，-分隔符，第三个
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        if (strArr[2].split("-").length > 3) {
                            String paramValue = strArr[2].split("-")[2];
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;
                case 14616:
                    // 第三个参数，-分隔符，第二个前缀数字
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        if (strArr[3].split("-").length > 1) {
                            String str = strArr[3].split("-")[1];
                            String value = "";
                            String paramValue = null;
                            for (int i = 0; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }
                                if (!value.equals("")) {
                                    paramValue = value;
                                }
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;
                case 14611:
                    // 第4个参数，_分隔，第二个数字
                    strArr = paraStr.split("/");

                    if (strArr.length > 3) {
                        if (strArr[3].split("_").length > 2) {
                            String paramValue = strArr[3].split("_")[1];
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // ss字符串后缀
                case 14742:
                case 14734:
                case 14729:
                case 14730:
                case 14736:
                case 14725:
                case 14726:
                case 14727:
                case 14728:
                case 14279:
                case 14280:
                case 14289: {
                    int indexNb = paraStr.indexOf("ss");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 2; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }

                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                // 第三个参数，-，第三部分，数字前缀

                case 14738:
                    // 第三个参数，-分隔符，第二个后缀数字
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        if (strArr[2].split("-").length > 3) {
                            String str = strArr[2].split("-")[3];
                            String value = null;
                            String paramValue = null;
                            for (int i = 1; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                }

                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;
                // model后面紧跟数字
                case 14004: {
                    int indexNb = paraStr.indexOf("model");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 5; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(116);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 116);
                }
                isSpecialParam = true;
                break;
                // ”/“分隔，第3个字符串，"_"分隔，第三部分字符串，s紧跟的数字

                case 14038:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        if (strArr[2].split("_").length > 3) {
                            String str = strArr[2].split("_")[2];
                            String value = "";
                            String paramValue = null;
                            for (int i = 1; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                }

                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;
                case 14068:
                    // ”/“分隔，第二个字符串，"_"分隔，第1部分字符串，s紧跟的数字
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String str = strArr[2];
                        String value = "";
                        String paramValue = null;
                        for (int i = 1; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;

                case 14099:
                    // ”/“分隔，第3个字符串，"_"分隔，第1部分字符串，s紧跟的数字,可能存在多组
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3].substring(1);
                        String[] params = str.split("-");
                        for (String paramValue : params) {
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // "/"分隔第三部分，”-“分隔，第二个字符串，tsg后缀
                case 14109:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        if (strArr[3].split("-").length > 2) {
                            String str = strArr[3].split("-")[1];
                            String value = "";
                            String paramValue = null;
                            for (int i = 3; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                }
                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;
                // "/"分隔第三部分，”-“分隔，第3个字符串，取数字前缀
                case 14163:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        if (strArr[2].split("-").length > 2) {
                            String str = strArr[2].split("-")[2];
                            String value = "";
                            String paramValue = null;
                            for (int i = 0; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }

                            }
                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);

                        }
                    }
                    isSpecialParam = true;
                    break;
                // "/"分隔第三部分，”_“分隔，第1个字符串数字
                case 14213:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else if (str.charAt(i) == '_' || str.charAt(i) == '.') {
                                if (!value.equals("") && Integer.valueOf(value) > 0) {
                                    paramValue = value;
                                    value = "";
                                    ParamModel param = new ParamModel();
                                    param.setActionId(actionId);
                                    param.setParamValue(paramValue);
                                    param.setParamTypeId(116);
                                    param.setLogCount(1L);
                                    paramMap.addParam(param);
                                    LOG.info("汽车品牌" + "action id" + actionId
                                            + " param value is" + paramValue
                                            + " uri is" + uri + " param type is" + 116);
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    isSpecialParam = true;
                    break;

                // 参数类型118，host后面的字母代表车系
                case 14472:
                    strArr = paraStr.split("/");
                    if (strArr.length > 1) {
                        String paramValue = strArr[1];
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(118);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车级别" + "action id" + actionId + " param value is "
                                + paramValue + " uri is" + uri + " param type is" + 118);
                    }
                    isSpecialParam = true;
                    break;
                // 车型，m字母后面的数字
                case 14515:
                case 14637:
                case 14357:
                case 14532:
                case 14417:
                case 14583:
                case 14395:
                case 14562:
                case 14090:
                case 14110: {
                    int indexNb = paraStr.indexOf("m");
                    String value = "";
                    String paramValue = null;
                    if (indexNb != -1) {
                        for (int i = indexNb + 1; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // ”spec/“后面的数字
                case 14488:
                case 14625:
                case 14444:
                case 14464:
                case 14603:
                case 14618:
                case 14630:
                case 14025:
                case 14027:
                case 14042: {
                    int indexNb = paraStr.indexOf("spec/");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 5; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // “/”第二个参数，前缀数字

                case 14545:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String str = strArr[2];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else {
                                break;
                            }

                        }
                        if (!value.equals("")) {
                            paramValue = value;
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(176);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 176);

                    }
                    isSpecialParam = true;
                    break;
                // “/”第三个参数，前缀数字
                case 14604:
                case 14026:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else {
                                break;
                            }

                        }
                        if (!value.equals("")) {
                            paramValue = value;
                        }
                        ParamModel param = new ParamModel();
                        param.setActionId(actionId);
                        param.setParamValue(paramValue);
                        param.setParamTypeId(176);
                        param.setLogCount(1L);
                        paramMap.addParam(param);
                        LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                                + paramValue + " uri is" + uri + " param type is" + 176);

                    }
                    isSpecialParam = true;
                    break;
                // s 后的数字
                case 14517: {
                    int indexNb = paraStr.indexOf("s");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 1; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }
                        }
                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // nc 后的数字
                case 14548: {
                    int indexNc = paraStr.indexOf("nc");
                    String value = "";
                    String paramValue = null;

                    if (indexNc != -1) {
                        for (int i = indexNc + 2; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // P- 后面的数字
                case 14461:
                case 14615: {
                    int indexNc = paraStr.indexOf("p-");
                    String value = "";
                    String paramValue = null;

                    if (indexNc != -1) {
                        for (int i = indexNc + 2; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // “/”第二个参数，“-”分隔，第二部分前缀数字
                case 14021:
                case 14024:
                case 14092:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        if (strArr[2].split("-").length > 1) {
                            String str = strArr[2].split("-")[1];
                            String value = "";
                            String paramValue = null;
                            for (int i = 0; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(176);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("车型" + "action id" + actionId + " param value is"
                                    + paramValue + " uri is" + uri + " param type is"
                                    + 176);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // photo_p,的后缀数字
                case 14033:
                case 14037: {
                    int indexNc = paraStr.indexOf("photo_p");
                    String value = "";
                    String paramValue = null;

                    if (indexNc != -1) {
                        for (int i = indexNc + 7; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("车型" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // model后的数字
                case 14072: {
                    int indexNb = paraStr.indexOf("model");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 5; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // detail
                case 14006: {
                    int indexNb = paraStr.indexOf("detail");
                    String value = "";
                    String paramValue = null;

                    if (indexNb != -1) {
                        for (int i = indexNb + 6; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else {
                                break;
                            }

                        }

                        if (!value.equals("")) {
                            paramValue = value;
                        }
                    }
                    ParamModel param = new ParamModel();
                    param.setActionId(actionId);
                    param.setParamValue(paramValue);
                    param.setParamTypeId(176);
                    param.setLogCount(1L);
                    paramMap.addParam(param);
                    LOG.info("汽车品牌" + "action id" + actionId + " param value is"
                            + paramValue + " uri is" + uri + " param type is" + 176);
                }
                isSpecialParam = true;
                break;
                // "/"第4个参数，“-”分隔第4部分，sp后数字
                case 14722:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        if (strArr[3].split("-").length > 3) {
                            String str = strArr[3].split("-")[3];
                            String value = "";
                            String paramValue = null;
                            for (int i = 2; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(176);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("车型" + "action id" + actionId + " param value is"
                                    + paramValue + " uri is" + uri + " param type is"
                                    + 176);
                        }
                    }
                    isSpecialParam = true;
                    break;
                case 14723:
                    // "/"第4个参数，“-”分隔第3部分，s后数字

                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        if (strArr[3].split("-").length > 2) {
                            String str = strArr[3].split("-")[2];
                            String value = "";
                            String paramValue = null;
                            for (int i = 1; i < str.length(); i++) {
                                if (Character.isDigit(str.charAt(i))) {
                                    value += str.charAt(i);
                                } else {
                                    break;
                                }

                            }

                            if (!value.equals("")) {
                                paramValue = value;
                            }
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(176);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("车型" + "action id" + actionId + " param value is"
                                    + paramValue + " uri is" + uri + " param type is"
                                    + 176);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // carids=后，“，”分隔大于0的数字
                case 14445:
                case 14447:
                case 14446: {
                    int indexNb = paraStr.indexOf("carids=");
                    String value = "";
                    String paramValue = null;
                    if (indexNb != -1) {
                        for (int i = indexNb + 7; i < paraStr.length(); i++) {
                            if (Character.isDigit(paraStr.charAt(i))) {
                                value += paraStr.charAt(i);
                            } else if (paraStr.charAt(i) == ',' || paraStr.charAt(i) == '?') {
                                if (value != null && !value.equals("")) {
                                    paramValue = value;
                                    value = "";
                                    if (Integer.valueOf(paramValue) > 0) {
                                        ParamModel param = new ParamModel();
                                        param.setActionId(actionId);
                                        param.setParamValue(paramValue);
                                        param.setParamTypeId(176);
                                        param.setLogCount(1L);
                                        paramMap.addParam(param);
                                        LOG.info("汽车品牌" + "action id" + actionId
                                                + " param value is" + paramValue
                                                + " uri is" + uri + " param type is"
                                                + 176);
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
                isSpecialParam = true;
                // 车型对比,"/"第二个参数，"分隔"
                case 14511:
                case 14648: {
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String str = strArr[2];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {

                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else if (str.charAt(i) == '-' || str.charAt(i) == '.') {
                                if (value != null && !value.equals("")) {
                                    paramValue = value;
                                    value = "";
                                    if (Integer.valueOf(paramValue) > 0) {
                                        ParamModel param = new ParamModel();
                                        param.setActionId(actionId);
                                        param.setParamValue(paramValue);
                                        param.setParamTypeId(176);
                                        param.setLogCount(1L);
                                        paramMap.addParam(param);
                                        LOG.info("汽车品牌" + "action id" + actionId
                                                + " param value is" + paramValue
                                                + " uri is" + uri + " param type is"
                                                + 176);
                                    }
                                }
                            } else {

                                break;
                            }
                        }
                    }
                }
                isSpecialParam = true;
                break;
                // "/"第三个参数，-分隔的多组参数
                case 14355:
                    strArr = paraStr.split("/");
                    if (strArr.length > 2) {
                        String str = strArr[2];
                        String[] params = str.split("-");
                        for (String paramValue : params) {
                            ParamModel param = new ParamModel();
                            param.setActionId(actionId);
                            param.setParamValue(paramValue);
                            param.setParamTypeId(116);
                            param.setLogCount(1L);
                            paramMap.addParam(param);
                            LOG.info("汽车品牌" + "action id" + actionId
                                    + " param value is" + paramValue + " uri is" + uri
                                    + " param type is" + 116);
                        }
                    }
                    isSpecialParam = true;
                    break;
                // "/"，第三参数，-隔开，m后面数字多组
                case 14574:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else if (str.charAt(i) == '-' || str.charAt(i) == '.') {
                                if (value != null && !value.equals("")) {
                                    paramValue = value;
                                    value = "";
                                    if (Integer.valueOf(paramValue) > 0) {
                                        ParamModel param = new ParamModel();
                                        param.setActionId(actionId);
                                        param.setParamValue(paramValue);
                                        param.setParamTypeId(176);
                                        param.setLogCount(1L);
                                        paramMap.addParam(param);
                                        LOG.info("汽车品牌" + "action id" + actionId
                                                + " param value is" + paramValue
                                                + " uri is" + uri + " param type is"
                                                + 176);
                                    }
                                }
                            } else if (str.charAt(i) == 'm') {
                                // do nothing

                            } else {
                                break;
                            }

                        }
                    }
                    isSpecialParam = true;
                    break;
                // 第三个参数，“-”分隔数字，大于0
                case 14600:
                    strArr = paraStr.split("/");
                    if (strArr.length > 3) {
                        String str = strArr[3];
                        String value = "";
                        String paramValue = null;
                        for (int i = 0; i < str.length(); i++) {
                            if (Character.isDigit(str.charAt(i))) {
                                value += str.charAt(i);
                            } else if (str.charAt(i) == '-') {
                                if (!value.equals("")) {
                                    paramValue = value;
                                    value = "";
                                    if (Integer.valueOf(paramValue) > 0) {
                                        ParamModel param = new ParamModel();
                                        param.setActionId(actionId);
                                        param.setParamValue(paramValue);
                                        param.setParamTypeId(116);
                                        param.setLogCount(1L);
                                        paramMap.addParam(param);
                                        LOG.info("汽车品牌" + "action id" + actionId
                                                + " param value is" + paramValue
                                                + " uri is" + uri + " param type is"
                                                + 116);
                                    }
                                }
                            } else {
                                break;
                            }

                        }
                    }
                    isSpecialParam = true;
                    break;

                default:
                    break;

            }
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.toString());
        }
        return isSpecialParam;
    }
}