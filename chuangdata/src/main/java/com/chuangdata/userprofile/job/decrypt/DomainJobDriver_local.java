package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.userprofile.job.JobDriver;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcNewOutputFormat;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.chuangdata.userprofile.job.extract.ExtractDomainReduce;
import java.io.IOException;
import java.net.URISyntaxException;

public class DomainJobDriver_local extends JobDriver {

	public DomainJobDriver_local(String[] args) {
		super(args);
	}

	@Override
	protected int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		// 1. set distributed cache
//		setDistributedCache(args[2], "chuangdata.dmu.userprofile.app.domain");
		
		// 2. set specific property
//        setProperty("chuangdata.dmu.userprofile.app.domain", "chuangdata_resource_app_domain");
//		setProperty("chuangdata.encrypt.private.key.file", "chuangdata.encrypt.private.key.file");
//		setProperty("chuangdata.dmu.userprofile.log.day", "chuangdata.dmu.userprofile.log.day");
//		setProperty("userprofile.result.hive.path","userprofile.result.hive.path");

		setProperty("chuangdata.dmu.userprofile.app.domain", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file4");
        setProperty("chuangdata.encrypt.private.key.file", "D:\\WorkSpace\\inputdata\\bj\\resource\\privateKey.keystore");
		setProperty("chuangdata.dmu.userprofile.log.day", "2017-03-15 00:00:00");
		setProperty("userprofile.result.hive.path","D:\\WorkSpace\\inputdata\\bj\\output\\hive\\day");

		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), DomainJobDriver_local.class.getSimpleName());
	    job.setJarByClass(DomainJobDriver_local.class);

	    job.setMapperClass(UserAppDecryptMapper.class);
		job.setReducerClass(ExtractDomainReduce.class);

		// map
		job.setInputFormatClass(TextInputFormat.class);
	    job.setMapOutputKeyClass(NullWritable.class);
	    job.setMapOutputValueClass(Text.class);

		job.setOutputFormatClass(OrcNewOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Writable.class);


		FileInputFormat.addInputPath(job, new Path("D:\\WorkSpace\\inputdata\\bj\\mr\\day"));
		FileOutputFormat.setOutputPath(job, new Path("D:\\\\WorkSpace\\\\inputdata\\\\bj\\\\output"));

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
        JobDriver jobDriver = new DomainJobDriver_local(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
