package com.oldboy.hadoop.mr.fulljoin.map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 */
public class App {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("fulljoin-mapper");
		job.setJarByClass(App.class);

		job.setMapperClass(JoinMapper.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.getConfiguration().set(JoinMapper.CUST_FILE_PATH , "file:///d:\\mr\\cust.txt");

		FileInputFormat.addInputPath(job, new Path("file:///D:\\mr\\orders.txt"));
		FileOutputFormat.setOutputPath(job, new Path("file:///D:\\mr\\out"));

		job.waitForCompletion(true);
	}
}
