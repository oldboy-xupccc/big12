package com.chuangdata.userprofile.model;

import com.chuangdata.framework.encrypt.AESEncrypt;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ParamModel extends ExtractValueModel {
    private LongWritable id;
    private IntWritable actionId;
    private Text param;
    private Text paramValue;
    private IntWritable paramTypeId;
    private static final AESEncrypt encrypter = AESEncrypt.getInstance();


    public ParamModel() {
        id = new LongWritable();
        actionId = new IntWritable();
        param = new Text();
        paramValue = new Text();
        paramTypeId = new IntWritable();
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public int getActionId() {
        return actionId.get();
    }

    public void setActionId(int actionId) {
        this.actionId.set(actionId);
    }

    public String getParam() {
        return param.toString();
    }

    public void setParam(String param) {
        this.param.set(param);
    }

    public String getParamValue() {
        return paramValue.toString();
    }

    public void setParamValue(String paramValue) {
        setParamValue(paramValue, false);
    }

    public void setParamValue(String paramValue, boolean isUserId) {
        this.paramValue.set(paramValue);
        if (isUserId) {
            try {
                this.paramValue.set(encrypter.encrypt(paramValue));
            } catch (Exception e) {
                // TODO do nothing for now
            }
        }

    }

    public int getParamTypeId() {
        return paramTypeId.get();
    }

    public void setParamTypeId(int paramTypeId) {
        this.paramTypeId.set(paramTypeId);
    }

    public void readFields(DataInput in) throws IOException {
        id.readFields(in);
        actionId.readFields(in);
        param.readFields(in);
        paramValue.readFields(in);
        paramTypeId.readFields(in);
        getLogCount().readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        id.write(out);
        actionId.write(out);
        param.write(out);
        paramValue.write(out);
        paramTypeId.write(out);
        getLogCount().write(out);
    }

    @Override
    public String getName() {
        return "param";
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(actionId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(param.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(paramValue.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(paramTypeId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(getLogCount().toString());
        return builder.toString();
    }
}
