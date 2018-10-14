package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.userprofile.job.JobDriver;
import com.chuangdata.userprofile.job.extract.ExtractDomainReduce;
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

public class DomainJobDriver extends JobDriver {

	public DomainJobDriver(String[] args) {
		super(args);
	}

	@Override
	protected int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

		// 1. set distributed cache
		setDistributedCache(args[9], "chuangdata_encrypt_private_key_file");
		setDistributedCache(args[7], "chuangdata_resource_app_domain");

		// 2. set specific property
        setProperty("chuangdata.dmu.userprofile.app.domain", "chuangdata_resource_app_domain");
		setProperty("chuangdata.encrypt.private.key.file", "chuangdata_encrypt_private_key_file");
		setProperty("chuangdata.dmu.userprofile.log.day", args[10]);

//		setProperty("chuangdata.dmu.userprofile.app.domain", "D:/test/is_special/resource/in/app_domain.csv");
//        setProperty("chuangdata.encrypt.private.key.file", "D:/test/is_special/resource/privateKey.keystore");
//
//		setProperty("chuangdata.dmu.userprofile.log.day", "2016-12-08");
//		setProperty("userprofile.result.hive.path","/isp=ct/collect_env=ps/province=zj/log_day=2016-12-08");

		
		// 3. set Job
	    Job job = Job.getInstance(getConfiguration(), DomainJobDriver.class.getSimpleName());
	    job.setJarByClass(DomainJobDriver.class);

		job.setMapperClass(UserAppDecryptMapper.class);
		job.setReducerClass(ExtractDomainReduce.class);

		// map
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputFormatClass(OrcNewOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Writable.class);


		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[8]));

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
        JobDriver jobDriver = new DomainJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
	}

}
