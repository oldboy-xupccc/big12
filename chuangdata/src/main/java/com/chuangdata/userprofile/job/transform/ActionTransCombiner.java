package com.chuangdata.userprofile.job.transform;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by Lucas on 2016/12/30.
 */
public class ActionTransCombiner extends Reducer<Text, IntWritable,Text, IntWritable> {

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        super.reduce(key, values, context);

        int value =  0;
        for(IntWritable val:values){
            value+=val.get();
        }

        context.write(key,new IntWritable(value));

    }
}
