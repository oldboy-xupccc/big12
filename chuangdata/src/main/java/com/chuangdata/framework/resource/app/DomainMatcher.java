package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.encrypt.resource.Encrypter;
import com.google.common.annotations.VisibleForTesting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DomainMatcher implements MatcherInitializer {

    private boolean isEncrypted = false;

    private Map<String, DomainUnit> domainMap;

    private Map<Integer, DomainUnit> domainIdMap;

    @VisibleForTesting
    protected Map getDomainMap() {
        return domainMap;
    }

    @VisibleForTesting
    protected void setDomainMap(Map<String, DomainUnit> domainMap) {
        this.domainMap = domainMap;
    }

    @VisibleForTesting
    protected DomainMatcher() {
    }

    public DomainMatcher(String domainConfigFile, boolean isEncrypted) throws IOException {
        domainMap = new HashMap<String, DomainUnit>();
        domainIdMap = new HashMap<Integer, DomainUnit>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(domainConfigFile, this, isEncrypted);
    }

    public void init(String[] info) {
        if (info != null && info.length == 3) {
            try{
            DomainUnit unit = new DomainUnit(
                    Integer.parseInt(info[0]),   // id
                    info[1],                     // host
                    info[2]);   // searchUri
            domainMap.put(info[1], unit);
            domainIdMap.put(unit.getId(), unit);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public DomainUnit getDomainById(int id) {
        if (domainIdMap != null) {
            return domainIdMap.get(id);
        }
        return null;
    }

    /**
     * Get domain unit via host, if not found, then return null
     *
     * @param host
     * @return
     */
    public DomainUnit getDomain(String host) {
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
            if (domainMap.containsKey(encryptedHost)) {
                return domainMap.get(encryptedHost);
            } else {
                host = host.substring(host.indexOf(".") + 1);
            }
        }
        return null;
    }

    public DomainUnit getDomain(String host, String uri) {
        DomainUnit unit = getDomain(host);
        if (unit != null && unit.isMatch(uri)) {
            // search domain
            // check whether the uri match searchUri
            return unit;
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
        return domainIdMap.size();
    }
}
