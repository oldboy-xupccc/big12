package com.chuangdata.userprofile.job.extract;

import com.chuangdata.framework.logmodel.FieldNotFoundException;
import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.logmodel.parser.LogModelParser;
import com.chuangdata.framework.logmodel.parser.LogModelParserException;
import com.chuangdata.framework.logmodel.parser.LogModelParserFactory;
import com.chuangdata.framework.resource.UrlMatcher;
import com.chuangdata.framework.resource.app.ActionUnit;
import com.chuangdata.framework.resource.app.AppUnit;
import com.chuangdata.framework.resource.app.DomainUnit;
import com.chuangdata.framework.resource.app.ParamUnit;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.chuangdata.userprofile.model.*;
import com.chuangdata.userprofile.utils.SpecialParamHandler;
import com.chuangdata.userprofile.utils.Strings;
import com.chuangdata.userprofile.utils.TimeConvertor;
import com.google.common.annotations.VisibleForTesting;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

/**
 * 
 * @date 2016-04-20 changed the encrypt to MD5 and output the trelation table of
 *       id for MD5 encrypting and AES encrypting
 * 
 * 
 */
public class ExtractMapper extends Mapper<Object, Text, Text, MultipleModel> {
	private static final Logger LOG = Logger.getLogger(ExtractMapper.class);
	protected LogModelParser logModeParser;

	protected UrlMatcher urlMatcher;

	protected Text outkey = new Text();

	private static int MSISDN_PARAM_TYPE_ID = 9;
	private static final int PARAM_TYPE_133 = 133;
	private static final int PARAM_TYPE_93 = 93;

	protected String currentMsisdnFromParam = null;

	public void setup(Context context) throws IOException {
		Configuration configuration = context.getConfiguration();
		String logConfigFilePath = configuration.get("chuangdata.log.config");
		if (logConfigFilePath != null) {
			try {
				this.logModeParser = LogModelParserFactory
						.getTextLogModelParserFromExternalConfig(logConfigFilePath);
			} catch (LogModelParserException e) {
				LOG.error("Init logModelParser error: ", e);
			}
		}
		String appHostFilePath = configuration.get("chuangdata.dmu.userprofile.app.host");
		String appActionFilePath = configuration.get("chuangdata.dmu.userprofile.app.action");
		String appParamFilePath = configuration.get("chuangdata.dmu.userprofile.app.param");
		String appDoaminFilePath = configuration.get("chuangdata.dmu.userprofile.app.domain");
		urlMatcher = new UrlMatcher(appHostFilePath, appActionFilePath,
				appParamFilePath, appDoaminFilePath, false); // Don't support encrypted resource files.
	}

	@VisibleForTesting
	protected void setLogModelParser(LogModelParser parser) {
		this.logModeParser = parser;
	}

	@VisibleForTesting
	protected void setUrlMatcher(UrlMatcher matcher) {
		this.urlMatcher = matcher;
	}

