package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Lucas on 2017/3/30.
 */
public class UserAgentModel extends ExtractValueModel {

    private Text userAgent;
    private Text preImei;

    public UserAgentModel(){
        userAgent = new Text();
        preImei = new Text();
    }

    @Override
    public String getName() {
        return "useragent";
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        userAgent.write(dataOutput);
        preImei.write(dataOutput);
        getLogCount().write(dataOutput);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        userAgent.readFields(dataInput);
        preImei.readFields(dataInput);
        getLogCount().readFields(dataInput);

    }

    public Text getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(Text userAgent) {
        this.userAgent = userAgent;
    }

    public Text getPreImei() {
        return preImei;
    }

    public void setPreImei(Text preImei) {
        this.preImei = preImei;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(userAgent.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(preImei.toString()).append(ExtractValueModel.SEPARATOR);
        builder.append(getLogCount().toString());
        return builder.toString();
    }
}
