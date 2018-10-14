package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.encrypt.resource.Encrypter;
import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppMatcher implements MatcherInitializer {

    private boolean isEncrypted = false;

    private Map<String, AppUnit> appMap;

    private Map<Integer, AppUnit> appMapById;

    private Map<Integer, Integer> appTypeMap;

    @VisibleForTesting
    protected Map getAppMap() {
        return appMap;
    }

    @VisibleForTesting
    protected void setAppMap(Map<String, AppUnit> appMap) {
        this.appMap = appMap;
    }

    @VisibleForTesting
    protected AppMatcher() {
    }

    public AppMatcher(String appHostConfigFile, boolean isEncrypted) throws IOException {
        appMap = new HashMap<String, AppUnit>();
        appMapById = new HashMap<Integer, AppUnit>();
        appTypeMap = new HashMap<Integer, Integer>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(appHostConfigFile, this, isEncrypted);
    }

    public void init(String[] info) {
        if (info != null && info.length == 4) {
            try{
            AppUnit unit = new AppUnit(
                    Integer.parseInt(info[0]),   // id
                    info[1],                     // host
                    Integer.parseInt(info[2]),   // app_id
                    Integer.parseInt(info[3]));  // app_type_id
            appMap.put(info[1], unit);
            appMapById.put(unit.getId(), unit);
            appTypeMap.put(unit.getAppId(), unit.getAppTypeId());

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Get app unit via host, if not found, then return null
     *
     * @param host
     * @return
     */
    public AppUnit getApp(String host) {
        if (host == null || host.isEmpty()) {
            return null;
        }
        // 以http://开头的
        if (host.startsWith("http://")) {
            host = host.substring(7);
        }
        // 以端口结尾
        if (host.indexOf(":") > 0) {
            int index = host.indexOf(":");
            host = host.substring(0, index);
        }
        while (host.indexOf(".") != -1) {
            String encryptedHost = getEncrypedHostIfNeeded(host);
            if (appMap.containsKey(encryptedHost)) {
                return appMap.get(encryptedHost);
            } else {
                host = host.substring(host.indexOf(".") + 1);
            }
        }
        return null;
    }

    private String getEncrypedHostIfNeeded(String host) {
        if (isEncrypted) {
            return Encrypter.encodeByMD5(host);
        }
        return host;
    }

    public int size() {
        return appMap.size();
    }

    public AppUnit getApp(int appId) {
        return appMapById.get(appId);
    }

    public int getAppTypeId(int appId) {
        if (appTypeMap.containsKey(appId)) {
            return appTypeMap.get(appId);
        }
        return -1;
    }
}
