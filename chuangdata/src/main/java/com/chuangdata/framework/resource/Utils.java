package com.chuangdata.framework.resource;

/**
 * @author luxiaofeng
 */
public class Utils {

    public static String replaceSeparator(String value) {
        // 替换字段分隔符，一般为'\t', '|', ',', '\001'等, 替换为空格
        String str = value.replaceAll("\\t", " ");
        str = str.replaceAll("\\|", " ");
        str = str.replaceAll("\001", " ");
        str = str.replaceAll(",", " ");
        return str;
    }
}
