package com.oldboy.hadoop.mr.maxtemp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * reducer
 */
public class MaxTempReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

	protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

		int max = Integer.MIN_VALUE ;

		for(IntWritable i : values){
			max = Math.max(max , i.get()) ;
		}
		context.write(key , new IntWritable(max));
	}
}
