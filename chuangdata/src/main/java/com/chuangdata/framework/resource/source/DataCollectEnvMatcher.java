package com.chuangdata.framework.resource.source;

import com.chuangdata.framework.resource.app.ConfigReader;
import com.chuangdata.framework.resource.app.MatcherInitializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luxiaofeng
 */
public class DataCollectEnvMatcher implements MatcherInitializer {

    private Map<String, DataCollectEnvUnit> dataCollectEnvMap;

    private boolean isEncrypted = false;

    public DataCollectEnvMatcher(String dataCollectEnvConfigFile, boolean isEncrypted) throws IOException {
        dataCollectEnvMap = new HashMap<String, DataCollectEnvUnit>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(dataCollectEnvConfigFile, this, isEncrypted);
    }

    @Override
    public void init(String[] info) {
        if (info != null && info.length == 5) {
            int id = Integer.parseInt(info[0]);
            String displayName = info[1];
            String name = info[2];
            int networkTypeId = Integer.parseInt(info[3]);
            int deleted = Integer.parseInt(info[4]);
            if (deleted > 0) {
                return;
            }
            DataCollectEnvUnit dataCollectEnvUnit = new DataCollectEnvUnit(id, displayName, name, networkTypeId);
            dataCollectEnvMap.put(name, dataCollectEnvUnit);
        }
    }

    @Override
    public int size() {
        return dataCollectEnvMap == null ? 0 : dataCollectEnvMap.size();
    }

    public DataCollectEnvUnit get(String name) {
        return dataCollectEnvMap == null ? null : dataCollectEnvMap.get(name);
    }

}
