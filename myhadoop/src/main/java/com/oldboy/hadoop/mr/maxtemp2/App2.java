package com.oldboy.hadoop.mr.maxtemp2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by Administrator on 2018/9/21.
 */
public class App2 {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("maxTemp2");
		job.setJarByClass(App2.class);

		job.setMapperClass(MaxtempMapper2.class);
		job.setReducerClass(MaxtempReducer2.class);

		FileInputFormat.setMinInputSplitSize(job , 44 * 1000);
		FileInputFormat.setMaxInputSplitSize(job, 44 * 1000);

		job.setMapOutputKeyClass(ComboKey.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(3);

		//设置分区类
		job.setPartitionerClass(YearPartitioner.class);
		//设置排序对比器
		job.setSortComparatorClass(ComkeySortComparator.class);
		//分组对比器
		job.setGroupingComparatorClass(YearGroupComparator.class);


		FileInputFormat.addInputPath(job, new Path("file:///D:\\mr\\temp3.dat"));
		FileOutputFormat.setOutputPath(job, new Path("file:///D:\\mr\\out"));

		job.waitForCompletion(true);
	}
}
