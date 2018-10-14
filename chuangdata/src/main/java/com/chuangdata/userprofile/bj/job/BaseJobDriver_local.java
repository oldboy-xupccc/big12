package com.chuangdata.userprofile.bj.job;

import com.chuangdata.userprofile.job.*;
import com.chuangdata.userprofile.job.decrypt.DecryptJobDriver_local;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcNewInputFormat;
import java.io.IOException;
import java.net.URISyntaxException;
import com.chuangdata.userprofile.job.transform.TransformJobDriver_local ;


public class BaseJobDriver_local extends JobDriver {

    public BaseJobDriver_local(String[] args) {
        super(args);
    }

    @Override
    protected int run(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        // 1. set distributed cache
//        setDistributedCache(args[2], "chuangdata_log_config");
//        setDistributedCache(args[3], "chuangdata_encrypt_public_key_file");
//        setDistributedCache(args[4], "chuangdata_resource_app_host");
//        setDistributedCache(args[5], "chuangdata_resource_app_action");
//        setDistributedCache(args[6], "chuangdata_resource_app_param");
//        setDistributedCache(args[7], "chuangdata_resource_app_domain");

        // 2. set specific property
//        setProperty("chuangdata.log.config", "chuangdata_log_config");
//        setProperty("chuangdata.encrypt.public.key.file", "chuangdata_encrypt_public_key_file");
//        setProperty("chuangdata.dmu.userprofile.app.host", "chuangdata_resource_app_host");
//        setProperty("chuangdata.dmu.userprofile.app.action", "chuangdata_resource_app_action");
//        setProperty("chuangdata.dmu.userprofile.app.param", "chuangdata_resource_app_param");
//        setProperty("chuangdata.dmu.userprofile.app.domain", "chuangdata_resource_app_domain");


        setProperty("chuangdata.log.config", "D:\\WorkSpace\\inputdata\\bj\\resource\\bj_ct.json");
        setProperty("chuangdata.encrypt.public.key.file", "D:\\WorkSpace\\inputdata\\bj\\resource\\publicKey.keystore");
        setProperty("chuangdata.dmu.userprofile.app.host", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file1");
        setProperty("chuangdata.dmu.userprofile.app.action", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file2");
        setProperty("chuangdata.dmu.userprofile.app.param", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file3");
        setProperty("chuangdata.dmu.userprofile.app.domain", "D:\\WorkSpace\\inputdata\\bj\\resource\\resource_file4");

        setProperty("chuangdata.dmu.userprofile.resource.encrypted", "false");
        setProperty("chuangdata.dmu.userprofile.result.encrypted", "false");
        setProperty("chuangdata.dmu.userprofile.result.url.encrypted", "false");
        setProperty("chuangdata.dmu.userprofile.logIndex", "0");

        // 3. set Job
        Job job = Job.getInstance(getConfiguration(), BaseJobDriver_local.class.getSimpleName());
        job.setJarByClass(BaseJobDriver_local.class);
        job.setMapperClass(BaseMapper.class);
        job.setPartitionerClass(TimePartitioner.class);
        job.setReducerClass(BaseReducer.class);
        job.setMapOutputKeyClass(KeyModel.class);
        job.setMapOutputValueClass(Metrics.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(OrcNewInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(job, new Path("D:\\WorkSpace\\inputdata\\bj\\input\\mr"));
        FileOutputFormat.setOutputPath(job, new Path("D:\\WorkSpace\\inputdata\\bj\\output"));

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
        JobDriver jobDriver = new BaseJobDriver_local(args);
        int result = jobDriver.run();
//        if(result==0){
//            DecryptJobDriver_local decryptJobDriver = new DecryptJobDriver_local(args);
//            result = decryptJobDriver.run();
//        }
        System.exit(result);
    }

    static class BaseMapper extends com.chuangdata.userprofile.job.BaseMapper<KeyModel> {
    }




}
