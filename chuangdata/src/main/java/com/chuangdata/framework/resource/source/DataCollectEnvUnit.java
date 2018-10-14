package com.chuangdata.framework.resource.source;

/**
 * @author luxiaofeng
 */
public class DataCollectEnvUnit {
    private int id;
    private String displayName;
    private String name;
    private int networkTypeId;

    public DataCollectEnvUnit(int id, String displayName, String name, int networkTypeId) {
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.networkTypeId = networkTypeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNetworkTypeId() {
        return networkTypeId;
    }

    public void setNetworkTypeId(int networkTypeId) {
        this.networkTypeId = networkTypeId;
    }

}
