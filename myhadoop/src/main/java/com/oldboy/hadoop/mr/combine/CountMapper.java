package com.oldboy.hadoop.mr.combine;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 统计函数
 */
public class CountMapper extends Mapper<LongWritable , Text , Text, IntWritable>{

	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		context.write(new Text("count") , new IntWritable(1));
	}
}
