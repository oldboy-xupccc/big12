package com.chuangdata.userprofile.model;

import com.chuangdata.framework.encrypt.MD5Encrypt;

public class ExtractMd5EncrypterKeyModel extends ExtractKeyModel {
    private static final MD5Encrypt encrypter = MD5Encrypt.getInstance();

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
