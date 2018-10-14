package com.chuangdata.userprofile.job;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Metrics implements Writable {


    private LongWritable logCount;
    private LongWritable upByte;
    private LongWritable dnByte;

    public Metrics() {
        logCount = new LongWritable();
        upByte = new LongWritable();
        dnByte = new LongWritable();
    }


    public long getLogCount() {
        return logCount.get();
    }

    public void setLogCount(long logCount) {
        this.logCount.set(logCount);
    }

    public long getUpByte() {
        return upByte.get();
    }

    public void setUpByte(long upByte) {
        this.upByte.set(upByte);
    }

    public long getDnByte() {
        return dnByte.get();
    }

    public void setDnByte(long dnByte) {
        this.dnByte.set(dnByte);
    }

    public void readFields(DataInput in) throws IOException {
        logCount.readFields(in);
        upByte.readFields(in);
        dnByte.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        logCount.write(out);
        upByte.write(out);
        dnByte.write(out);
    }

    public void add(Metrics other) {
        this.setLogCount(this.getLogCount() + other.getLogCount());
        this.setUpByte(this.getUpByte() + other.getUpByte());
        this.setDnByte(this.getDnByte() + other.getDnByte());
    }

    public String toString(String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(logCount).append(separator);
        builder.append(upByte).append(separator);
        builder.append(dnByte);
        return builder.toString();
    }

}
