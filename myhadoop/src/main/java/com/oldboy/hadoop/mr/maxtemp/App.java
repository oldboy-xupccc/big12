package com.oldboy.hadoop.mr.maxtemp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2018/9/20.
 */
public class App {
	public static void main(String[] args) throws Exception {
		if(args == null || args.length < 2){
			throw new Exception("参数不足!") ;
		}
		Configuration conf = new Configuration();
		//递归删除
		FileSystem fs = FileSystem.get(conf);
		Path p = new Path(args[1]) ;
		if(fs.exists(p)){
			fs.delete(new Path(args[1]), true);
		}

		Job job = Job.getInstance(conf) ;
		job.setJobName("maxTemp");
		job.setJarByClass(App.class);

		job.setMapperClass(MaxTempMapper.class);
		job.setReducerClass(MaxTempReducer.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setNumReduceTasks(5);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job , new Path(args[0]));
		FileOutputFormat.setOutputPath(job , new Path(args[1]) );

		job.waitForCompletion(true) ;
	}
}
