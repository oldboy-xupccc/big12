package com.oldboy.hadoop.mr.fulljoin.reduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by Administrator on 2018/9/21.
 */
public class App {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);
		job.setJobName("fulljoinreduce");
		job.setJarByClass(App.class);

		job.setMapperClass(JoinMapper2.class);
		job.setReducerClass(JoinReducer2.class);

		job.setMapOutputKeyClass(ComboKey.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		job.setNumReduceTasks(3);

		//设置分区类
		job.setPartitionerClass(CIDPartitioner.class);
		//设置排序对比器
		job.setSortComparatorClass(ComboKey2SortComparator.class);
		//分组对比器
		job.setGroupingComparatorClass(CIDGroupComparator.class);

		FileInputFormat.addInputPath(job, new Path("file:///D:\\mr\\fulljoin"));
		FileOutputFormat.setOutputPath(job, new Path("file:///D:\\mr\\out"));

		job.waitForCompletion(true);
	}
}
