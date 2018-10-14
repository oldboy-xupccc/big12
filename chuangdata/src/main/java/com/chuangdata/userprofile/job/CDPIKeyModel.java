package com.chuangdata.userprofile.job;

import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author luxiaofeng
 */
public class CDPIKeyModel extends KeyModel {

    private Text msisdn;
    private Text imsi;

    public CDPIKeyModel() {
        msisdn = new Text();
        imsi = new Text();
    }

    public Text getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn.set(msisdn);
    }

    public Text getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi.set(imsi);
    }

    public Text getImei() {
        return getUserId();
    }

    public void setImei(String imei) {
        setUserId(imei);
    }

    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        msisdn.readFields(in);
        imsi.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        super.write(out);
        msisdn.write(out);
        imsi.write(out);
    }

    public String toString(String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(getLogTypeId()).append(separator);
        builder.append(getUserId()).append(separator);
        builder.append(getTimeStamp()).append(separator);
        builder.append(getHost()).append(separator); // should be an ID
        builder.append(getRefererHost()).append(separator); // should be an ID
        builder.append(getOs()).append(separator); // should be an ID
        builder.append(getTerminal()).append(separator); // should be an ID
        builder.append(getTerminalProducer()).append(separator); // should be an ID
        builder.append(getBrowser()).append(separator); // should be an ID
//		builder.append(cookie).append(separator); // jiangsu do not output cookie
//		builder.append(userIp).append(separator); // don't output
        builder.append(getServerIp()).append(separator);
        builder.append(getAppId()).append(separator);
        builder.append(getAppActionId()).append(separator);
        builder.append(getUrlParams()).append(separator);
        builder.append(getOtherParams()).append(separator);
        builder.append(getMsisdn());
        return builder.toString();
    }
}
