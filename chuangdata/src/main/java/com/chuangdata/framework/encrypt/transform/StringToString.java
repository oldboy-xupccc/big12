package com.chuangdata.framework.encrypt.transform;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luxiaofeng
 */
public class StringToString {

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final Map<String, Integer> HEX_TO_INT = new HashMap<String, Integer>();

    static {
        HEX_TO_INT.put("a", 10);
        HEX_TO_INT.put("b", 11);
        HEX_TO_INT.put("c", 12);
        HEX_TO_INT.put("d", 13);
        HEX_TO_INT.put("e", 14);
        HEX_TO_INT.put("f", 15);
    }

    /**
     * 字节数据转十六进制字符串
     *
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            // 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
            // 取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
        }
        return stringBuilder.toString();
    }

    public static byte[] stringToByteArray(String data) {
        if (data.length() % 2 != 0) {
            return null;
        }
        byte[] result = new byte[data.length() / 2];
        for (int i = 0; i < data.length() - 1; i += 2) {
            String high = data.substring(i, i + 1);
            String low = data.substring(i + 1, i + 2);
            byte current = 0;
            if (HEX_TO_INT.containsKey(high)) {
                current = (byte) ((HEX_TO_INT.get(high).intValue() & 0x0f) << 4);
            } else {
                // should be parse as int directly
                current = (byte) ((Integer.parseInt(high) & 0x0f) << 4);
            }
            if (HEX_TO_INT.containsKey(low)) {
                current = (byte) (current + (byte) (HEX_TO_INT.get(low).intValue() & 0x0f));
            } else {
                // should be parse as int directly
                current = (byte) (current + (byte) (Integer.parseInt(low) & 0x0f));
            }
            result[i / 2] = current;
        }
        return result;
    }

    public static String transform(String input) {
        String output = byteArrayToString(input.getBytes());
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < output.length(); i += 15) {
            int len = 15;
            if (i + 15 >= output.length()) {
                len = output.length() - i;
            }
            if (len == 1) {
                encrypted.append(output.charAt(i + len));
            } else {
                encrypted.append(output.substring(i, i + 1)).append(".").append(output.substring(i + 1, i + len)).append(",");
            }
        }
        return encrypted.toString();
    }

    public static String deTransfrom(String input) {
        input = input.replace(",", "");
        input = input.replace(".", "");
        String output = new String(stringToByteArray(input));
        return output;
    }
}
