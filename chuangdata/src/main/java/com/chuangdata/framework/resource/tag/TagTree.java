package com.chuangdata.framework.resource.tag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务标签树
 *
 * @author luxiaofeng
 */
public class TagTree {
    private Map<Integer, TagUnit> tagTree;

    public TagTree() {
        tagTree = new HashMap<Integer, TagUnit>();
    }

    public void addTagToTree(TagUnit tagUnit) {
        tagTree.put(tagUnit.getTagId(), tagUnit);
    }

    public void buildTagTree() {
        for (int tagId : tagTree.keySet()) {
            TagUnit tagUnit = tagTree.get(tagId);
            int tagLevelId = tagUnit.getTagLevelId();
            if (tagLevelId > 1) {
                // not top level, then must and only have one parent
                int parentTagId = tagUnit.getParentTagId();
                TagUnit parentTagUnit = tagTree.get(parentTagId);
                if (parentTagUnit != null) {
                    parentTagUnit.addChild(tagId);
                } else {
                    // should never get here
                }
            }
        }
    }

    public TagUnit getTagUnitById(int tagId) {
        return tagTree.get(tagId);
    }

    public int size() {
        return this.tagTree.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int tagId : tagTree.keySet()) {
            TagUnit tagUnit = tagTree.get(tagId);
            int tagLevelId = tagUnit.getTagLevelId();
            if (tagLevelId == 1) {
                appendTag(builder, tagUnit);
            }
        }
        return builder.toString();
    }

    private void appendTag(StringBuilder builder, TagUnit tagUnit) {
        int tagLevelId = tagUnit.getTagLevelId();
        for (int i = 1; i < tagLevelId - 1; i++) {
            builder.append("|  ");
        }
        if (tagLevelId > 1) {
            builder.append("|--");
        }
        builder.append(tagUnit.getTagId()).append("\n");
        Collection<Integer> childrenTags = tagUnit.getChildrenTags();
        for (Integer tagId : childrenTags) {
            TagUnit childTagUnit = tagTree.get(tagId);
            appendTag(builder, childTagUnit);
        }
    }
}
