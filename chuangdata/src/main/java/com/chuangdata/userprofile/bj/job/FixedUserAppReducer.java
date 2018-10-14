package com.chuangdata.userprofile.bj.job;

import com.chuangdata.userprofile.job.Metrics;
import org.apache.log4j.Logger;

/**
 * @author luxiaofeng
 */
public class FixedUserAppReducer extends UserAppReducer {
    private static final Logger LOG = Logger.getLogger(FixedUserAppReducer.class);

    @Override
    public void reduce(UserAppKeyModel key, Iterable<Metrics> value, Context context) {
        Metrics outModel = new Metrics();
        for (Metrics val : value) {
            outModel.add(val);
        }
        outKey.set(key.toString(separator));
        outValue.set(String.valueOf(outModel.getLogCount()));
        try {
            encryptAndWrite(context);
        } catch (Exception e) {
            LOG.error("Encrypt and write last K/V error", e);
        }
    }
}
