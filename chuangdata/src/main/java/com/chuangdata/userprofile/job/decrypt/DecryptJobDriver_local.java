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
import com.chuangdata.userprofile.job.extract.ExtractReducer_local;

import java.io.IOException;
import java.net.URISyntaxException;

public class DecryptJobDriver_local extends JobDriver {

	public DecryptJobDriver_local(String[] args) {
		super(args);
	}

	@Override
	protected int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

		setProperty("chuangdata.log.config", "D:\\WorkSpace\\inputdata\\bj\\resource\\bj_ct.json");
		setProperty("chuangdata.encrypt.private.key.file", "D:\\WorkSpace\\inputdata\\bj\\resource\\privateKey.keystore");
		setProperty("chuangdata.dmu.userprofile.app.host", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file1");
		setProperty("chuangdata.dmu.userprofile.app.action", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file2");
		setProperty("chuangdata.dmu.userprofile.app.param", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file3");

		setProperty("userprofile.result.hive.path","D:\\WorkSpace\\inputdata\\bj\\output\\hive");

//		setProperty("chuangdata.dmu.userprofile.result.encrypted", "false");
//		setProperty("chuangdata.dmu.userprofile.result.url.encrypted", "false");
		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), DecryptJobDriver_local.class.getSimpleName());
	    job.setJarByClass(DecryptJobDriver_local.class);


	    job.setMapperClass(DecryptMapper.class);
	    job.setReducerClass(ExtractReducer_local.class);
		// map
		job.setInputFormatClass(TextInputFormat.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(MultipleModel.class);

		//reduce
		job.setOutputFormatClass(OrcNewOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Writable.class);


		FileInputFormat.addInputPath(job, new Path("D:\\WorkSpace\\inputdata\\bj\\input\\decrypt"));
	    FileOutputFormat.setOutputPath(job, new Path("D:\\WorkSpace\\inputdata\\bj\\output\\hive"));

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
        JobDriver jobDriver = new DecryptJobDriver_local(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
