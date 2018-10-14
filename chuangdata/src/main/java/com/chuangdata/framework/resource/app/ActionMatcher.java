package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.encrypt.resource.Encrypter;
import com.chuangdata.userprofile.counter.UserProfileCounter;
import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionMatcher implements MatcherInitializer {
//    private static final Logger LOG = Logger.getLogger(ActionMatcher.class);
    private Map<String, Map<Pattern, ActionUnit>> actionMap;
    private Map<Long, ActionUnit> actionMapById;
    private boolean isEncrypted = false;
    private Map<String, ActionUnit> actionOtherMap;



    @VisibleForTesting
    protected Map getActionMap() {
        return actionMap;
    }

    @VisibleForTesting
    protected void setActionMap(Map<String, Map<Pattern, ActionUnit>> actionMap) {
        this.actionMap = actionMap;
    }

    @VisibleForTesting
    protected ActionMatcher() {
    }

    public ActionMatcher(String configFilePath, boolean isEncrypted) throws IOException {
        actionMap = new HashMap<String, Map<Pattern, ActionUnit>>();
        actionOtherMap = new HashMap<String,ActionUnit>();
        actionMapById = new HashMap<Long, ActionUnit>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(configFilePath, this, isEncrypted);
    }

    public void init(String[] info) {
        if (info != null && info.length == 7) {
            try {
                Long id = Long.parseLong(info[0]);
                String host = info[1];
                String url = info[2];
                Integer detailActionId = Integer.parseInt(info[3]);
                Integer actionTypeId = Integer.parseInt(info[4]);
                String interestTag = info[5];
                Integer appId = Integer.parseInt(info[6]);
                Pattern urlPattern = Pattern.compile(url);
                ActionUnit unit = new ActionUnit(id, host, url, detailActionId, actionTypeId, interestTag, appId);
                Map<Pattern, ActionUnit> patternMap = actionMap.get(host);
                if (patternMap == null) {
                    patternMap = new LinkedHashMap<Pattern, ActionUnit>();//change by luxiaofeng
                }
                patternMap.put(urlPattern, unit);

                //2017-08-30 Add for filter out by url is '/' by leijianping
                if (null != url && url.equalsIgnoreCase("/")) {
                    actionOtherMap.put(host, unit);
                } else {
                    actionMap.put(host, patternMap);
                }

                actionMapById.put(unit.getId(), unit);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * if nothing matched, then return null
//     *
//     * @param host
//     * @param uri
//     * @return
//     */
//    public ActionUnit getAction(String host, String uri) {
//        // 1. if host not match, then return null directly
//        String encryptedHost = getEncrypedHostIfNeeded(host);
////        if (!actionMap.containsKey(encryptedHost)) {
////            if (host.startsWith("www.")) {
////                encryptedHost = getEncrypedHostIfNeeded(host.substring(4));
////                if (!actionMap.containsKey(encryptedHost)) {
////                    return null;
////                }
////            } else {
////                //2017-08-30 Add for filter out by url is '/' by leijianping
////                if (actionOtherMap.containsKey(encryptedHost)) {
////                    return actionOtherMap.get(encryptedHost);
////                }
////                else{
////                    return null;
////                }
////            }
////        }
//        Map<Pattern, ActionUnit> patternMap = actionMap.get(encryptedHost);
//        // 2. for each pattern of the specific host
//        // TODO how to improve? for each pattern is not a good way
//        if(patternMap!=null&&patternMap.size()>0) {
//            for (Pattern urlPattern : patternMap.keySet()) {
//                Matcher m = urlPattern.matcher(uri);
//                if (m.find()) {
//                    // matches, then return behaviourUnit
//                    return patternMap.get(urlPattern);
//                }
//            }
//        }
//        //2017-08-30 Add for filter out by url is '/' by leijianping
//        if (actionOtherMap.containsKey(encryptedHost)) {
//            return actionOtherMap.get(encryptedHost);
//        }
//
//        // no pattern matches and no filter out by url is '/', then return null by leijianping
//        return null;
//    }




    /**
     * if nothing matched, then return null
     *
     * @param host
     * @param uri
     * @return
     */
    public ActionUnit getAction(String host, String uri) {
        // 1. if host not match, then return null directly
        String encryptedHost = getEncrypedHostIfNeeded(host);
//        if (!actionMap.containsKey(encryptedHost)) {
//            if (host.startsWith("www.")) {
//                encryptedHost = getEncrypedHostIfNeeded(host.substring(4));
//                if (!actionMap.containsKey(encryptedHost)) {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }
        Map<Pattern, ActionUnit> patternMap = actionMap.get(encryptedHost);
        if(patternMap==null){
            //2017-08-30 Add for filter out by url is '/' by leijianping
            if (actionOtherMap.containsKey(encryptedHost)) {
//                LOG.error("/匹配  host:"+host+"  uri:"+ uri);
                return actionOtherMap.get(encryptedHost);
            }
        }
        if(null == patternMap || patternMap.size() <=0){
            return null;

        }
        // 2. for each pattern of the specific host
//        for (Pattern urlPattern : patternMap.keySet()) {
//            System.out.println(urlPattern);
//            Matcher m = urlPattern.matcher(encryptedHost+uri);
//            if (m.find()) {
//                // matches, then return behaviourUnit
//                return patternMap.get(urlPattern);
////                System.out.println(urlPattern);
//            }
//        }
        // sort patternMap by value<ActionUnit.id> asc
        List<Map.Entry<Pattern, ActionUnit>> pList = getHashMapSortByValue(patternMap);
        for (Map.Entry<Pattern, ActionUnit> pEntry : pList) {
            //2017-07-18 Updated for host+uri match by Luxiaofeng
            Pattern urlPattern = pEntry.getKey();
//            System.out.println(urlPattern);
            Matcher m = urlPattern.matcher(encryptedHost+uri);
            if (m.find()) {
                // matches, then return behaviourUnit
//                LOG.error("host+uri关联  host:"+host+"  uri:"+ uri);
                return pEntry.getValue();
//                System.out.println(pEntry.getValue());
            }
        }

        // no pattern matches, then return null
        return null;
    }



    private String getEncrypedHostIfNeeded(String host) {
        if (isEncrypted) {
            return Encrypter.encodeByMD5(host);
        }
        return host;
    }

    public int size() {
        return actionMap.size();
    }

    public ActionUnit getAction(long actionId) {
        return actionMapById.get(actionId);
    }

    /**
     * GetHashMapSortByValue
     * @param map
     * @return
     */
    public List<Map.Entry<Pattern, ActionUnit>> getHashMapSortByValue(Map<Pattern, ActionUnit> map){
        List<Map.Entry<Pattern, ActionUnit>> infoIds =
                new ArrayList<Map.Entry<Pattern, ActionUnit>>(map.entrySet());

        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<Pattern, ActionUnit>>() {
            public int compare(Map.Entry<Pattern, ActionUnit> o1, Map.Entry<Pattern, ActionUnit> o2) {
                if(o2.getValue().getId() > o1.getValue().getId()){
                    return -1;
                }else{
                    return 1;
                }
                // return (o1.getKey()).toString().compareTo(o2.getKey()); //by key
            }
        });

        return infoIds;
    }

}
