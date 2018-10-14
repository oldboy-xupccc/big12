package com.chuangdata.userprofile.utils;

public class TagListUtil {

    public static String[] getTagList(String tagStr, String prefix) {
        String[] taglist = new String[4];
        for (int i = 0, j = 0; i < tagStr.length(); i = i + 8, j++) {
            taglist[j] = new String(prefix + Long.parseLong(tagStr.substring(i, i + 8), 16));
//	    	 System.out.println("index is " + j+ "the hex is " + tagStr.substring(i,i+8) +"and the long is "+ taglist[j]);
        }
        return taglist;
    }

    public static String[] getTagList(String tagStr) {
        return getTagList(tagStr, "");
    }

    public static String revertTagList(String[] tagList, int prefixLen) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tagList.length; i++) {
            if (prefixLen > 0) {
                builder.append(Long.toHexString(Long.parseLong(tagList[i].substring(prefixLen))));
            } else {
                builder.append(Long.toHexString(Long.parseLong(tagList[i])));
            }
        }
        return builder.toString();
    }

    public static String revertTagList(String[] tagList) {
        return revertTagList(tagList, 0);
    }
}
