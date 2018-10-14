package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class AppHostEncrypter extends Encrypter {
    @Override
    public String convert(String line, boolean strict) throws Exception {
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length == 0) {
            return null;
        }
        String id = info[0];
        String host = info[1];
        String encryptedHost = encodeByMD5(host);
        StringBuilder builder = new StringBuilder();
        if (strict) {
            builder.append(id).append(SEPARATOR)
                    .append(encryptedHost).append(SEPARATOR)
                    .append(info[2]).append(SEPARATOR)
                    .append(0);
        } else {
            builder.append(id).append(SEPARATOR)
                    .append(encryptedHost).append(SEPARATOR)
                    .append(info[2]).append(SEPARATOR)
                    .append(info[3]);
        }
        return builder.toString();
    }
}
