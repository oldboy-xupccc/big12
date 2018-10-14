package com.chuangdata.framework.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * @date 2016-10-1-20
 * changed history
 * fixed the bug of the singleton class
 */

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AESEncrypt {

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "chuangdatadmu@gz";
    private String ivParameter = "internetmeasures";
    private static AESEncrypt instance = new AESEncrypt();

    private AESEncrypt() {

    }

    public static AESEncrypt getInstance() {
        return instance;
    }

    // 加密
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new String(Base64.encodeBase64(encrypted));// 此处使用BASE64做转码。
    }

    // 解密
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        AESEncrypt encrypter = AESEncrypt.getInstance();
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
            } else if (args[0].equals("-e")) {
                System.out.println(encrypter.encrypt(args[1]));
            } else if (args[0].equals("-d") && args[1].equals("-f")) {
                String fileName = args[2];
                String seperator = args[3];
                int columnIndex = Integer.parseInt(args[4]);
                encrypter.decryptFile(fileName, seperator, columnIndex);
            } else if (args[0].equals("-d")) {
                System.out.println(encrypter.decrypt(args[1]));
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
        handleFile(fileName, seperator, columnIndex, true);
    }

    private void decryptFile(String fileName, String seperator, int columnIndex) throws Exception {
        handleFile(fileName, seperator, columnIndex, false);
    }

    private void handleFile(String fileName, String seperator, int columnIndex, boolean encrypt) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] info = line.split(seperator);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < info.length; i++) {
                if (i == columnIndex) {
                    String res = encrypt ? encrypt(info[i]) : decrypt(info[i]);
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
                + "AESEncrypt help:\n"
                + "\t-e <string> encrypt the input string\n"
                + "\t-e -f <filepath> <separator> <index> encrypt the input file for column index\n"
                + "\t-d <string> decrypt the input string\n"
                + "\t-d -f <filepath> <separator> <index> decrypt the input file for column index\n"
        );
    }
}