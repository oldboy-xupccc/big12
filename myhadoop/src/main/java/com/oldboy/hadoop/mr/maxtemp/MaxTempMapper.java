package com.oldboy.hadoop.mr.maxtemp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 */
public class MaxTempMapper extends Mapper<LongWritable,Text,IntWritable,IntWritable> {
	public MaxTempMapper(){
		System.out.println("new MaxTempMapper()");

	}
	IntWritable keyOut = new IntWritable();
	IntWritable valueOut = new IntWritable();

	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String[] arr = value.toString().split(" ");
		int year = Integer.parseInt(arr[0]) ;
		int temp = Integer.parseInt(arr[1]) ;
		keyOut.set(year);
		valueOut.set(temp);
		context.write(keyOut,valueOut);

	}
}
