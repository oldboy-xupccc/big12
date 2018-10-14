package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class TagResourceEncryter extends Encrypter {

    @Override
    public String convert(String line, boolean strict) throws Exception {
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length < 7) {
            return null;
        }
        String id = info[0];
        String tagName = info[1];
        String tagLevelId = info[2];
        String parentId = info[3];
        String tagTypeId = info[4];
        String oldTypeId = info[5];
        String deleted = info[6];
        StringBuilder builder = new StringBuilder();
        if (strict) {
            builder.append(id).append(SEPARATOR)
                    .append(tagName).append(SEPARATOR)
                    .append(tagLevelId).append(SEPARATOR)
                    .append(parentId).append(SEPARATOR)
                    .append(tagTypeId).append(SEPARATOR)
                    .append(oldTypeId).append(SEPARATOR)
                    .append(0);
        } else {
            builder.append(id).append(SEPARATOR)
                    .append(tagName).append(SEPARATOR)
                    .append(tagLevelId).append(SEPARATOR)
                    .append(parentId).append(SEPARATOR)
                    .append(tagTypeId).append(SEPARATOR)
                    .append(oldTypeId).append(SEPARATOR)
                    .append(deleted);
        }
        return builder.toString();
    }
}
