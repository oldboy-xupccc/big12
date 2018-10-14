package com.chuangdata.userprofile.sh.job;

import com.chuangdata.framework.encrypt.RSAEncrypt;
import com.chuangdata.userprofile.job.BaseReducer;
import com.chuangdata.userprofile.job.CDPIKeyModel;
import com.chuangdata.userprofile.job.KeyModel;
import com.chuangdata.userprofile.job.Metrics;
import com.chuangdata.userprofile.utils.Strings;
import org.apache.log4j.Logger;

/**
 * @author luxiaofeng
 */
public class CDPIReducer extends BaseReducer {

    private static final Logger LOG = Logger.getLogger(CDPIReducer.class);

    public void reduce(KeyModel key, Iterable<Metrics> value, Context context) {
        Metrics outModel = new Metrics();
        for (Metrics val : value) {
            outModel.add(val);
        }
        if (lastTime == null) {
            lastTime = key.getTimeStamp().get();
            setKeyAndValue(key, outModel);
        } else {
            try {
                if (lastTime != key.getTimeStamp().get()) {
                    // 1. write
                    encryptAndWrite(context);
                    // and also write count for lastTime
                    outKey.set(String.valueOf(lastTime));
                    outValue.set(String.valueOf(index + 1)); // how many records output with the key of lastTime
                    context.write(outKey, outValue);
                    count = 0;
                    index = 0;

                    // 2. set lasttime
                    lastTime = key.getTimeStamp().get();

                    // 3. set new key and value
                    setKeyAndValue(key, outModel);
                } else {
                    // append value
                    appendValue(key, outModel, context);
                }
            } catch (Exception e) {
                LOG.error("Encrypt and write K/V error", e);
            }
        }
    }

    protected String buildValue(KeyModel key, Metrics value) {
        StringBuilder builder = new StringBuilder();
        String val = outValue.toString();
        if (Strings.isNotEmpty(val)) {
            builder.append(val).append("\001");
        }
        try {
            builder.append(toString(key)).append(separator);
            builder.append(value.toString(separator));
        } catch (Exception e) {
            // do nothing
        }
        return builder.toString();
    }

    protected String toString(KeyModel key) throws Exception {
        CDPIKeyModel keyModel = (CDPIKeyModel) key;
        StringBuilder builder = new StringBuilder();
        builder.append(keyModel.getImei()).append(separator);
        builder.append(keyModel.getTimeStamp()).append(separator);
        builder.append(keyModel.getHost()).append(separator); // should be an ID
        builder.append(keyModel.getRefererHost()).append(separator); // should be an ID
        builder.append(keyModel.getOs()).append(separator); // should be an ID
        builder.append(keyModel.getTerminal()).append(separator); // should be an ID
        builder.append(keyModel.getTerminalProducer()).append(separator); // should be an ID
        builder.append(keyModel.getBrowser()).append(separator); // should be an ID
        builder.append(keyModel.getCookie()).append(separator); // should empty
//		builder.append(userIp).append(separator); // don't output
        builder.append(keyModel.getServerIp()).append(separator);
        builder.append(keyModel.getAppId()).append(separator);
        builder.append(keyModel.getAppActionId()).append(separator);
        builder.append(isUrlParamEncrypt() ? RSAEncrypt.encrypt(getRSAPublicKey(), keyModel.getUrlParams().toString()) : keyModel.getUrlParams()).append(separator); // always encrypt url params
        builder.append(keyModel.getOtherParams());
        return builder.toString();
    }
}
