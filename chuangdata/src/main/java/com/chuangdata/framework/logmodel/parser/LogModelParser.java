package com.chuangdata.framework.logmodel.parser;

import com.chuangdata.framework.logmodel.LogModel;

public interface LogModelParser {
    LogModel parse(Object log);

    LogModel parse(byte[] data, long[] offset, int fieldsNumber);
}
