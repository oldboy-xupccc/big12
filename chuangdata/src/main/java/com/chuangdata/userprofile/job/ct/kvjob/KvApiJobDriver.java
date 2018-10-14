package com.chuangdata.userprofile.job.ct.kvjob;

import com.chuangdata.userprofile.job.JobDriver;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author luxiaofeng
 */
public class KvApiJobDriver extends JobDriver {

    public KvApiJobDriver(String[] args) {
        super(args);
    }

    @Override
    protected int run(String[] args) throws IOException,
            ClassNotFoundException, InterruptedException, URISyntaxException {
        // 1. set distributed cache
        setDistributedCache(args[2], "chuangdata.dmu.userprofile.tag.resource");

        // 2. set specific property
        setProperty("chuangdata.dmu.userprofile.tag.resource", "chuangdata.dmu.userprofile.tag.resource");
        // 3. set Job
        Job job = Job.getInstance(getConfiguration(), KvApiJobDriver.class.getSimpleName());
        job.setJarByClass(KvApiJobDriver.class);

        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, KvAppMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, KvAppAactionMapper.class);
        // TODO COMBINER
        job.setReducerClass(KvReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(ValueGenericWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[3]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        JobDriver jobDriver = new KvApiJobDriver(args);
        int result = jobDriver.run();
        System.exit(result);
    }


}
