package com.chuangdata.userprofile.sh.job;

import com.chuangdata.userprofile.job.CDPIKeyModel;
import com.chuangdata.userprofile.job.JobDriver;
import com.chuangdata.userprofile.job.Metrics;
import com.chuangdata.userprofile.job.TimePartitioner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;

public class CDPIJobDriver extends JobDriver {
    private static final Logger LOG = Logger.getLogger(CDPIJobDriver.class);

    public CDPIJobDriver(String[] args) {
        super(args);
    }

    private String getMEIDResource(String input) throws Exception {
        Configuration conf = getConfiguration();
        FileSystem hdfs = FileSystem.get(conf);
        Path inputPath = new Path(input);
        FileStatus files[] = hdfs.listStatus(inputPath);
        if (files == null) {
            throw new Exception("MEID Resource File Does Not Exist");
        }
        FileStatus latest = null;
        for (FileStatus file : files) {
            if (latest == null || latest.isFile()) {
                latest = file;
            } else {
                if (file.isDirectory()) {
                    Long fileDate = Long.parseLong(file.getPath().getName());
                    Long latestDate = Long.parseLong(latest.getPath().getName());
                    if (fileDate > latestDate) {
//            	    	if (hdfs.exists(new Path(file.getPath() + "/00/_SUCCESS")))
                        latest = file;
                    }
                }
            }
        }
        if (latest == null) {
            throw new Exception("MEID Resource File Does Not Exist");
        }
        FileStatus resources[] = hdfs.listStatus(new Path(latest.getPath().toString() + "/00"));
        if (resources == null) {
            throw new Exception("MEID Resource File Does Not Exist");
        }
        if (resources.length >= 1) {
            // actually it should be 1
            return resources[0].getPath().toString();
        }
        throw new Exception("MEID Resource File Does Not Exist");
    }

    @Override
    protected int run(String[] args) throws Exception {
        // 1. set distributed cache
        setDistributedCache(args[2], "chuangdata_log_config");
        setDistributedCache(args[3], "chuangdata_encrypt_public_key_file");
        setDistributedCache(args[4], "chuangdata_resource_app_host");
        setDistributedCache(args[5], "chuangdata_resource_app_action");
        setDistributedCache(args[6], "chuangdata_resource_app_param");
        setDistributedCache(args[7], "chuangdata_resource_app_domain");

        // 2. set specific property
        setProperty("chuangdata.log.config", "chuangdata_log_config");
        setProperty("chuangdata.encrypt.public.key.file", "chuangdata_encrypt_public_key_file");
        setProperty("chuangdata.dmu.userprofile.app.host", "chuangdata_resource_app_host");
        setProperty("chuangdata.dmu.userprofile.app.action", "chuangdata_resource_app_action");
        setProperty("chuangdata.dmu.userprofile.app.param", "chuangdata_resource_app_param");
        setProperty("chuangdata.dmu.userprofile.app.domain", "chuangdata_resource_app_domain");

        // args[7] should be meid resource input path
        String meidResourceFile = getMEIDResource(args[8]);
        LOG.info("Get MEID Resource: " + meidResourceFile);
        if (meidResourceFile.endsWith(".gz")) {
            setDistributedCache(meidResourceFile, "chuangdata_resource_sh_meid.gz");
            setProperty("chuangdata.dmu.userprofile.sh.meid", "chuangdata_resource_sh_meid.gz");
        } else {
            setDistributedCache(meidResourceFile, "chuangdata_resource_sh_meid");
            setProperty("chuangdata.dmu.userprofile.sh.meid", "chuangdata_resource_sh_meid");
        }

        // 3. set Job
        Job job = Job.getInstance(getConfiguration(), CDPIJobDriver.class.getSimpleName());
        job.setJarByClass(CDPIJobDriver.class);
        job.setMapperClass(CDPIMapper.class);
        job.setReducerClass(CDPIReducer.class);
        job.setPartitionerClass(TimePartitioner.class);
        job.setMapOutputKeyClass(CDPIKeyModel.class);
        job.setMapOutputValueClass(Metrics.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
     * TODO Run this main method directly is not recommended, we may need a job
     * scheduler to schedule hourly/daily jobs
     * <p>
     * In production, the job scheduler can be Oozie
     *
     * @param args 0 input file path
     *             1 output file path
     *             2 traffic_destination resource path
     *             3 top hosts resource path
     *             4 isUA
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new CDPIJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
    }
}
