package com.chuangdata.userprofile.job.decrypt;

import com.chuangdata.framework.encrypt.transform.IntToInt;
import com.chuangdata.framework.resource.UrlMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.job.encrypt.MD5Encrypt;
import com.chuangdata.userprofile.model.*;
import com.chuangdata.userprofile.utils.Strings;
import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author luxiaofeng
 */
public class DecryptMapper extends Mapper<Object, Text, Text, MultipleModel> {
	private static final Logger LOG = Logger.getLogger(DecryptMapper.class);
	private Decrypter decrypter1;
	private Decrypter decrypter2;

	protected UrlMatcher urlMatcher;

	private boolean onlyValue = false;

	private Text outKey = new Text();

	private boolean isEncrypted = true;
	private boolean isUrlEncrypted = true;

	private static int MSISDN_PARAM_TYPE_ID = 9;

	protected String currentMsisdnFromParam = "";

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private static final MD5Encrypt md5Encrypter = MD5Encrypt.getInstance();

	@VisibleForTesting
	protected void setUrlMatcher(UrlMatcher matcher) {
		this.urlMatcher = matcher;
	}

	@VisibleForTesting
	protected void setDecrypter(Decrypter decrypter) {
		this.decrypter1 = decrypter;
	}

	public void setup(Context context) throws IOException {
		Configuration conf = context.getConfiguration();

		String privateKeyFile = conf.get("chuangdata.encrypt.private.key.file");
		try {
			decrypter1 = new Decrypter(privateKeyFile);
		} catch (Exception e) {
			LOG.error("Init private key error: ", e);
		}
		String privateKeyFile2 = conf.get("chuangdata.encrypt.private.key.file.new");

		if (privateKeyFile2 != null) {
			try {
				decrypter2 = new Decrypter(privateKeyFile2);
			} catch (Exception e) {
				LOG.error("Init new private key error: ", e);
			}
		}
		onlyValue = conf.getBoolean("chuangdata.zj.result.only.value", false);
		isEncrypted = conf.getBoolean(
				"chuangdata.dmu.userprofile.result.encrypted", true);
		isUrlEncrypted = conf.getBoolean(
				"chuangdata.dmu.userprofile.result.url.encrypted", true);

		String appHostFilePath = conf.get("chuangdata.dmu.userprofile.app.host");
		String appActionFilePath = conf.get("chuangdata.dmu.userprofile.app.action");
		String appParamFilePath = conf.get("chuangdata.dmu.userprofile.app.param");

		urlMatcher = new UrlMatcher(appHostFilePath, appActionFilePath,
				appParamFilePath, false); // Don't support encrypted resource
											// files.
	}

	private String decrypteIfNeed(String input, boolean encrypted) {
		String decrypted = null;
		try {
			// if not encrypt, just return input
			// if encrypted, then try to decrypt input
			// if throw exception while decryption, then try another private key
			decrypted = encrypted ? decrypter1.decrypt(input) : input;
			return decrypted;
		} catch (Exception e) {
			if (decrypter2 != null) {
				try {
					decrypted = decrypter2.decrypt(input);
					return decrypted;
				} catch (Exception ex) {
					return input;
				}
			}
			return input;
		}
	}

	public void map(Object key, Text value, Context context) {
		try {
			String input = value.toString();
			if (!onlyValue) {
				String tmp[] = input.split("\\t");
				if(tmp.length > 1) {
					input = tmp[1];
				}else{
					input = tmp[0];
				}
			}
			String decrypted = decrypteIfNeed(input, isEncrypted);
			if (decrypted == null || decrypted.isEmpty()) {
				return;
			}
			String[] logs = splitDecryptedLine(decrypted);
			for (String log : logs) {
				String[] inputArray = Strings.split(log, "\\|");
				this.currentMsisdnFromParam = null;
				ExtractAESEncrypterKeyModel keyModel = buildKey(inputArray);
				MultipleModel valueModel = buildValue(inputArray);
				if (null == valueModel) {
					return;
				}
				if (this.currentMsisdnFromParam != null
						&& !Strings.isNotEmpty(keyModel.getMsisdn())) {
					keyModel.setEncryptedMsisdn(currentMsisdnFromParam);
				}
				outKey.set(keyModel.toString());
				context.write(outKey, valueModel );

			}
		} catch (InvalidInputException e) {
			// TODO increase counter
			LOG.error("Map error: ", e);
		} catch (Exception e) {
			LOG.error("Map error: ", e);
		}
	}

	/**
	 * We had better to not use \n as the field separator
	 * 
	 * @param decrypted
	 * @return
	 */
	private String[] splitDecryptedLine(String decrypted) {
		if (decrypted.contains("\\001")) {
			return decrypted.split("\\001");
		}
		return decrypted.split("\\n");
	}

