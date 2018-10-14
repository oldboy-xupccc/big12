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


public class UserIdToTagLogCountModel implements Writable {

    private IntWritable tagId;
    private LongWritable logCount;
    private Text otherParam;

    public UserIdToTagLogCountModel() {
        tagId = new IntWritable();
        logCount = new LongWritable();
        otherParam = new Text();
    }

    public IntWritable getTagId() {
        return tagId;
    }

    public void setTagId(IntWritable tagId) {
        this.tagId = tagId;
    }

    public Text getOtherParam() {
        return otherParam;
    }

    public void setOtherParam(Text otherParam) {
        this.otherParam = otherParam;
    }

    public LongWritable getLogCount() {
        return logCount;
    }

    public void setLogCount(LongWritable logCount) {
        this.logCount = logCount;
    }


    public void readFields(DataInput in) throws IOException {
        tagId.readFields(in);
        logCount.readFields(in);
        otherParam.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        tagId.write(out);
        logCount.write(out);
        otherParam.write(out);
    }
}    
