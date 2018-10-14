package com.chuangdata.userprofile.job.tag;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/** 
 * @author luxiaofeng
 */
public class GroupTagCombiner extends Reducer<Text, LongWritable, Text, LongWritable>{

	public void reduce(Text key, Iterable<LongWritable> value, Context context) throws IOException, InterruptedException {
    	long sumLogCount = 0;
    	for (LongWritable logCount : value) {
    		sumLogCount += logCount.get();
    	}
    	context.write(key, new LongWritable(sumLogCount));
    }
}
