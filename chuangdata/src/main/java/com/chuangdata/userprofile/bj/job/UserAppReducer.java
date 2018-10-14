package com.chuangdata.userprofile.bj.job;

import com.chuangdata.framework.encrypt.RSAEncrypt;
import com.chuangdata.userprofile.job.Metrics;
import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.security.interfaces.RSAPublicKey;

/**
 * @author luxiaofeng
 */
public class UserAppReducer extends Reducer<UserAppKeyModel, Metrics, Text, Text> {
    private static final Logger LOG = Logger.getLogger(UserAppReducer.class);
    protected String separator = "|";

    protected Text outKey = new Text();
    protected Text outValue = new Text();
    private RSAPublicKey rsaPublicKey;
    private boolean encrypt = true;

    public void setup(Context context) {
        Configuration conf = context.getConfiguration();
        String publicKeyFile = conf.get("chuangdata.encrypt.public.key.file");
        try {
            rsaPublicKey = RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicKeyFile));
        } catch (Exception e) {
            LOG.error("Init public key error: ", e);
        }
        encrypt = Boolean.parseBoolean(conf.get("chuangdata.dmu.userprofile.result.encrypted", "true"));
    }

    @VisibleForTesting
    protected void setRSAPublicKey(RSAPublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    @Override
    public void reduce(UserAppKeyModel key, Iterable<Metrics> value, Context context) {
        Metrics outModel = new Metrics();
        for (Metrics val : value) {
            outModel.add(val);
        }
        outKey.set(key.toString(separator));
        outValue.set(outModel.toString(separator));
        try {
            encryptAndWrite(context);
        } catch (Exception e) {
            LOG.error("Encrypt and write last K/V error", e);
        }
    }

    protected void encryptAndWrite(Context context) throws Exception {
        String encrypted = encrypt ? RSAEncrypt.encrypt(rsaPublicKey, outValue.toString()) : outValue.toString();
        outValue.set(encrypted);
        context.write(outKey, outValue);
    }

}
