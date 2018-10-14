package com.chuangdata.framework.logmodel.text;

import com.chuangdata.framework.logmodel.FieldNotFoundException;

import java.util.Map;


public class ByteOffsetLogModel extends TextLogModel {

    private final byte[] data;
    private final long[] offset;
    private final int fieldsNumber;

    public ByteOffsetLogModel(String[] fields,
                              Map<String, Integer> fieldsIndexMap, byte[] data, long[] offset, int fieldsNumber) {
        super(fields, fieldsIndexMap); // actually fields should be empty, but the size is enough
        this.data = data;
        this.offset = offset;
        this.fieldsNumber = fieldsNumber;
    }

    public int getConfigFieldsNumber() {
        return fieldsIndexMap.size();
    }

    public int getActualFieldsNumber() {
        return fieldsNumber;
    }

    protected String getFields(String fieldName) throws FieldNotFoundException {
        if (fieldsIndexMap.containsKey(fieldName)) {
//            return fields[fieldsIndexMap.get(fieldName) - 1];
            if (fields[fieldsIndexMap.get(fieldName) - 1] == null) {
                // get field from data, and then set to fields
                // offset[0]绝对偏移量, offset[1]为首字段的相对偏移量,为0
                try {
                    int index = fieldsIndexMap.get(fieldName); // index从1开始
                    int start = (int) offset[index];
                    int len = (int) offset[index + 1] - start - 1; // 要求分隔符必须为1个字节长度
                    fields[fieldsIndexMap.get(fieldName) - 1] = new String(data, start, len, "UTF-8"); // FIXME hard code UTF-8
                } catch (Exception e) {
                    // throw FieldNotFoundException here is not a good idea
                    throw new FieldNotFoundException("Error to parse offset and data with name=" + fieldName);
                }
            }
            return fields[fieldsIndexMap.get(fieldName) - 1];
        }
        throw new FieldNotFoundException("Can't find field with name=" + fieldName);
    }
}
