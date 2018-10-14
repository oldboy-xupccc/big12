package com.chuangdata.userprofile.model;


public class ExtractNonEncrypterKeyModel extends ExtractKeyModel {

    @Override
    protected String encrypt(String origin) {
        return origin;
    }
}
