package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ActionModel extends ExtractValueModel {
    private LongWritable id; // action id
    private Text host;
    private Text urlPatternStr;
    private IntWritable detailActionId;
    private IntWritable actionTypeId;
    private Text interestTag;
    private IntWritable appId;
    private IntWritable appTypeId;

    public ActionModel() {
        id = new LongWritable();
        host = new Text();
        urlPatternStr = new Text();
        detailActionId = new IntWritable();
        actionTypeId = new IntWritable();
        interestTag = new Text();
        appId = new IntWritable();
        appTypeId = new IntWritable();
    }

    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getHost() {
        return host.toString();
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public String getUrlPatternStr() {
        return urlPatternStr.toString();
    }

    public void setUrlPatternStr(String urlPatternStr) {
        this.urlPatternStr.set(urlPatternStr);
    }

    public int getActionTypeId() {
        return actionTypeId.get();
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId.set(actionTypeId);
    }

    public String getInterestTag() {
        return interestTag.toString();
    }

    public void setInterestTag(String interestTag) {
        this.interestTag.set(interestTag);
    }

    public int getDetailActionId() {
        return detailActionId.get();
    }

    public void setDetailActionId(int detailActionId) {
        this.detailActionId.set(detailActionId);
    }

    public int getAppId() {
        return appId.get();
    }

    public void setAppId(int appId) {
        this.appId.set(appId);
    }

    public int getAppTypeId() {
        return appTypeId.get();
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId.set(appTypeId);
    }

    public void readFields(DataInput in) throws IOException {
        id.readFields(in);
        host.readFields(in);
        urlPatternStr.readFields(in);
        detailActionId.readFields(in);
        actionTypeId.readFields(in);
        interestTag.readFields(in);
        appId.readFields(in);
        appTypeId.readFields(in);
        getLogCount().readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        id.write(out);
        host.write(out);
        urlPatternStr.write(out);
        detailActionId.write(out);
        actionTypeId.write(out);
        interestTag.write(out);
        appId.write(out);
        appTypeId.write(out);
        getLogCount().write(out);
    }

    @Override
    public String getName() {
        return "action";
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(host.toString()).append(ExtractValueModel.SEPARATOR);
        // builder.append(urlPatternStr.toString()).append(ExtractValueModel.SEPARATOR);
        // issue 11: http://120.236.169.50:88/imc/UserProfileApplication/issues/11
        // Only output a placeholder for host.
        builder.append("").append(ExtractValueModel.SEPARATOR);
        builder.append(detailActionId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(actionTypeId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(interestTag.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(appId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(appTypeId.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(getLogCount().toString());
        return builder.toString();
    }
}