	public void map(Object key, Text value, Context context) {
		try {
			LogModel logModel = logModeParser.parse(value.toString());
			// 1. filter by something
			if (filterOut(logModel, context)) {
				context.getCounter(
						UserProfileCounter.MAP_FILTER_OUT)
						.increment(1);
				return;
			}
			this.currentMsisdnFromParam = null;
			ExtractAESEncrypterKeyModel keyModel = buildKey(logModel);
			ExtractMd5EncrypterKeyModel md5KeyModel = buildMd5Key(logModel);
			MultipleModel valueModel = buildValue(logModel);

			if (this.currentMsisdnFromParam != null
					&& !Strings.isNotEmpty(keyModel.getMsisdn())
					&& !Strings.isNotEmpty(md5KeyModel.getMsisdn())) {
				keyModel.setMsisdn(currentMsisdnFromParam);
				md5KeyModel.setMsisdn(currentMsisdnFromParam);
			}
			outkey.set(md5KeyModel.toString());
			context.write(outkey, valueModel);

			MultipleModel imeiModel = new MultipleModel();
			imeiModel.add((new Text(keyModel.getImei())));
			context.write(new Text(md5KeyModel.getImei()), imeiModel);
			// LOG.info("the imei id "+ " MD5 is " + md5KeyModel.getImei()+
			// "  and  AES is "+ keyModel.getImei() );

			MultipleModel imsiModel = new MultipleModel();
			imeiModel.add((new Text(keyModel.getImsi())));
			context.write(new Text(md5KeyModel.getImsi()), imsiModel);
			// LOG.info("the imsi id "+ " MD5 is " + md5KeyModel.getImsi()+
			// "  and  AES is "+ keyModel.getImsi() );

			MultipleModel msisdnModel = new MultipleModel();
			imeiModel.add((new Text(keyModel.getMsisdn())));
			context.write(new Text(md5KeyModel.getMsisdn()), msisdnModel);
			// LOG.info("the  msisdn id "+ " MD5 is " + md5KeyModel.getMsisdn()+
			// "  and  AES is "+ keyModel.getMsisdn());

			// out put domain
			MultipleModel domainModel = new MultipleModel();
			ExtractMd5EncrypterDomainKeyModel extractMd5EncrypterDomainKeyModel = ExtractMd5EncrypterDomainKeyModel
					.buildEncrypterDomainKeyModel(logModel);
			AppDomainModel appDomainModel = buildDomainModel(logModel);
			if (appDomainModel.getAppDomainId().get() > 0) {
				domainModel.add(appDomainModel);
				context.write(
						new Text(extractMd5EncrypterDomainKeyModel.toString()),
						domainModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.toString());
			context.getCounter(UserProfileCounter.MAP_INVALID_INPUT_ERROR)
					.increment(1);
		}
	}

	private AppDomainModel buildDomainModel(LogModel logModel)
			throws FieldNotFoundException {
		AppDomainModel appDomainModel = new AppDomainModel();
		DomainUnit domainUnit = urlMatcher.getDomain(getHost(logModel),
				getUri(logModel));
		if (domainUnit != null) {
			appDomainModel.setAppDomainId(domainUnit.getId());
			appDomainModel.setHost(getHost(logModel));
			appDomainModel.setLogCount(1L);
		}
		return appDomainModel;
	}

	private MultipleModel buildValue(LogModel logModel) {
		MultipleModel value = new MultipleModel();
		try {
			AppModel appModel = extractApp(logModel);
			if (appModel.getAppId().get() > 0) {
				value.add(appModel);
				ActionModel actionModel = extractAction(logModel);
				if (actionModel.getId() > 0) {
					value.add(actionModel);
					ParamMapModel paramMap = new ParamMapModel();
					boolean isSepecialParam = SpecialParamHandler.paramHandle((int)actionModel.getId(), getUri(logModel), paramMap );
					if(!isSepecialParam) {
					paramMap = extractParam(logModel, actionModel.getId());
					}
					value.add(paramMap);
				}
			}
		} catch (Exception e) {
			// do nothing
		}
		return value;
	}

	private AppModel extractApp(LogModel logModel)
			throws FieldNotFoundException {
		AppModel appModel = new AppModel();
		AppUnit appUnit = urlMatcher.getApp(getHost(logModel));
		if (appUnit != null) {
			appModel.setAppId(appUnit.getAppId());
			appModel.setAppTypeId(appUnit.getAppTypeId());
			appModel.setLogCount(1L);
		}
		return appModel;
	}

	private ActionModel extractAction(LogModel logModel)
			throws FieldNotFoundException {
		ActionModel actionModel = new ActionModel();
		ActionUnit unit = urlMatcher.getAction(getHost(logModel), getUri(logModel));
		if (unit != null) {
			actionModel.setId(unit.getId());
			actionModel.setHost(unit.getHost());
			actionModel.setUrlPatternStr(unit.getUrlPatternStr());
			actionModel.setDetailActionId(unit.getDetailActionId());
			actionModel.setActionTypeId(unit.getActionTypeId());
			actionModel.setInterestTag(unit.getInterestTag());
			actionModel.setAppId(unit.getAppId());
			actionModel.setAppTypeId(unit.getAppTypeId());
			actionModel.setLogCount(1L);
		}
		return actionModel;
	}

	private ParamMapModel extractParam(LogModel logModel, long actionId)
			throws FieldNotFoundException {
		ParamMapModel paramMap = new ParamMapModel();
		List<ParamUnit> paramUnits = urlMatcher.getParam(actionId, logModel);
		boolean isGetMutiParam = false;
		if (paramUnits != null && !paramUnits.isEmpty()) {
			for (ParamUnit unit : paramUnits) {
				ParamModel paramModel = new ParamModel();
				paramModel.setId(unit.getId());
				paramModel.setActionId(unit.getActionId());
				paramModel.setParam(unit.getParam());
				paramModel.setParamValue(unit.getParamValue(), unit.isUserId());
				paramModel.setParamTypeId(unit.getParamTypeId());
				paramModel.setLogCount(1L);
				paramMap.addParam(paramModel);
				// set msisdn
				if (paramModel.getParamTypeId() == MSISDN_PARAM_TYPE_ID) {
					this.currentMsisdnFromParam = unit.getParamValue(); // should already be encrypted
				}
				// 多参数萃取
				if (!isGetMutiParam && (PARAM_TYPE_133 == unit.getParamTypeId() || PARAM_TYPE_93 == unit.getParamTypeId())) {
					getMutiParam(paramMap,unit.getParamTypeId(),unit.getParamValue(),logModel);
					isGetMutiParam = true;
				}
			}
		}
		return paramMap;
	}

	private void getMutiParam(ParamMapModel paramMap, int actionTypeId,
			String value, LogModel logModel) {
		// json format
		if (value.contains("{") && value.contains("}") && value.contains("\"")
				&& value.contains(":") && value.contains(",")) {
			HashSet<Integer> actionSet = (HashSet<Integer>) urlMatcher.getActinIdSetByActionType(actionTypeId);
			for (Integer actionId : actionSet) {
				List<ParamUnit> paramUnits = urlMatcher.getParam(actionId, logModel);
				if (paramUnits != null && !paramUnits.isEmpty()) {
					for (ParamUnit unit : paramUnits) {
						if (value.contains(unit.getParam())) {
							int startIndex=value.indexOf(unit.getParam());
							int endIndex=value.indexOf("\"", startIndex+unit.getParam().length()+3);
							if(endIndex<startIndex|| startIndex<0|| endIndex<0) {
								break;
							}
							String paraValue=value.substring(startIndex,endIndex);
							ParamModel paramModel = new ParamModel();
							paramModel.setId(unit.getId());
							paramModel.setActionId(unit.getActionId());
							paramModel.setParam(unit.getParam());
							paramModel.setParamValue(paraValue, unit.isUserId());
							paramModel.setParamTypeId(unit.getParamTypeId());
							paramModel.setLogCount(1L);
							paramMap.addParam(paramModel);
							LOG.debug("muti value is "+ value+" muti param id is "+unit.getId()+" param is "+unit.getParam()+ " param value is  "
									+ paraValue + " paramType is "+ unit.getParamTypeId());
							if (paramModel.getParamTypeId() == MSISDN_PARAM_TYPE_ID) {
								this.currentMsisdnFromParam = unit.getParamValue(); // should already be encrypted
							}
						}
					}
				}
			}
		} else if (value.contains(":") && value.contains(",")) {
			// key value
			HashSet<Integer> actionSet = (HashSet<Integer>) urlMatcher.getActinIdSetByActionType(actionTypeId);
			for (Integer actionId : actionSet) {
				List<ParamUnit> paramUnits = urlMatcher.getParam(actionId, logModel);
				if (paramUnits != null && !paramUnits.isEmpty()) {
					for (ParamUnit unit : paramUnits) {
						if (value.contains(unit.getParam())) {
							int startIndex=value.indexOf(unit.getParam());
							int endIndex=value.indexOf(",", startIndex);
							if(endIndex<startIndex|| startIndex<0|| endIndex<0) {
								break;
							}
							String paraValue=value.substring(startIndex,endIndex-1);
							ParamModel paramModel = new ParamModel();
							paramModel.setId(unit.getId());
							paramModel.setActionId(unit.getActionId());
							paramModel.setParam(unit.getParam());
							paramModel.setParamValue(paraValue, unit.isUserId());
							paramModel.setParamTypeId(unit.getParamTypeId());
							paramModel.setLogCount(1L);
							paramMap.addParam(paramModel);
							LOG.debug("muti value is "+ value+" muti param id is "+unit.getId()+" param is "+unit.getParam()+ " param value is  "
									+ paraValue + " paramType is "+ unit.getParamTypeId());
							if (paramModel.getParamTypeId() == MSISDN_PARAM_TYPE_ID) {
								this.currentMsisdnFromParam = unit.getParamValue(); // should already be encrypted
							}
						}
					}
				}
			}
		
		}
	}

	private ExtractAESEncrypterKeyModel buildKey(LogModel logModel) {
		ExtractAESEncrypterKeyModel key = new ExtractAESEncrypterKeyModel();
		try {
			try {
				key.setTime(Long.parseLong(logModel.getProcedureEndTime()));
			} catch (NumberFormatException e) {
				try {
					key.setTime(Double.valueOf(
					    Double.parseDouble(logModel.getProcedureEndTime()) * 1000).longValue());
				} catch (NumberFormatException ex) {
					try {
						key.setTime(TimeConvertor.convertTime(logModel.getProcedureEndTime()));
					} catch (ParseException pex) {
						// DO NOTHING?
					}
				}
			}
		} catch (FieldNotFoundException e) {
			// TODO time should not be null
		}
		try {
			key.setMsisdn(logModel.getMsisdn());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setImei(logModel.getImei());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setImsi(logModel.getImsi());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setUserIp(logModel.getClientIP());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		return key;
	}

	private ExtractMd5EncrypterKeyModel buildMd5Key(LogModel logModel) {
		ExtractMd5EncrypterKeyModel key = new ExtractMd5EncrypterKeyModel();
		try {
			try {
				key.setTime(Long.parseLong(logModel.getProcedureEndTime()));
			} catch (NumberFormatException e) {
				try {
					key.setTime(Double.valueOf(
					    Double.parseDouble(logModel.getProcedureEndTime()) * 1000).longValue());
				} catch (NumberFormatException ex) {
					try {
						key.setTime(TimeConvertor.convertTime(logModel.getProcedureEndTime()));
					} catch (ParseException pex) {
						// DO NOTHING?
					}
				}
			}
		} catch (FieldNotFoundException e) {
			// TODO time should not be null
		}
		try {
			key.setMsisdn(logModel.getMsisdn());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setImei(logModel.getImei());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setImsi(logModel.getImsi());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		try {
			key.setUserIp(logModel.getClientIP());
		} catch (FieldNotFoundException e) {
			// DO NOTHING
		}
		return key;
	}

	private boolean filterOut(LogModel logModel, Context context) {
		// 1. Host should not be null
		try {
			String host = getHost(logModel);
			if (!Strings.isNotEmpty(host)) {
				context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_EMPTY_HOST).increment(1L);
				return true;
			}
			if (urlMatcher.getApp(host) == null) {
				context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_UNKNOW_APP);
				return true;
			}
		} catch (FieldNotFoundException e) {
			// Host not found, then nothing can do for userprofile
			context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_HOST).increment(1L);
			return true;
		}
		// 2. Url should not be null
		try {
			String url = getUri(logModel);
			if (!Strings.isNotEmpty(url)) {
				context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1L);
				return true;
			}
		} catch (FieldNotFoundException e) {
			context.getCounter(UserProfileCounter.MAP_FILTER_OUT_BY_URI).increment(1L);
			// url is a must field for userprofile system
			return true;
		}
		return false;
	}

	@VisibleForTesting
	protected String getHost(LogModel logModel) throws FieldNotFoundException {
		if (Strings.isNotEmpty(logModel.getUri())) {
			// uri不为空，检查uri中是否包含host，是则以uri为准
			String uri = logModel.getUri();
			if (uri.startsWith("http://")) {
				uri = uri.substring(7);
			}
			int index = uri.indexOf("/");
			if (index > 0) {
				// uri中包含host
				String h = uri.substring(0, index);
				if (Strings.isNotEmpty(h) && h.indexOf(":") > 0) {
					// if uri contains port
					return h.substring(0, h.indexOf(":"));
				}
				return h;
			}
		}
		return logModel.getHost();
	}

	@VisibleForTesting
	protected String getUri(LogModel logModel) throws FieldNotFoundException {
		// 若host不为空，但uri为空，则设为/
		if (Strings.isNotEmpty(getHost(logModel))
				&& !Strings.isNotEmpty(logModel.getUri())) {
			return "/";
		} else if (Strings.isNotEmpty(getHost(logModel))
				&& Strings.isNotEmpty(logModel.getUri())) {
			// host, uri are not empty, 去掉uri中的host
			String uri = logModel.getUri();
			if (uri.startsWith("http://")) {
				uri = uri.substring(7);
			}
			int index = uri.indexOf("/");
			if (index > 0) {
				// uri中包含host
				uri = uri.substring(index);
			}
			return uri;
		}
		return logModel.getUri();
	}
}
