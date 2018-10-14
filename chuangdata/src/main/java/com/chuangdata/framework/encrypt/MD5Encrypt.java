package com.chuangdata.framework.encrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author luxiaofeng
 * @date 2016-10-1-20
 * changed history
 * fixed the bug of the singleton class
 */
public class MD5Encrypt {
    private static MD5Encrypt instance = new MD5Encrypt();
    private static String saltValue = "@chuangdatadum@";
    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private MD5Encrypt() {

    }

    public static MD5Encrypt getInstance() {
        return instance;
    }

    // 返回形式为数字跟字符串
    private String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public String encrypt(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public String encryptWithSaltValue(String strObj) {
        String resultString = null;
        strObj = strObj + saltValue;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }


    public static void main(String[] args) throws Exception {
        MD5Encrypt encrypter = MD5Encrypt.getInstance();
        if (args == null || args.length < 2) {
            printHelp();
            return;
        }

        try {
            if (args[0].equals("-e") && args[1].equals("-f")) {
                String fileName = args[2];
                String seperator = args[3];
                int columnIndex = Integer.parseInt(args[4]);
                encrypter.encryptFile(fileName, seperator, columnIndex);
            } else if (args[0].equals("-e") && args[1].equals("-uppercase")) {
                System.out.println(encrypter.encrypt(args[1]).toUpperCase());
            } else if (args[0].equals("-e") && args[1].equals("-lowercase")) {
                System.out.println(encrypter.encrypt(args[1]).toLowerCase());
            } else if (args[0].equals("-e")) {
                System.out.println(encrypter.encrypt(args[1]));
            } else {
                printHelp();
                return;
            }
        } catch (Exception e) {
            printHelp();
            return;
        }
    }

    private void encryptFile(String fileName, String seperator, int columnIndex) throws Exception {
        handleFile(fileName, seperator, columnIndex);
    }

    private void handleFile(String fileName, String seperator, int columnIndex) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] info = line.split(seperator);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < info.length; i++) {
                if (i == columnIndex) {
                    String res = encrypt(info[i]);
                    builder.append(res);
                } else {
                    builder.append(info[i]);
                }
                if (i < info.length - 1) {
                    builder.append(seperator);
                }
            }
            System.out.println(builder.toString());
        }
        reader.close();
    }

    public static void printHelp() {
        System.out.println(""
                + "MD5Encrypt help:\n"
                + "\t-e <string> encrypt the input string\n"
                + "\t-e -f <filepath> <separator> <index> encrypt the input file for column index\n"
                + "\t-e -uppercase <string> encrypt the input string and return uppercase\n"
                + "\t-e -lowercase <string> encrypt the input string and return lowercase\n"
        );
    }
}
