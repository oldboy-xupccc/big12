package com.chuangdata.framework.logmodel.parser;

import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.logmodel.text.ByteOffsetLogModel;
import com.chuangdata.framework.logmodel.text.TextLogModel;
import com.chuangdata.framework.logmodel.text.TextLogModelConfig;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextLogModelParser implements LogModelParser {
    private ObjectMapper objectMapper;
    private TextLogModelConfig config;

    public TextLogModelParser(String configFilePath) throws JsonParseException, JsonMappingException, IOException {
        this(configFilePath, false);
    }

    public TextLogModelParser(String configFilePath, boolean isExternal) throws JsonParseException, JsonMappingException, IOException {
        objectMapper = new ObjectMapper();
        InputStream inputstream;
        if (isExternal) {
            inputstream = new FileInputStream(configFilePath);
        } else {
            inputstream = this.getClass().getResourceAsStream(configFilePath);
        }
        config = objectMapper.readValue(inputstream, TextLogModelConfig.class);
    }

    public LogModel parse(Object log) {
        return new TextLogModel(split((String) log, config.getSeparator()), config.getFieldsIndexMap());
    }

    public LogModel parse(byte[] data, long[] offset, int fieldsNumber) {
        return new ByteOffsetLogModel(new String[config.getFieldsIndexMap().size()], config.getFieldsIndexMap(), data, offset, fieldsNumber);
    }

    private String[] split(String log, String separator) {
        String[] info = log.split(separator);
        String[] result = info;
        if (log.endsWith(separator)) {
            result = new String[info.length + 1];
            System.arraycopy(info, 0, result, 0, info.length);
            result[info.length] = separator;
        }
        return result;
    }

}
