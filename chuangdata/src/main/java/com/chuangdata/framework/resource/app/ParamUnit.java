package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.resource.Utils;

public class ParamUnit {
    private int id;
    private int actionId;
    private String param;
    private String paramValue;
    private int paramTypeId;
    private boolean isUserId;
    private boolean isSpecial;

    private boolean isJsonType;

    public ParamUnit() {

    }

    public ParamUnit(int id, int actionId, String param, String paramValue, int paramTypeId, boolean isJsonType, boolean isSpecial) {
        this(id, actionId, param, paramValue, paramTypeId, isJsonType, false,isSpecial);
    }

    public ParamUnit(int id, int actionId, String param, String paramValue, int paramTypeId, boolean isJsonType, boolean isUserId, boolean isSpecial) {
        this.id = id;
        this.actionId = actionId;
        this.param = param;
        this.paramValue = paramValue;
        this.paramTypeId = paramTypeId;
        this.isJsonType = isJsonType;
        this.isUserId = isUserId;
        this.isSpecial=isSpecial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = Utils.replaceSeparator(paramValue);
    }

    public int getParamTypeId() {
        return paramTypeId;
    }

    public void setParamTypeId(int paramTypeId) {
        this.paramTypeId = paramTypeId;
    }

    public boolean isJsonType() {
        return isJsonType;
    }

    public void setJsonType(boolean isJsonType) {
        this.isJsonType = isJsonType;
    }

    public boolean isUserId() {
        return isUserId;
    }

    public void setUserId(boolean isUserId) {
        this.isUserId = isUserId;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }
}
