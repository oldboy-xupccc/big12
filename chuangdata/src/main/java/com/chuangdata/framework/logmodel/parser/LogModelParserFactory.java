package com.chuangdata.framework.logmodel.parser;

import com.chuangdata.framework.logmodel.LogFormatType;
import com.chuangdata.framework.logmodel.LogModelType;
import com.chuangdata.framework.logmodel.UnsupportedLogModelNameException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

public class LogModelParserFactory {
    public static LogModelParser getLogModelParser(String logTypeName)
            throws LogModelParserException, UnsupportedLogModelNameException,
            JsonParseException, JsonMappingException, IOException {
        if (logTypeName == null || logTypeName.isEmpty()) {
            throw new LogModelParserException("Invaild logTypeName: " + logTypeName);
        }
        LogModelType logModelType = LogModelType.getLogModelType(logTypeName);
        if (logModelType.getLogFormatType() == LogFormatType.TEXT) {
            return new TextLogModelParser(logModelType.getConfigFilePath());
        } else if (logModelType.getLogFormatType() == LogFormatType.BINARY) {
            // throw new
            // LogModelParserException("Currently we don't support binary log.");
        }
        throw new LogModelParserException("Currently we don't support the log format type: "
                + logModelType.getLogFormatType());
    }

    public static LogModelParser getTextLogModelParserFromExternalConfig(String configFilePath)
            throws LogModelParserException, JsonParseException, JsonMappingException, IOException {
        if (configFilePath == null || configFilePath.isEmpty()) {
            throw new LogModelParserException("Invaild configFilePath: " + configFilePath);
        }
        return new TextLogModelParser(configFilePath, true);
    }
}
