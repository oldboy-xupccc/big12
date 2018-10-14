package com.chuangdata.framework.resource.tag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author luxiaofeng
 */
public class TagUnit {
    private int tagId;
    private String tagName;
    private int tagLevelId;
    private int parentTagId;
    private int tagTypeId;
    private int oldTagId;

    private Set<Integer> childrenTags = new HashSet<Integer>();

    public TagUnit(int tagId, String tagName, int tagLevelId, int parentTagId, int tagTypeId, int oldTagId) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.tagLevelId = tagLevelId;
        this.parentTagId = parentTagId;
        this.tagTypeId = tagTypeId;
        this.oldTagId = oldTagId;
    }

    public void addChild(int childTagId) {
        if (!this.childrenTags.contains(childTagId)) {
            this.childrenTags.add(childTagId);
        }
    }

    public Collection<Integer> getChildrenTags() {
        return this.childrenTags;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagLevelId() {
        return tagLevelId;
    }

    public void setTagLevelId(int tagLevelId) {
        this.tagLevelId = tagLevelId;
    }

    public int getParentTagId() {
        return parentTagId;
    }

    public void setParentTagId(int parentTagId) {
        this.parentTagId = parentTagId;
    }

    public int getTagTypeId() {
        return tagTypeId;
    }

    public void setTagTypeId(int tagTypeId) {
        this.tagTypeId = tagTypeId;
    }

    public int getOldTagId() {
        return oldTagId;
    }

    public void setOldTagId(int oldTagId) {
        this.oldTagId = oldTagId;
    }


}
