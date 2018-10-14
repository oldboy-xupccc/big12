package com.chuangdata.userprofile.job;

import org.apache.hadoop.mapreduce.Partitioner;

public class TimePartitioner extends Partitioner<KeyModel, Metrics> {

    @Override
    public int getPartition(KeyModel key, Metrics value, int numPartitions) {
        long time = key.getTimeStamp().get();
        return (int) (time % numPartitions);
    }

}
