package com.chuangdata.userprofile.js.job;

import com.chuangdata.framework.encrypt.RSAEncrypt;
import com.chuangdata.userprofile.job.BaseReducer;
import com.chuangdata.userprofile.job.KeyModel;

/**
 * @author luxiaofeng
 */
public class GDPIReducer extends BaseReducer {

    protected String toString(KeyModel key) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(key.getLogTypeId()).append(separator);
        builder.append(key.getUserId()).append(separator);
        builder.append(key.getTimeStamp()).append(separator);
        builder.append(key.getHost()).append(separator); // should be an ID
        builder.append(key.getRefererHost()).append(separator); // should be an ID
        builder.append(key.getOs()).append(separator); // should be an ID
        builder.append(key.getTerminal()).append(separator); // should be an ID
        builder.append(key.getTerminalProducer()).append(separator); // should be an ID
        builder.append(key.getBrowser()).append(separator); // should be an ID
//		builder.append(key.getCookie()).append(separator); // should empty // Do not output cookie for jiangsu
//		builder.append(userIp).append(separator); // don't output
        builder.append(key.getServerIp()).append(separator);
        builder.append(key.getAppId()).append(separator);
        builder.append(key.getAppActionId()).append(separator);
        builder.append(isUrlParamEncrypt() ? RSAEncrypt.encrypt(getRSAPublicKey(), key.getUrlParams().toString()) : key.getUrlParams()).append(separator); // always encrypt url params
        builder.append(key.getOtherParams());
        return builder.toString();
    }
}
