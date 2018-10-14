package com.chuangdata.framework.encrypt.resource;

import com.chuangdata.framework.encrypt.AESEncrypt;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于加密资源文件
 *
 * @author luxiaofeng
 */
public abstract class Encrypter {

    private static final AESEncrypt ENCRYPT = AESEncrypt.getInstance();

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static final String SEPARATOR = ",";

    protected abstract String convert(String line, boolean strict) throws Exception;

    protected String[] split(String line, String separator) {
        List<String> result = new ArrayList<String>();
        int index = 0;
        while (line.indexOf(separator, index) >= 0) {
            int end = line.indexOf(separator, index);
            result.add(line.substring(index, end));
            index = end + separator.length();
        }
        // add last section
        result.add(line.substring(index));
        return result.toArray(new String[result.size()]);
    }

    public void encrypt(BufferedReader reader, BufferedWriter writer, boolean strict)
            throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                String encryptedLine = ENCRYPT.encrypt(convert(line, strict)); // secondary encrypt by AES
                writer.write(encryptedLine);
                writer.newLine();
            } catch (Exception e) {
                // DO NOTHING ?
            }
        }
    }

    /**
     * encode By MD5
     *
     * @param str
     * @return String
     */
    public static String encodeByMD5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }


    /**
     * Encrypt resource file
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 10) {
            return;
        }
        String appHostPath = args[0];
        String appHostOutPath = args[1];
        String appActionPath = args[2];
        String appActionOutPath = args[3];
        String appParamPath = args[4];
        String appParamOutPath = args[5];
        String appDomainPath = args[6];
        String appDomainOutPath = args[7];
        String tagResourPath = args[8];
        String tagResourOutPath = args[9];
        String paramTagResourPath = args[10];
        String paramTagOutPath = args[11];
        boolean strict = true;
        if (args.length == 13) {
            strict = args[12].equals("0") ? true : false;
        }
        BufferedReader reader = new BufferedReader(new FileReader(new File(appHostPath)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(appHostOutPath)));
        Encrypter appHostEncrypter = new AppHostEncrypter();
        appHostEncrypter.encrypt(reader, writer, strict);
        reader.close();
        writer.close();

        reader = new BufferedReader(new FileReader(new File(appActionPath)));
        writer = new BufferedWriter(new FileWriter(new File(appActionOutPath)));
        Encrypter appActionEncrypter = new AppActionEncrypter();
        appActionEncrypter.encrypt(reader, writer, strict);
        reader.close();
        writer.close();

        reader = new BufferedReader(new FileReader(new File(appParamPath)));
        writer = new BufferedWriter(new FileWriter(new File(appParamOutPath)));
        Encrypter appParamEncrypter = new AppParamEncrypter();
        appParamEncrypter.encrypt(reader, writer, strict);
        reader.close();
        writer.close();

        reader = new BufferedReader(new FileReader(new File(appDomainPath)));
        writer = new BufferedWriter(new FileWriter(new File(appDomainOutPath)));
        Encrypter appDomainEncrypter = new AppDomainEncrypter();
        appDomainEncrypter.encrypt(reader, writer, strict);
        reader.close();
        writer.close();

        reader = new BufferedReader(new FileReader(new File(tagResourPath)));
        writer = new BufferedWriter(new FileWriter(new File(tagResourOutPath)));
        Encrypter tagResource = new TagResourceEncryter();
        tagResource.encrypt(reader, writer, strict);
        reader.close();
        writer.close();

        reader = new BufferedReader(new FileReader(new File(paramTagResourPath)));
        writer = new BufferedWriter(new FileWriter(new File(paramTagOutPath)));
        Encrypter paramTagResource = new ParamTagEncrypter();
        paramTagResource.encrypt(reader, writer, strict);
        reader.close();
        writer.close();
    }
}
