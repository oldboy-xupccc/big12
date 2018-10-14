package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author luxiaofeng
 */
public class AppDomainModel extends ExtractValueModel {

    private IntWritable appDomainId;
    private Text host;

    public AppDomainModel() {
        appDomainId = new IntWritable();
        host = new Text();
    }


    public IntWritable getAppDomainId() {
        return appDomainId;
    }


    public void setAppDomainId(IntWritable appDomainId) {
        this.appDomainId = appDomainId;
    }


    public void setAppDomainId(int appDomainId) {
        this.appDomainId.set(appDomainId);
    }


    public Text getHost() {
        return host;
    }


    public void setHost(Text host) {
        this.host = host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        appDomainId.readFields(in);
        host.readFields(in);
        getLogCount().readFields(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        appDomainId.write(out);
        host.write(out);
        getLogCount().write(out);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(appDomainId.toString()).append(SEPARATOR);
        builder.append(host.toString()).append(SEPARATOR);
        builder.append(getLogCount().toString());
        return builder.toString();
    }

    public String getName() {
        return "domain";
    }

}
