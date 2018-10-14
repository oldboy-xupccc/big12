package com.chuangdata.framework.resource;

import com.chuangdata.framework.logmodel.LogModel;
import com.chuangdata.framework.resource.app.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 各种通过url, host匹配的入口类
 *
 * @author luxiaofeng
 */
public class UrlMatcher {
    private static final Logger LOG = Logger.getLogger(UrlMatcher.class);
    private AppMatcher appMatcher;

    private ActionMatcher actionMatcher;

    private ParamMatcher paramMatcher;

    private DomainMatcher domainMatcher;

    public UrlMatcher(String appHostFilePath, String appActionFilePath, String appParamFilePath, boolean isEncrypted) throws IOException {
        LOG.info("Initialize urlMathcer. isEncrypted=" + Boolean.toString(isEncrypted));
        appMatcher = new AppMatcher(appHostFilePath, isEncrypted);
        actionMatcher = new ActionMatcher(appActionFilePath, isEncrypted);
        paramMatcher = new ParamMatcher(appParamFilePath, isEncrypted);
    }

    public UrlMatcher(String appHostFilePath, String appActionFilePath, String appParamFilePath, String domainFilePath, boolean isEncrypted) throws IOException {
        LOG.info("Initialize urlMathcer. isEncrypted=" + Boolean.toString(isEncrypted));
        appMatcher = new AppMatcher(appHostFilePath, isEncrypted);
        actionMatcher = new ActionMatcher(appActionFilePath, isEncrypted);
        paramMatcher = new ParamMatcher(appParamFilePath, isEncrypted);
        domainMatcher = new DomainMatcher(domainFilePath, isEncrypted);
    }

    public AppUnit getApp(String host) {
        return appMatcher.getApp(host);
    }

    public AppUnit getApp(int appId) {
        return appMatcher.getApp(appId);
    }

    public ActionUnit getAction(String host, String uri) {
        ActionUnit unit = actionMatcher.getAction(host, uri);
        if (unit != null) {
            unit.setAppTypeId(appMatcher.getAppTypeId(unit.getAppId()));
        }
        return unit;
    }

    public ActionUnit getAction(long actionId) {
        ActionUnit unit = actionMatcher.getAction(actionId);
        if (unit != null) {
            unit.setAppTypeId(appMatcher.getAppTypeId(unit.getAppId()));
        }
        return unit;
    }

    public List<ParamUnit> getParam(long actionId, LogModel logModel) {
        return paramMatcher.getParam(actionId, logModel);
    }

    public ParamUnit getParam(int paramId) {
        return paramMatcher.getParam(paramId);
    }

    public DomainUnit getDomain(String host, String uri) {
        if (domainMatcher != null) {
            return domainMatcher.getDomain(host, uri);
        }
        return null;
    }

    public DomainUnit getDomainById(int app_domain_id) {
        if (domainMatcher != null) {
            return domainMatcher.getDomainById(app_domain_id);
        }
        return null;
    }

    public Set<Integer> getActinIdSetByActionType(int actionTypeId) {
        return paramMatcher.getActionIdSet(actionTypeId);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("App size=" + (appMatcher == null ? 0 : appMatcher.size())).append("\t");
        builder.append("Action size=" + (actionMatcher == null ? 0 : actionMatcher.size())).append("\t");
        builder.append("Param size=" + (paramMatcher == null ? 0 : paramMatcher.size())).append("\t");
        builder.append("Domain size=" + (domainMatcher == null ? 0 : domainMatcher.size()));
        return builder.toString();
    }

}
