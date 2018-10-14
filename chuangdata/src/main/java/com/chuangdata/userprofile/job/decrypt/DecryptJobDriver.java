package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.userprofile.job.JobDriver;
import com.chuangdata.userprofile.job.extract.ExtractReducer;
import com.chuangdata.userprofile.model.MultipleModel;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcNewOutputFormat;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

public class DecryptJobDriver extends JobDriver {

	public DecryptJobDriver(String[] args) {
		super(args);
	}

	@Override
	protected int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		// 1. set distributed cache
		setDistributedCache(args[2], "chuangdata_log_config");
		setDistributedCache(args[4], "chuangdata_encrypt_private_key_file");
		setDistributedCache(args[5], "chuangdata_resource_app_host");
		setDistributedCache(args[6], "chuangdata_resource_app_action");
		setDistributedCache(args[7], "chuangdata_resource_app_param");
		
		// 2. set specific property
		setProperty("chuangdata.log.config", "chuangdata_log_config");
		setProperty("chuangdata.encrypt.private.key.file", "chuangdata_encrypt_private_key_file");
		setProperty("chuangdata.dmu.userprofile.app.host", "chuangdata_resource_app_host");
		setProperty("chuangdata.dmu.userprofile.app.action", "chuangdata_resource_app_action");
		setProperty("chuangdata.dmu.userprofile.app.param", "chuangdata_resource_app_param");

		setProperty("userprofile.result.hive.path",args[11]);
//
//
//		setProperty("chuangdata.dmu.userprofile.result.encrypted", "false");
//		setProperty("chuangdata.dmu.userprofile.result.url.encrypted", "false");
		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), DecryptJobDriver.class.getSimpleName());
	    job.setJarByClass(DecryptJobDriver.class);


	    job.setMapperClass(DecryptMapper.class);
	    job.setReducerClass(ExtractReducer.class);
		// map
		job.setInputFormatClass(TextInputFormat.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(MultipleModel.class);

		//reduce
		job.setOutputFormatClass(OrcNewOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Writable.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[10]));

	    return job.waitForCompletion(true) ? 0 : 1;
	}

	/**
	 * TODO Run this main method directly is not recommended, we may need a job
	 * scheduler to schedule hourly/daily jobs
	 * 
	 * In production, the job scheduler can be Oozie
	 * 
	 * @param args
	 *     0 input file path
	 *     1 output file path
	 *     2 traffic_destination resource path
	 *     3 top hosts resource path
	 *     4 isUA
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new DecryptJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
