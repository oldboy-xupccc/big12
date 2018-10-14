package com.chuangdata.framework.resource.source;

/**
 * @author luxiaofeng
 */
public class DataSourceUnit {

    private int id;
    private String name;
    private String province;
    private String isp;

    public DataSourceUnit(int id, String name, String province, String isp) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.isp = isp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

}
