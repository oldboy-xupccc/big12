package com.chuangdata.userprofile.model;

import com.chuangdata.framework.encrypt.AESEncrypt;

public class ExtractAESEncrypterKeyModel extends ExtractKeyModel {
    private static final AESEncrypt encrypter = AESEncrypt.getInstance();

    @Override
    protected String encrypt(String origin) {
        try {
            return encrypter.encrypt(origin);
        } catch (Exception e) {
            // TODO how to handle encrypt exception ?
            return origin;
        }
    }
}
