package com.chuangdata.framework.resource.tag;

import com.chuangdata.framework.resource.app.ConfigReader;
import com.chuangdata.framework.resource.app.MatcherInitializer;
import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * 目前仅支持业务标签匹配
 *
 * @author luxiaofeng
 */
public class TagMatcher implements MatcherInitializer {
    Logger logger = Logger.getLogger(TagMatcher.class);
    private TagTree tagTree;
    private Map<Integer, Integer> appIdToTagIdMap;
    private Map<String, TagUnit> oldIdToTagIdMap;
    private Map<String, TagUnit> oldIdToTagIdMapByName;

    private boolean isEncrypted = false;

    public TagMatcher(String tagConfigFile, boolean isEncrypted)
            throws IOException {
        tagTree = new TagTree();
        appIdToTagIdMap = new HashMap<Integer, Integer>();
        oldIdToTagIdMap = new HashMap<String, TagUnit>();
        oldIdToTagIdMapByName = new HashMap<String, TagUnit>();
        this.isEncrypted = isEncrypted;
        ConfigReader.read(tagConfigFile, this, isEncrypted);
        tagTree.buildTagTree();
    }

    @Override
    public void init(String[] info) {
        if (info != null && info.length == 7) {
            int tagId = Integer.parseInt(info[0]);
            String tagName = info[1];
            int tagLevelId = Integer.parseInt(info[2]);
            int parentTagId = Integer.parseInt(info[3]);
            int tagTypeId = Integer.parseInt(info[4]);
            int oldTagId = Integer.parseInt(info[5]);
            int deleted = Integer.parseInt(info[6]);
            if (deleted > 0) {
                return;
            }
            TagUnit tagUnit = new TagUnit(tagId, tagName, tagLevelId,
                    parentTagId, tagTypeId, oldTagId);
            tagTree.addTagToTree(tagUnit);
            oldIdToTagIdMap.put(tagLevelId + "_" + parentTagId + "_" + oldTagId, tagUnit);
            oldIdToTagIdMapByName.put(tagLevelId + "_" + parentTagId + "_" + tagName, tagUnit);
            if (tagLevelId == 2 && tagTypeId == 3) {
                // set into appIdToTagMap
                appIdToTagIdMap.put(oldTagId, tagUnit.getTagId());
            }
        }
    }

    public TagUnit getOldIdToTagIdMap(int tagLevelId, int parentTagId, int oldTagId) {
        return this.oldIdToTagIdMap.get(tagLevelId + "_" + parentTagId + "_" + oldTagId);
    }

    public TagUnit getOldIdToTagIdMapByName(int tagLevelId, int parentTagId, String tagName) {
        return this.oldIdToTagIdMapByName.get(tagLevelId + "_" + parentTagId + "_" + tagName);
    }

    public Map<Integer, Integer> getAppIdToTagIdMap() {
        return this.appIdToTagIdMap;
    }

    @Override
    public int size() {
        return tagTree.size();
    }

    public TagUnit get(int tagId) {
        return tagTree.getTagUnitById(tagId);
    }

    /**
     * Get parent tag ids from itself (contains) to root id
     *
     * @param tagId
     * @return
     */
    public List<Integer> getParentTagIds(int tagId) {
        List<Integer> tagLists = new ArrayList<Integer>();
        TagUnit tagUnit = tagTree.getTagUnitById(tagId);
        if (tagUnit == null) {
            return tagLists;
        }
        tagLists.add(tagId);
        int parentTagId = tagUnit.getParentTagId();
        while (parentTagId > 0) {
            tagUnit = tagTree.getTagUnitById(parentTagId);
            if (tagUnit != null) {
                tagLists.add(parentTagId);
                parentTagId = tagUnit.getParentTagId();
            } else {
                break;
            }
        }
        return tagLists;
    }

