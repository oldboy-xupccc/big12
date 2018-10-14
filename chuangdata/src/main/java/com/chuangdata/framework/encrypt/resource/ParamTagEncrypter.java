package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class ParamTagEncrypter extends Encrypter {

    @Override
    public String convert(String line, boolean strict) throws Exception {
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length < 3) {
            return null;
        }
        String appNameId = info[0];
        String paramTypeId = info[1];
        String paramValue = info[2];
        String paramTagId = info[3];

        StringBuilder builder = new StringBuilder();
        if (strict) {
            builder.append(appNameId).append(SEPARATOR)
                    .append(paramTypeId).append(SEPARATOR)
                    .append(paramValue).append(SEPARATOR)
                    .append(paramTagId);
        } else {
            builder.append(appNameId).append(SEPARATOR)
                    .append(paramTypeId).append(SEPARATOR)
                    .append(paramValue).append(SEPARATOR)
                    .append(paramTagId);
        }
        return builder.toString();
    }

}
