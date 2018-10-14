package com.chuangdata.userprofile.job.ct.kvjob;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author luxiaofeng
 * @date 2016-10-05
 */
public class TagToUserCountModel implements Writable {
    private Text imeiId;
    private IntWritable provinceId;
    private LongWritable logCount;

    public TagToUserCountModel() {
        provinceId = new IntWritable();
        imeiId = new Text();
        logCount = new LongWritable();
    }

    public Text getImeiId() {
        return imeiId;
    }

    public void setImeiId(Text imeiId) {
        this.imeiId = imeiId;
    }

    public IntWritable getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(IntWritable provinceId) {
        this.provinceId = provinceId;
    }

    public LongWritable getLogCount() {
        return logCount;
    }

    public void setLogCount(LongWritable logCount) {
        this.logCount = logCount;
    }

    public void readFields(DataInput in) throws IOException {
        imeiId.readFields(in);
        provinceId.readFields(in);
        logCount.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        imeiId.write(out);
        provinceId.write(out);
        logCount.write(out);
    }
}
