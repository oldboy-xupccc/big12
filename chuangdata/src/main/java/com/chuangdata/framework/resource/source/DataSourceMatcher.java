package com.chuangdata.framework.resource.source;

import com.chuangdata.framework.resource.app.ConfigReader;
import com.chuangdata.framework.resource.app.MatcherInitializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luxiaofeng
 */
public class DataSourceMatcher implements MatcherInitializer {

    private Map<String, DataSourceUnit> dataSourceMap;

    private boolean isEncrypted = false;

    public DataSourceMatcher(String dataSourceConfigFile, boolean isEncrypted) throws IOException {
        dataSourceMap = new HashMap<String, DataSourceUnit>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(dataSourceConfigFile, this, isEncrypted);
    }

    @Override
    public void init(String[] info) {
        if (info != null && info.length == 5) {
            int id = Integer.parseInt(info[0]);
            String name = info[1];
            String province = info[2];
            String isp = info[3];
            int deleted = Integer.parseInt(info[4]);
            if (deleted > 0) {
                return;
            }
            DataSourceUnit dataSourceUnit = new DataSourceUnit(id, name, province, isp);
            dataSourceMap.put(province + "_" + isp, dataSourceUnit);
        }
    }

    @Override
    public int size() {
        return dataSourceMap == null ? 0 : dataSourceMap.size();
    }

    public DataSourceUnit get(String province, String isp) {
        return dataSourceMap == null ? null : dataSourceMap.get(province + "_" + isp);
    }
}
