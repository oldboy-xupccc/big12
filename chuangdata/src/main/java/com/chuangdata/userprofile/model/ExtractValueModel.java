package com.chuangdata.userprofile.model;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;

public abstract class ExtractValueModel implements Writable {
    protected static final String SEPARATOR = "|";
    private LongWritable logCount = new LongWritable();

    public LongWritable getLogCount() {
        return logCount;
    }

    public void setLogCount(LongWritable logCount) {
        this.logCount = logCount;
    }

    public void setLogCount(long logCount) {
        this.logCount.set(logCount);
    }

    public abstract String getName();
}
