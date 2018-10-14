package com.chuangdata.framework.logmodel.parser;

public class LogModelParserException extends Exception {
    private static final long serialVersionUID = -1696009048955093563L;

    public LogModelParserException(String msg) {
        super(msg);
    }

    public LogModelParserException(String msg, Throwable e) {
        super(msg, e);
    }

}
