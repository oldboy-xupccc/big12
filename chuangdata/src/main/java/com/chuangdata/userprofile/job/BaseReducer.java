package com.chuangdata.userprofile.job;

import java.security.interfaces.RSAPublicKey;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import com.chuangdata.framework.encrypt.RSAEncrypt;
import com.chuangdata.userprofile.utils.Strings;

import com.google.common.annotations.VisibleForTesting;

public class BaseReducer extends Reducer<KeyModel, Metrics, Text, Text>{
	private static final Logger LOG = Logger.getLogger(BaseReducer.class);
	protected String separator = "|";
	
	protected Long lastTime = null;
	protected Text outKey = new Text();
	protected Text outValue = new Text();
	protected long index = 0;
	protected long count = 0;
	protected long max_count = 5;
	protected long max_len = 4096;

	private RSAPublicKey rsaPublicKey;
	private boolean encrypt = true;
	private boolean urlParamEncrypt = true;
	
	public void setup(Context context) {
		Configuration conf = context.getConfiguration();
		String maxCountStr = conf.get("chuangdata.public.reduce.value.max.count", "5");
		String maxLenStr = conf.get("chuangdata.public.reduce.value.max.len", "8192");
		max_count = Long.parseLong(maxCountStr);
		max_len = Long.parseLong(maxLenStr);
		
		String publicKeyFile = conf.get("chuangdata.encrypt.public.key.file");

		try {
			rsaPublicKey = RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(publicKeyFile));
		} catch (Exception e) {
			LOG.error("Init public key error: ", e);
		}
		encrypt = Boolean.parseBoolean(conf.get("chuangdata.dmu.userprofile.result.encrypted", "true")); // should be always true
		urlParamEncrypt = Boolean.parseBoolean(conf.get("chuangdata.dmu.userprofile.result.url.encrypted", "true")); // should by always true
	}
	
	protected RSAPublicKey getRSAPublicKey() {
		return rsaPublicKey;
	}
	
	protected boolean isUrlParamEncrypt() {
		return urlParamEncrypt;
	}

	@VisibleForTesting
	protected void setRSAPublicKey(RSAPublicKey rsaPublicKey) {
		this.rsaPublicKey = rsaPublicKey;
	}

    @Override
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
    
    public void cleanup(Context context) {
    	// write last key and value
    	try {
    	    encryptAndWrite(context);
    	} catch (Exception e) {
    		LOG.error("Encrypt and write last K/V error", e);
    	}
    }
    
    protected void setKeyAndValue(KeyModel key, Metrics value) {
    	// FIXME the first value should not be longer than max_len
    	outKey.set(String.valueOf(lastTime) + getIndex());
    	outValue.clear();
		outValue.set(buildValue(key, value));
		count++;
    }
    
    protected String buildValue(KeyModel key, Metrics value) {
    	StringBuilder builder = new StringBuilder();
    	String val = outValue.toString();
    	if (Strings.isNotEmpty(val)) {
    		builder.append(val).append("\n"); // FIXME so we need to encrypt the output record
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
		builder.append(key.getCookie()).append(separator); // should empty
//		builder.append(userIp).append(separator); // don't output
		builder.append(key.getServerIp()).append(separator);
		builder.append(key.getAppId()).append(separator);
		builder.append(key.getAppActionId()).append(separator);
		builder.append(urlParamEncrypt ? RSAEncrypt.encrypt(rsaPublicKey, key.getUrlParams().toString()) : key.getUrlParams()).append(separator); // always encrypt url params
		builder.append(key.getOtherParams()); // should only nai
		return builder.toString();
	}
    
    protected void appendValue(KeyModel key, Metrics value, Context context) throws Exception {
    	String val = encryptIfNeeded(context, buildValue(key, value)); // already added the new K/V
    	if (count >= max_count || val.length() >= max_len) {
    		encryptAndWrite(context);
    		count = 0;
    		index++;
    		outKey.set(String.valueOf(lastTime) + getIndex());
    		outValue.clear();
    	}
    	outValue.set(buildValue(key, value));
    	count++;
    }

    private String getIndex() {
    	if (index < 10) {
    		return "0000" + String.valueOf(index);
    	} else if (index >= 10 && index < 100) {
    		return "000" + String.valueOf(index);
    	} else if (index >= 100 && index < 1000) {
    		return "00" + String.valueOf(index);
    	} else if (index >= 1000 && index < 10000) {
    		return "0" + String.valueOf(index);
    	}
        return String.valueOf(index);
    }
    
    protected String encryptIfNeeded(Context context, String value) throws Exception {
    	String encrypted = encrypt ? RSAEncrypt.encrypt(rsaPublicKey, value) : value;
    	return encrypted;
    }
    
    protected void encryptAndWrite(Context context) throws Exception {
    	String encrypted = encrypt ? RSAEncrypt.encrypt(rsaPublicKey, outValue.toString()) : outValue.toString();
    	outValue.set(encrypted);
    	context.write(outKey, outValue);
    }
}
