package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ExtractKeyModel {
    protected static final String SEPARATOR = "|";
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long MIN_DATE = 0L;

    static {
        try {
            MIN_DATE = DATE_FORMAT.parse("2000-01-01 00:00:00").getTime();
        } catch (ParseException e) {
            // TODO do nothing for now
        }
    }

    protected LongWritable time;
    protected Text msisdn;
    protected Text imei;
    protected Text imsi;
    protected Text userIp;

    public ExtractKeyModel() {
        time = new LongWritable();
        msisdn = new Text();
        imei = new Text();
        imsi = new Text();
        userIp = new Text();
    }

    public long getTime() {
        return time.get();
    }

    public void setTime(long time) {
        this.time.set(time);
    }

    public String getMsisdn() {
        return msisdn.toString();
    }

    /**
     * Should be override
     *
     * @param origin
     * @return
     */
    protected abstract String encrypt(String origin);

    /**
     * Msisdn will be encrypted
     *
     * @param msisdn
     */
    public void setMsisdn(String msisdn) {
        try {
            this.msisdn.set(encrypt(msisdn));
        } catch (Exception e) {
            // TODO how to handle encrypt exception ?
        }
    }

    public void setEncryptedMsisdn(String encryptedMsisdn) {
        this.msisdn.set(encryptedMsisdn);
    }

    public String getImei() {
        return imei.toString();
    }

    /**
     * Imei will be encrypted
     *
     * @param imei
     */
    public void setImei(String imei) {
        try {
            this.imei.set(encrypt(imei));
        } catch (Exception e) {
            // TODO how to handle encrypt exception ?
        }
    }

    public void setEncryptedImei(String encryptedImei) {
        this.imei.set(encryptedImei);
    }

    public String getImsi() {
        return imsi.toString();
    }

    /**
     * Imsi will be encrypted
     *
     * @param imsi
     */
    public void setImsi(String imsi) {
        try {
            this.imsi.set(encrypt(imsi));
        } catch (Exception e) {
            // TODO how to handle encrypt exception ?
        }
    }

    public void setEncryptedImsi(String encryptedImsi) {
        this.imsi.set(encryptedImsi);
    }

    public String getUserIp() {
        return userIp.toString();
    }

    public void setUserIp(String userIp) {
        try {
            this.userIp.set(encrypt(userIp));
        } catch (Exception e) {
            // TODO how to handle encrypt exception ?
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(convertTime(DATE_FORMAT)).append(SEPARATOR);
        builder.append(msisdn.toString()).append(SEPARATOR);
        builder.append(imei.toString()).append(SEPARATOR);
        builder.append(imsi.toString()).append(SEPARATOR);
        builder.append(userIp.toString());
        return builder.toString();
    }

    protected String convertTime(SimpleDateFormat DATE_FORMAT) {
        if (this.time.get() < MIN_DATE && this.time.get() * 1000 > MIN_DATE) {
            // unit is s
            return DATE_FORMAT.format(new Date(time.get() * 1000));
        } else if (this.time.get() > MIN_DATE) {
            // unit is ms
            return DATE_FORMAT.format(new Date(time.get()));
        } else {
            // unknow, ignore for now
            return time.toString();
        }
    }


}
