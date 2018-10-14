package com.chuangdata.userprofile.js.job;

import com.chuangdata.framework.encrypt.RSAEncrypt;
import com.chuangdata.userprofile.job.BaseReducer;
import com.chuangdata.userprofile.job.CDPIKeyModel;
import com.chuangdata.userprofile.job.KeyModel;

/**
 * @author luxiaofeng
 */
public class CDPIReducer extends BaseReducer {

    protected String toString(KeyModel key) throws Exception {
        CDPIKeyModel keyModel = (CDPIKeyModel) key;
        StringBuilder builder = new StringBuilder();
        builder.append(keyModel.getLogTypeId()).append(separator);
        builder.append(keyModel.getImei()).append(separator);
        builder.append(keyModel.getTimeStamp()).append(separator);
        builder.append(keyModel.getHost()).append(separator); // should be an ID
        builder.append(keyModel.getRefererHost()).append(separator); // should be an ID
        builder.append(keyModel.getOs()).append(separator); // should be an ID
        builder.append(keyModel.getTerminal()).append(separator); // should be an ID
        builder.append(keyModel.getTerminalProducer()).append(separator); // should be an ID
        builder.append(keyModel.getBrowser()).append(separator); // should be an ID
//		builder.append(keyModel.getCookie()).append(separator); // should empty // Do not output cookie for jiangsu
//		builder.append(userIp).append(separator); // don't output
        builder.append(keyModel.getServerIp()).append(separator);
        builder.append(keyModel.getAppId()).append(separator);
        builder.append(keyModel.getAppActionId()).append(separator);
        builder.append(isUrlParamEncrypt() ? RSAEncrypt.encrypt(getRSAPublicKey(), keyModel.getUrlParams().toString()) : keyModel.getUrlParams()).append(separator); // always encrypt url params
        builder.append(keyModel.getOtherParams()).append(separator); // should only nai
        builder.append(keyModel.getMsisdn());
        return builder.toString();
    }
}
