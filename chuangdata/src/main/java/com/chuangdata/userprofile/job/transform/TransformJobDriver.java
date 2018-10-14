package com.chuangdata.userprofile.job.transform;

import com.chuangdata.userprofile.job.JobDriver;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.orc.OrcNewInputFormat;
import org.apache.hadoop.hive.ql.io.orc.OrcNewOutputFormat;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Lucas on 2016/12/21.
 */
public class TransformJobDriver extends JobDriver {
    public TransformJobDriver(String[] args) {
        super(args);
    }

    /**
     *
     * @param args
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Override
    protected int run(String[] args) throws IOException,
            ClassNotFoundException, InterruptedException, URISyntaxException {
        // 1. set distributed cache
        setDistributedCache(args[5], "chuangdata_resource_app_host");
        setDistributedCache(args[6], "chuangdata_resource_app_action");
        setDistributedCache(args[9], "chuangdata.dmu.userprofile.tag.resource");

        // 2. set specific property
        setProperty("chuangdata.dmu.userprofile.tag.resource", "chuangdata.dmu.userprofile.tag.resource");
        setProperty("chuangdata.dmu.userprofile.app.host", "chuangdata_resource_app_host");
        setProperty("chuangdata.dmu.userprofile.app.action", "chuangdata_resource_app_action");

        // 3. set Job
        Job job = Job.getInstance(getConfiguration(), TransformJobDriver.class.getSimpleName());
        job.setJarByClass(TransformJobDriver.class);
        job.setInputFormatClass(OrcNewInputFormat.class);
        job.setMapperClass(ActionTransMapper.class);
        job.setCombinerClass(ActionTransCombiner.class);
        job.setReducerClass(ActionTransReduce.class);

        //Input
        //MultipleInputs.addInputPath(job, new Path(args[0]), OrcNewInputFormat.class, ActionTransMapper.class);
        FileInputFormat.addInputPath(job, new Path(args[12]));


        // map
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        //reduce
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Writable.class);

        //Output
        job.setOutputFormatClass(OrcNewOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[13]));


        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new TransformJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
    }

}
