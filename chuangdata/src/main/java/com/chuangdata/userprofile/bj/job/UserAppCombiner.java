package com.chuangdata.userprofile.bj.job;

import com.chuangdata.userprofile.job.Metrics;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author luxiaofeng
 */
public class UserAppCombiner extends Reducer<UserAppKeyModel, Metrics, UserAppKeyModel, Metrics> {

    @Override
    public void reduce(UserAppKeyModel key, Iterable<Metrics> value, Context context) throws IOException, InterruptedException {
        Metrics outModel = new Metrics();
        for (Metrics val : value) {
            outModel.add(val);
        }
        context.write(key, outModel);
    }
}