	private ExtractAESEncrypterKeyModel buildKey(String[] input) {
		ExtractAESEncrypterKeyModel key = new ExtractAESEncrypterKeyModel();
		try {
			key.setTime(DATE_FORMAT.parse(input[EncryptedFields.TIME_STAMP])
					.getTime());
		} catch (ParseException e) {
			try {
				// FIXME set time according to value unit
				// just assume the unit is Second here, so we need convert it to
				// MS
				key.setTime(Long.parseLong(input[EncryptedFields.TIME_STAMP]) * 1000);
			} catch (Exception ex) {
				LOG.error("TIME STAMP PARSE ERROR:"
						+ input[EncryptedFields.TIME_STAMP]);
			}
		}
		key.setEncryptedImei(input[EncryptedFields.MEID]);
		//key.setUserIp(input[EncryptedFields.USER_IP]);
		return key;
	}

	private MultipleModel buildValue(String[] input) {
		MultipleModel value = new MultipleModel();

		//userAgent
		UserAgentModel userAgentModel = new UserAgentModel();
		String ua = input[EncryptedFields.OS]+"^"+input[EncryptedFields.TERMINAL]+"^"+input[EncryptedFields.TERMINAL_PRODUCER]+"^"+input[EncryptedFields.BROWSER];
		if(null == ua || ua.equalsIgnoreCase("null")){ua="";}
		userAgentModel.setUserAgent(new Text(ua));
		userAgentModel.setPreImei(new Text(""));
		userAgentModel.setLogCount(1L);
		value.add(userAgentModel);

		int appId = Integer.parseInt(input[EncryptedFields.APP_ID]);
		AppUnit appUnit = urlMatcher.getApp(appId);
		AppModel appModel = new AppModel();
		if (appUnit != null) {
			appModel.setAppId(appUnit.getAppId());
			appModel.setAppTypeId(appUnit.getAppTypeId());
			appModel.setLogCount(1L);
		} else {
			// not a valid line, just ignore it
			// throw new InvalidInputException("Can't get app unit by id=" +
			// appId);
			return null;
		}
		if (appModel.getAppId().get() > 0) {
			value.add(appModel);
			long actionId = Long.parseLong(input[EncryptedFields.ACTION_ID]);
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
				if (Strings.isNotEmpty(input[EncryptedFields.URL_PARAMS])) {
					ParamMapModel paramMap = extractParam(input[EncryptedFields.URL_PARAMS]);
					value.add(paramMap);
				}
			}
		}
		return value;
	}

	private ParamMapModel extractParam(String urlParams) {
		try {
			urlParams = decrypteIfNeed(urlParams, isUrlEncrypted);
		} catch (Exception e) {
			// maybe isUrlEncrypted is wrong?
			// just leave the origin value of urlParams
		}
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
					unit.setParamTypeId(Integer.parseInt(IntToInt
							.decrypt(ids[1])));
				} else {
					unit = urlMatcher.getParam(Integer.parseInt(IntToInt
							.decrypt(info[0])));
					if(null!=unit){
					unit.setParamValue(info[1]);
					}
				}
				if (unit == null) {
					continue;
				}
				ParamModel paramModel = new ParamModel();
				paramModel.setId(unit.getId());
				paramModel.setActionId(unit.getActionId());
				paramModel.setParam(unit.getParam());
				paramModel.setParamValue(unit.getParamValue(), false); // just
																		// parameter
																		// is
																		// userid,do
																		// not
																		// encrypt,had
																		// been
																		// encrypted
																		// by
																		// md5
				// User id params are already encrypted by MD5
				/*
				 * if (BaseMapper.userIdParamTypeSet.contains(unit.getId()) ||
				 * unit.isUserId()) {
				 * paramModel.setParamValue(StringToString.deTransfrom
				 * (unit.getParamValue()), unit.isUserId()); }
				 */
				paramModel.setParamTypeId(unit.getParamTypeId());
				paramModel.setLogCount(1L);

				// set encrypted msisdn
				if (paramModel.getParamTypeId() == MSISDN_PARAM_TYPE_ID) {

					//if it is not be encrypted, encrypte it by md5.
					String tmp = unit.getParamValue().trim();

					//encrypt as 13006352257_2
					if(tmp.indexOf("_") > 0){
						this.currentMsisdnFromParam = md5Encrypter.encrypt(tmp.split("\\_")[0]);
					}

                    if(tmp.length() == 11 && tmp.startsWith("1")) {
						this.currentMsisdnFromParam = md5Encrypter.encrypt(tmp);
					}else {
						this.currentMsisdnFromParam = tmp; // should already be encrypted
					}

					paramModel.setParamValue(this.currentMsisdnFromParam, false);


				}


				paramMap.addParam(paramModel);

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
