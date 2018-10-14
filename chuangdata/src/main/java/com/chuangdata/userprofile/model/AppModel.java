package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.IntWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AppModel extends ExtractValueModel {

    private IntWritable appId;
    private IntWritable appTypeId;

    public AppModel() {
        appId = new IntWritable();
        appTypeId = new IntWritable();
    }

    public IntWritable getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(IntWritable appTypeId) {
        this.appTypeId = appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId.set(appTypeId);
    }

    public IntWritable getAppId() {
        return appId;
    }

    public void setAppId(IntWritable appId) {
        this.appId = appId;
    }

    public void setAppId(int appId) {
        this.appId.set(appId);
    }

    public void readFields(DataInput in) throws IOException {
        appId.readFields(in);
        appTypeId.readFields(in);
        getLogCount().readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        appId.write(out);
        appTypeId.write(out);
        getLogCount().write(out);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(appId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(appTypeId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(getLogCount().toString());
        return builder.toString();
    }

    public String getName() {
        return "app";
    }

}
