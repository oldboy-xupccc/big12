package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class AppParamEncrypter extends Encrypter {

    @Override
    public String convert(String line, boolean strict) throws Exception {
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length < 4) {
            return null;
        }
        String id = info[0];
        String actionId = info[1];
        String param = info[2];
        // String paramTypeId = info[3];
        String isUserId = info[4];
        StringBuilder builder = new StringBuilder();
        if (strict) {
            builder.append(id).append(SEPARATOR)
                    .append(actionId).append(SEPARATOR)
                    .append(param).append(SEPARATOR)
                    .append(0).append(SEPARATOR)
                    .append(isUserId);
        } else {
            builder.append(id).append(SEPARATOR)
                    .append(actionId).append(SEPARATOR)
                    .append(param).append(SEPARATOR)
                    .append(info[3]).append(SEPARATOR)
                    .append(isUserId);
        }
        return builder.toString();
    }

}
