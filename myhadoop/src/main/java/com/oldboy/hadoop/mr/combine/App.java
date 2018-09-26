package com.oldboy.hadoop.mr.combine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("countMR");
		job.setJarByClass(App.class);

		job.setMapperClass(CountMapper.class);
		job.setReducerClass(CountReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		//设置组合文件输入格式，放置大量小文件。
		job.setInputFormatClass(CombineTextInputFormat.class);
		//自定义路径过滤
//		CombineTextInputFormat.setInputPathFilter(job , MyPathFilter.class);
		CombineTextInputFormat.getMaxSplitSize(job) ;
		CombineTextInputFormat.setMaxInputSplitSize(job , 700);

		job.setNumReduceTasks(1);

		FileInputFormat.addInputPath(job, new Path("file:///d:/mr/data"));
		FileOutputFormat.setOutputPath(job, new Path("file:///d:/mr/data/out"));

		job.waitForCompletion(true);
	}
}
