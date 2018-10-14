package com.chuangdata.framework.encrypt.transform;

import java.util.HashMap;
import java.util.Map;

/**
 * 做一个简单的变换, 能从Int变成另一个Int
 *
 * @author luxiaofeng
 */
public class IntToInt {
//	private static final int[] ENCRYPT = {3, 6, 1, 5, 7, 0, 9, 8, 2, 4};
//	private static final int[] DECRYPT = {5, 2, 8, 0, 9, 3, 1, 4, 7, 6};

    private static final Map<String, String> ENCRYPT = new HashMap<String, String>();

    static {
        ENCRYPT.put("0", "3");
        ENCRYPT.put("1", "6");
        ENCRYPT.put("2", "1");
        ENCRYPT.put("3", "5");
        ENCRYPT.put("4", "7");
        ENCRYPT.put("5", "0");
        ENCRYPT.put("6", "9");
        ENCRYPT.put("7", "8");
        ENCRYPT.put("8", "2");
        ENCRYPT.put("9", "4");
    }

    private static final Map<String, String> DECRYPT = new HashMap<String, String>();

    static {
        DECRYPT.put("0", "5");
        DECRYPT.put("1", "2");
        DECRYPT.put("2", "8");
        DECRYPT.put("3", "0");
        DECRYPT.put("4", "9");
        DECRYPT.put("5", "3");
        DECRYPT.put("6", "1");
        DECRYPT.put("7", "4");
        DECRYPT.put("8", "7");
        DECRYPT.put("9", "6");
    }

    /**
     * return a string which looks like a number, but the first char may be 0
     *
     * @param id
     * @return
     */
    public static String encrypt(String id) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            String x = id.substring(i, i + 1);
            builder.append(ENCRYPT.get(x));
        }
        return builder.toString();
    }

    public static String decrypt(String id) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < id.length(); i++) {
            String x = id.substring(i, i + 1);
            builder.append(DECRYPT.get(x));
        }
        return builder.toString();
    }
}
