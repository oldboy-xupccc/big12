package com.oldboy.hadoop.mr.combine;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 *
 */
public class CountReducer extends Reducer<Text,IntWritable,Text,NullWritable> {
	protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int count = 0 ;
		for(IntWritable i : values){
			count = count + i.get() ;
		}
		context.write(new Text("" + count) , NullWritable.get());
	}
}
