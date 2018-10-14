package com.chuangdata.userprofile.gd.test;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.chuangdata.userprofile.job.JobDriver;

public class GuangdongTestJobDriver extends JobDriver {

	public GuangdongTestJobDriver(String[] args) {
		super(args);
	}
	
	@Override
	protected int run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, URISyntaxException {
		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), GuangdongTestJobDriver.class.getSimpleName());
	    job.setJarByClass(GuangdongTestJobDriver.class);
	    job.setMapperClass(GuangdongTestMapper.class);
	    job.setNumReduceTasks(0);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));

	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new GuangdongTestJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