    public List<Integer> getAllTagIdsByAction(int appId, int actionId,
                                              int detailActionId) {
        List<Integer> tagIds = new ArrayList<Integer>();
        // logger.info("the app id is " + appId);
        // if (null != appIdToTagIdMap) {
        // logger.info(appIdToTagIdMap.get(appId));
        // }
        if (appIdToTagIdMap.containsKey(appId)) {
            int tagId = appIdToTagIdMap.get(appId);
            TagUnit appTag = tagTree.getTagUnitById(tagId);
            if (appTag != null) {
                tagIds.add(appTag.getParentTagId()); // level1 tag id : appType
                tagIds.add(tagId); // level2 tag id : app
                Collection<Integer> level3Tags = appTag.getChildrenTags();
                TagUnit level3TagUnit = null;
                for (int level3TagId : level3Tags) {
                    tagIds.add(level3TagId);
                    level3TagUnit = tagTree.getTagUnitById(level3TagId);
                    break; // only ont level 3 tag id for service tag
                }
                if (level3TagUnit == null) {
                    return tagIds;
                }
                TagUnit level4TagUnit = null;
                Collection<Integer> level4Tags = level3TagUnit
                        .getChildrenTags();
                for (int level4TagId : level4Tags) {
                    TagUnit childUnit = tagTree.getTagUnitById(level4TagId);
                    if (childUnit.getOldTagId() == actionId) {
                        tagIds.add(level4TagId);
                        level4TagUnit = childUnit;
                        break;
                    }
                }
                if (level4TagUnit == null) {
                    return tagIds;
                }
                Collection<Integer> level5Tags = level4TagUnit
                        .getChildrenTags();
                for (int level5TagId : level5Tags) {
                    TagUnit childUnit = tagTree.getTagUnitById(level5TagId);
                    if (childUnit.getOldTagId() == detailActionId) {
                        tagIds.add(level5TagId);
                        break;
                    }
                }
            }
        }
        return tagIds;

    }

    /**
     * get Object[appId,appName] by tagId,else return null
     *
     * @param tagId
     * @return
     */
    public Object[] getAppTypeNameArrayByAppTypeId(int tagId) {
        Object[] result = null;
        TagUnit tagUnit = tagTree.getTagUnitById(tagId);
        if (tagUnit == null) {
            return result;
        }
        String tagName = tagUnit.getTagName();
        result = new Object[]{tagId, tagName};
        return result;

    }

    /**
     * get Object[appId,appName,typeName] by tagId,else return null
     *
     * @param tagId
     * @return
     */
    public Object[] getAppNameArrayByAppNameId(int tagId) {
        Object[] result = null;
        TagUnit tagUnit = tagTree.getTagUnitById(tagId);
        if (tagUnit == null) {
            return result;
        }
        String tagName = tagUnit.getTagName();
        int parentId = tagUnit.getParentTagId();
        String parentName = null;
        if (parentId > 0) {
            parentName = tagTree.getTagUnitById(parentId).getTagName();
        }
        result = new Object[]{tagId, tagName, parentName};
        return result;

    }

    /**
     * get Object[appId,appActionName,appName,typeName] by tagId,else return
     * null
     *
     * @param tagId
     * @return
     */
    public Object[] getAppActionArrayByAppActionId(int tagId) {
        Object[] result = null;
        TagUnit tagUnit = tagTree.getTagUnitById(tagId);
        if (tagUnit == null) {
            return result;
        }
        String actionName = tagUnit.getTagName();
        int parentId = tagUnit.getParentTagId();
        int appNameId = 0;
        if (parentId > 0) {
            appNameId = tagTree.getTagUnitById(parentId).getParentTagId();
        }

        String appName = null;
        int appTypeId = 0;
        String appTypeName = null;
        if (appNameId > 0) {
            appName = tagTree.getTagUnitById(appNameId).getTagName();
            appTypeId = tagTree.getTagUnitById(appNameId).getParentTagId();
        }
        if (appTypeId > 0) {
            appTypeName = tagTree.getTagUnitById(appTypeId).getTagName();
        }

        result = new Object[]{tagId, actionName, appName, appTypeName};
        return result;

    }

    /**
     * 传入 appid返回tagUnit
     *
     * @param appId
     * @return
     */
    public TagUnit getTagUnitById(int appId) {
        TagUnit appTag = null;
        if (appIdToTagIdMap.containsKey(appId)) {
            int tagId = appIdToTagIdMap.get(appId);
            appTag = tagTree.getTagUnitById(tagId);
        }
        return appTag;
    }

    @VisibleForTesting
    protected void printTree() {
        if (this.tagTree != null) {
            System.out.println(this.tagTree.toString());
        }
    }
}
