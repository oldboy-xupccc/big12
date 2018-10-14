package com.chuangdata.userprofile.job.extract;

import com.chuangdata.userprofile.job.JobDriver;
import com.chuangdata.userprofile.model.MultipleModel;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * changed by 
 * @author luxiaofeng
 * @date 2016-06-16 
 *
 */
public class ExtractJobDriver extends JobDriver {

	public ExtractJobDriver(String[] args) {
		super(args);
	}
	
	@Override
	protected int run(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, URISyntaxException {
		// 1. set distributed cache
		setDistributedCache(args[2], "chuangdata_imc_userprofile_app_host");
		setDistributedCache(args[3], "chuangdata_imc_userprofile_app_action");
		setDistributedCache(args[4], "chuangdata_imc_userprofile_app_param");
		setDistributedCache(args[5], "chuangdata_imc_userprofile_app_domain");
		setDistributedCache(args[6], "chuangdata_log_config");
		
		
		// 2. set specific property
//		setProperty("mapreduce.output.textoutputformat.separator", "\t"); // set it in the run.sh
		setProperty("chuangdata.dmu.userprofile.app.host", "chuangdata_imc_userprofile_app_host");
		setProperty("chuangdata.dmu.userprofile.app.action", "chuangdata_imc_userprofile_app_action");
		setProperty("chuangdata.dmu.userprofile.app.param", "chuangdata_imc_userprofile_app_param");
		setProperty("chuangdata.dmu.userprofile.app.domain", "chuangdata_imc_userprofile_app_domain");
		setProperty("chuangdata.log.config", "chuangdata_log_config");
		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), ExtractJobDriver.class.getSimpleName());
	    job.setJarByClass(ExtractJobDriver.class);
	    job.setMapperClass(ExtractMapper.class);
	    // TODO COMBINER
	    job.setReducerClass(ExtractReducer.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(MultipleModel.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    job.setInputFormatClass(TextInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));

	    return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new ExtractJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
