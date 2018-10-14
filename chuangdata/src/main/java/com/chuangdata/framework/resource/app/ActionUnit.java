package com.chuangdata.framework.resource.app;

public class ActionUnit {
    private long id;
    private String host;
    private String urlPatternStr;
    private int detailActionId;
    private int actionTypeId;
    private String interestTag;
    private int appId;
    private int appTypeId;

    public ActionUnit() {
    }

    public ActionUnit(long id, String host, String urlPatternStr,
                      int detailActionId, int actionTypeId, String interestTag, int appId, int appTypeId) {
        this.id = id;
        this.host = host;
        this.urlPatternStr = urlPatternStr;
        this.detailActionId = detailActionId;
        this.actionTypeId = actionTypeId;
        this.interestTag = interestTag;
        this.appId = appId;
        this.appTypeId = appTypeId;
    }

    public ActionUnit(long id, String host, String urlPatternStr,
                      int detailActionId, int actionTypeId, String interestTag, int appId) {
        this(id, host, urlPatternStr, detailActionId, actionTypeId, interestTag, appId, -1);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrlPatternStr() {
        return urlPatternStr;
    }

    public void setUrlPatternStr(String urlPatternStr) {
        this.urlPatternStr = urlPatternStr;
    }

    public int getDetailActionId() {
        return detailActionId;
    }

    public void setDetailActionId(int detailActionId) {
        this.detailActionId = detailActionId;
    }

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    public String getInterestTag() {
        return interestTag;
    }

    public void setInterestTag(String interestTag) {
        this.interestTag = interestTag;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
    }


}
