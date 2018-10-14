package com.chuangdata.framework.logmodel;

public class FieldNotFoundException extends Exception {

    private static final long serialVersionUID = 4573076007672353824L;

    public FieldNotFoundException(String msg) {
        super(msg);
    }

    public FieldNotFoundException(String msg, Throwable e) {
        super(msg, e);
    }

}
