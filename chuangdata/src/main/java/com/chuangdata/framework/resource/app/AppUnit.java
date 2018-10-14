package com.chuangdata.framework.resource.app;

public class AppUnit {
    private int id;
    private String host;
    private int appId;
    private int appTypeId;

    public AppUnit() {
    }

    public AppUnit(int id, String host, int appId, int appTypeId) {
        this.id = id;
        this.host = host;
        this.appId = appId;
        this.appTypeId = appTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
