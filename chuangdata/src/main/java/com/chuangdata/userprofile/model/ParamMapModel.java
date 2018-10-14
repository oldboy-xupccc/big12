package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ParamMapModel extends ExtractValueModel {

    private MapWritable paramMap;

    public ParamMapModel() {
        paramMap = new MapWritable();
    }

    public void addParam(ParamModel paramModel) {
        this.paramMap.put(new LongWritable(paramModel.getId()), paramModel);
    }

    public Set<ParamModel> getParamModels() {
        Set<ParamModel> valueSet = new HashSet<ParamModel>();
        for (Writable val : paramMap.values()) {
            valueSet.add((ParamModel) val);
        }
        return valueSet;
    }

    public void readFields(DataInput in) throws IOException {
        paramMap.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        paramMap.write(out);
    }

    @Override
    public String getName() {
        return "params";
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ParamModel model : getParamModels()) {
            builder.append(model.toString()).append(ExtractValueModel.SEPARATOR);
        }
        if (builder.length() > 0)
            return builder.toString().substring(0, builder.length() - 1);
        return builder.toString();
    }

}
