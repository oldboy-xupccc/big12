package com.chuangdata.userprofile.job.shctdecrypt;

import com.chuangdata.framework.encrypt.transform.IntToInt;
import com.chuangdata.framework.resource.UrlMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.job.decrypt.Decrypter;
import com.chuangdata.userprofile.model.*;
import com.chuangdata.userprofile.utils.Strings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 
 * @author luxiaofeng
 * @desription mapper for ct shanghai
 * 
 */
public class ShCtDecryptMapper extends
		Mapper<Object, Text, Text, MultipleModel> {
	private static final Logger LOG = Logger.getLogger(ShCtDecryptMapper.class);
	private boolean onlyValue = false;
	private boolean isEncrypted = true;
	private Decrypter decrypter;
	private UrlMatcher urlMatcher;
	protected String currentMsisdnFromParam = null;
	private static int MSISDN_PARAM_TYPE_ID = 9;
	private Text outKey = new Text();

	public void setup(Context context) throws IOException {
		Configuration conf = context.getConfiguration();
		String privateKeyFile = conf.get("chuangdata.encrypt.private.key.file");
		try {
			decrypter = new Decrypter(privateKeyFile);
		} catch (Exception e) {
			LOG.error(" fail to inintial private encrypter key");
			e.printStackTrace();
		}
		onlyValue = conf.getBoolean("chuangdata.zj.result.only.value", true);
		isEncrypted = conf.getBoolean("chuangdata.dmu.userprofile.result.encrypted", true);

		String appHostFilePath = conf.get("chuangdata.dmu.userprofile.app.host");
		String appActionFilePath = conf.get("chuangdata.dmu.userprofile.app.action");
		String appParamFilePath = conf.get("chuangdata.dmu.userprofile.app.param");
		String appDomainFilePath = conf.get("chuangdata.dmu.userprofile.app.domain");
		urlMatcher = new UrlMatcher(appHostFilePath, appActionFilePath,
				appParamFilePath, appDomainFilePath, false); // Don't support encrypted resource
											// files.
	}

	public void map(Object object, Text text, Context context)
			throws IOException, InterruptedException {
		String inputStr = text.toString();
		if (!onlyValue) {
			inputStr = inputStr.split("\\t")[1];
		}

		if (inputStr == null || Strings.isEmpty(inputStr)) {
			return;
		}
		try {
			inputStr = isEncrypted ? decrypter.decrypt(inputStr) : inputStr;
			// LOG.info("the decrypt records is "+ inputStr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String[] logs = inputStr.split(ShCtLogFields.RECORD_SEPERTOR);
		for (String log : logs) {
			// LOG.info("the split log is "+ inputStr);
			String[] input = log.split(ShCtLogFields.FIELD_SEPERTOR);
			this.currentMsisdnFromParam = null;
			ExtractMd5EncrypterKeyModel keyModel = buildKey(input);
			MultipleModel valueModel = buildValue(input);
			if(null==valueModel){
				return ;
			}
			if (!valueModel.getSet().isEmpty()) {
				// maybe no action, but domain matches
				if (this.currentMsisdnFromParam != null
						&& !Strings.isNotEmpty(keyModel.getMsisdn())) {
					keyModel.setEncryptedMsisdn(currentMsisdnFromParam);
				}
				
				outKey.set(keyModel.toString());
				context.write(outKey, valueModel);
				
			}
			
			// build domain model
			MultipleModel domainModel  = new MultipleModel();
	        ExtractMd5EncrypterDomainKeyModel extractMd5EncrypterDomainKeyModel = buildDomainKey(input);
	        AppDomainModel appDomainModel = buildDomainModel(input);
	        if (extractMd5EncrypterDomainKeyModel != null && appDomainModel != null && appDomainModel.getAppDomainId().get() > 0) {
	        	if (this.currentMsisdnFromParam != null && !Strings.isNotEmpty(keyModel.getMsisdn())) {
	        		extractMd5EncrypterDomainKeyModel.setMsisdn(currentMsisdnFromParam);
				}
	            domainModel.add(appDomainModel);
	            context.write(new Text(extractMd5EncrypterDomainKeyModel.toString()), domainModel);
	        }
		}
	}
	
	private ExtractMd5EncrypterDomainKeyModel buildDomainKey(String[] input) {
		int app_domain_id = Integer.parseInt(input[ShCtLogFields.APP_ID]); // actually it's app_domain_id
		DomainUnit domainUnit = urlMatcher.getDomainById(app_domain_id);
		if (domainUnit != null) {
			ExtractMd5EncrypterDomainKeyModel key = new ExtractMd5EncrypterDomainKeyModel();
			try {
				key.setTime(Long.valueOf(input[ShCtLogFields.TIME_STAMP]));
			} catch (NumberFormatException e) {
				// DO NOTHING?
			}
			key.setEncryptedImei(input[ShCtLogFields.USER_ID]); // meid 15
			key.setIsClicked(2);
			// we should set sha meid as imsi
			String otherParams = input[ShCtLogFields.OTHER_PARAMS];
			String[] info = otherParams.split("&");
			if (info != null) {
				for (String c : info) {
					String[] cc = c.split("=");
					if (cc != null && cc.length == 2 && cc[0].equals("2")) {
						// sha meid
						key.setEncryptedImsi(cc[1]);
					}
				}
			}
			return key;
		}
		return null;
	}
	
	private AppDomainModel buildDomainModel(String[] input) {
		int app_domain_id = Integer.parseInt(input[ShCtLogFields.APP_ID]); // actually it's app_domain_id
		 AppDomainModel appDomainModel = new AppDomainModel();
    	 DomainUnit domainUnit = urlMatcher.getDomainById(app_domain_id);
         if (domainUnit != null) {
             appDomainModel.setAppDomainId(domainUnit.getId());
             appDomainModel.setHost(domainUnit.getDomain());
             appDomainModel.setLogCount(Long.parseLong(input[ShCtLogFields.LOG_COUNT]));
         }
         return appDomainModel;
	}

	protected ExtractMd5EncrypterKeyModel buildKey(String[] input) {
		ExtractMd5EncrypterKeyModel model = new ExtractMd5EncrypterKeyModel();
		model.setEncryptedImei(input[ShCtLogFields.USER_ID]);
		try {
			model.setTime(Long.valueOf(input[ShCtLogFields.TIME_STAMP]));
		} catch (NumberFormatException e) {
			// DO NOTHING?
		}
		// we should set sha meid as imsi
		String otherParams = input[ShCtLogFields.OTHER_PARAMS];
		String[] info = otherParams.split("&");
		if (info != null) {
			for (String c : info) {
				String[] cc = c.split("=");
				if (cc != null && cc.length == 2 && cc[0].equals("2")) {
					// sha meid
					model.setEncryptedImsi(cc[1]);
				}
			}
		}
		return model;
	}

	protected MultipleModel buildValue(String[] input) {
		MultipleModel value = new MultipleModel();
		int appId = Integer.parseInt(input[ShCtLogFields.HOST]); // host id is the app_host id
		if (appId == -1) {
	        // maybe app_domain_id != -1 but app_host_id == -1
			return value;
		}
		AppUnit appUnit = urlMatcher.getApp(appId);
		AppModel appModel = new AppModel();
		if (appUnit != null) {
			appModel.setAppId(appUnit.getAppId());
			appModel.setAppTypeId(appUnit.getAppTypeId());
			appModel.setLogCount(1L);
		} else {
			// not a valid line, just ignore it
			//throw new InvalidInputException("Can't get app unit by id=" + appId);
            return null;
		}
		if (appModel.getAppId().get() > 0) {
			value.add(appModel);
			long actionId = Long.parseLong(input[ShCtLogFields.ACTION_ID]);
			ActionUnit actionUnit = urlMatcher.getAction(actionId);
			ActionModel actionModel = new ActionModel();
			if (actionUnit != null) {
				actionModel.setId(actionUnit.getId());
				actionModel.setHost(actionUnit.getHost());
				actionModel.setUrlPatternStr(actionUnit.getUrlPatternStr());
				actionModel.setDetailActionId(actionUnit.getDetailActionId());
				actionModel.setActionTypeId(actionUnit.getActionTypeId());
				actionModel.setInterestTag(actionUnit.getInterestTag());
				actionModel.setAppId(actionUnit.getAppId());
				actionModel.setAppTypeId(actionUnit.getAppTypeId());
				actionModel.setLogCount(1L);
			}
			if (actionModel.getId() > 0) {
				value.add(actionModel);
				if (Strings.isNotEmpty(input[ShCtLogFields.URL_PARAMS])) {
					ParamMapModel paramMap = extractParam(input[ShCtLogFields.URL_PARAMS]);
					value.add(paramMap);
				}
			}
		}
		return value;
	}

	private ParamMapModel extractParam(String urlParams) {

		ParamMapModel paramMap = new ParamMapModel();
		String[] params = urlParams.split("&");
		for (String param : params) {
			String[] info = param.split("=");
			if (info != null && info.length == 2) {
				ParamUnit unit = null;
    			if (info[0].contains("\002")) {
    				unit = new ParamUnit();
    				String[] ids = info[0].split("\002");
    				unit.setParam("");
    				unit.setParamValue(info[1]);
    				unit.setActionId(Integer.parseInt(IntToInt.decrypt(ids[0])));
    				unit.setParamTypeId(Integer.parseInt(IntToInt.decrypt(ids[1])));
    			} else {
    				unit = urlMatcher.getParam(Integer.parseInt(IntToInt.decrypt(info[0])));
	    			if(null!=unit){
    				unit.setParamValue(info[1]);}
    			}
    			if (unit == null) {
    				continue;
    			}
				ParamModel paramModel = new ParamModel();
				paramModel.setId(unit.getId());
				paramModel.setActionId(unit.getActionId());
				paramModel.setParam(unit.getParam());
				paramModel.setParamValue(unit.getParamValue(), unit.isUserId());
				// User id params are already encrypted by MD5
				/*
				 * if (BaseMapper.userIdParamTypeSet.contains(unit.getId()) ||
				 * unit.isUserId()) {
				 * paramModel.setParamValue(StringToString.deTransfrom
				 * (unit.getParamValue()), unit.isUserId()); }
				 */
				paramModel.setParamTypeId(unit.getParamTypeId());
				paramModel.setLogCount(1L);
				paramMap.addParam(paramModel);

				// set msisdn
				if (paramModel.getParamTypeId() == MSISDN_PARAM_TYPE_ID) {
					this.currentMsisdnFromParam = paramModel.getParamValue();
				}
			}
		}
		return paramMap;
	}

	static class InvalidInputException extends RuntimeException {
		private static final long serialVersionUID = -8658561314390590451L;

		public InvalidInputException(String msg) {
			super(msg);
		}

		public InvalidInputException() {
		}
	}
}
