package com.chuangdata.userprofile.job;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class KeyModel implements WritableComparable<KeyModel> {

    private Text logTypeId;
    private Text userId; // for mobile log, the userId is meid, for fixed log, the userId is ad
    private LongWritable timeStamp;
    private Text host;
    private Text refererHost;
    private Text os;
    private Text terminal;
    private Text terminalProducer;
    private Text browser;
    private Text cookie;
    private Text userIp;
    private Text serverIp;
    private LongWritable appId;
    private LongWritable appActionId;
    private Text urlParams;
    private Text otherParams;

    public KeyModel() {
        logTypeId = new Text();
        userId = new Text();
        timeStamp = new LongWritable();
        host = new Text();
        refererHost = new Text();
        os = new Text();
        terminal = new Text();
        terminalProducer = new Text();
        browser = new Text();
        cookie = new Text();
        userIp = new Text();
        serverIp = new Text();
        appId = new LongWritable(-1);
        appActionId = new LongWritable(-1);
        urlParams = new Text();
        otherParams = new Text();
    }

    public void readFields(DataInput in) throws IOException {
        logTypeId.readFields(in);
        userId.readFields(in);
        timeStamp.readFields(in);
        host.readFields(in);
        refererHost.readFields(in);
        os.readFields(in);
        terminal.readFields(in);
        terminalProducer.readFields(in);
        browser.readFields(in);
        cookie.readFields(in);
        userIp.readFields(in);
        serverIp.readFields(in);
        appId.readFields(in);
        appActionId.readFields(in);
        urlParams.readFields(in);
        otherParams.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        logTypeId.write(out);
        userId.write(out);
        timeStamp.write(out);
        host.write(out);
        refererHost.write(out);
        os.write(out);
        terminal.write(out);
        terminalProducer.write(out);
        browser.write(out);
        cookie.write(out);
        userIp.write(out);
        serverIp.write(out);
        appId.write(out);
        appActionId.write(out);
        urlParams.write(out);
        otherParams.write(out);
    }

    public Text getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId.set(logTypeId);
    }

    public Text getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public LongWritable getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp.set(timeStamp);
    }

    public Text getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public Text getRefererHost() {
        return refererHost;
    }

    public void setRefererHost(String refererHost) {
        this.refererHost.set(refererHost);
    }

    public Text getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os.set(os);
    }

    public Text getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal.set(terminal);
    }

    public Text getTerminalProducer() {
        return terminalProducer;
    }

    public void setTerminalProducer(String terminalProducer) {
        this.terminalProducer.set(terminalProducer);
    }

    public Text getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser.set(browser);
    }

    public Text getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie.set(cookie);
    }

    public Text getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp.set(userIp);
    }

    public Text getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp.set(serverIp);
    }

    public LongWritable getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId.set(appId);
    }

    public LongWritable getAppActionId() {
        return appActionId;
    }

    public void setAppActionId(long appActionId) {
        this.appActionId.set(appActionId);
    }

    public Text getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams.set(urlParams);
    }

    public Text getOtherParams() {
        return otherParams;
    }

    public void setOtherParams(String otherParams) {
        this.otherParams.set(otherParams);
    }

    public String toString(String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(logTypeId).append(separator);
        builder.append(userId).append(separator);
        builder.append(timeStamp).append(separator);
        builder.append(host).append(separator); // should be an ID
        builder.append(refererHost).append(separator); // should be an ID
        builder.append(os).append(separator); // should be an ID
        builder.append(terminal).append(separator); // should be an ID
        builder.append(terminalProducer).append(separator); // should be an ID
        builder.append(browser).append(separator); // should be an ID
        builder.append(cookie).append(separator); // should empty
//		builder.append(userIp).append(separator); // don't output
        builder.append(serverIp).append(separator);
        builder.append(appId).append(separator);
        builder.append(appActionId).append(separator);
        builder.append(urlParams).append(separator);
        builder.append(otherParams); // should only nai
        return builder.toString();
    }

    public String toString() {
        return toString(",");
    }

    public int compareTo(KeyModel other) {
        // 1. compare time
        int c_time = this.timeStamp.compareTo(other.timeStamp);
        if (c_time != 0) return c_time;
        // 2. compare other fields
        return this.toString(",").compareTo(other.toString(","));
    }

    @Override
    public int hashCode() {
        return
                this.logTypeId.hashCode() +
                        this.userId.hashCode() +
                        this.timeStamp.hashCode() +
                        this.host.hashCode() +
                        this.refererHost.hashCode() +
                        this.os.hashCode() +
                        this.terminal.hashCode() +
                        this.terminalProducer.hashCode() +
                        this.browser.hashCode() +
                        this.cookie.hashCode() +
                        this.userIp.hashCode() +
                        this.serverIp.hashCode() +
                        this.appId.hashCode() +
                        this.appActionId.hashCode() +
                        this.urlParams.hashCode() +
                        this.otherParams.hashCode() ;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyModel)) {
            return false;
        }
        KeyModel keyModel = (KeyModel) obj;

        if(!this.logTypeId.equals(keyModel.logTypeId)){
            return false;
        }

        if(!this.userId.equals(keyModel.userId)){
            return false;
        }


        if(!this.timeStamp.equals(keyModel.timeStamp)){
            return false;
        }

        if(!this.host.equals(keyModel.host)){
            return false;
        }

        if(!this.refererHost.equals(keyModel.refererHost)){
            return false;
        }

        if(!this.os.equals(keyModel.os)){
            return false;
        }

        if(!this.terminal.equals(keyModel.terminal)){
            return false;
        }

        if(!this.terminalProducer.equals(keyModel.terminalProducer)){
            return false;
        }

        if(!this.browser.equals(keyModel.browser)){
            return false;
        }

        if(!this.cookie.equals(keyModel.cookie)){
            return false;
        }

        if(!this.userIp.equals(keyModel.userIp)){
            return false;
        }

        if(!this.serverIp.equals(keyModel.serverIp)){
            return false;
        }

        if(!this.appId.equals(keyModel.appId)){
            return false;
        }

        if(!this.appActionId.equals(keyModel.appActionId)){
            return false;
        }

        if(!this.urlParams.equals(keyModel.urlParams)){
            return false;
        }

        if(!this.otherParams.equals(keyModel.otherParams)){
            return false;
        }


        return true;
    }

}
