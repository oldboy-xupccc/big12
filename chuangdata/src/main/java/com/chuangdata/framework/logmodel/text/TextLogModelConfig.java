package com.chuangdata.framework.logmodel.text;

import java.util.Map;

public class TextLogModelConfig {
    private String separator;
    private Map<String, Integer> fieldsIndexMap;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public Map<String, Integer> getFieldsIndexMap() {
        return fieldsIndexMap;
    }

    public void setFieldsIndexMap(Map<String, Integer> fieldsIndexMap) {
        this.fieldsIndexMap = fieldsIndexMap;
    }

}
