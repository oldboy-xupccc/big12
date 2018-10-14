package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class AppDomainEncrypter extends Encrypter {
    @Override
    public String convert(String line, boolean strict) throws Exception {
        // actually strict flag is useless here
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length == 0) {
            return null;
        }
        String id = info[0];
        String host = info[1];
        String encryptedHost = encodeByMD5(host);
        StringBuilder builder = new StringBuilder();
        builder.append(id).append(SEPARATOR)
                .append(encryptedHost).append(SEPARATOR)
                .append(info[2]); // search uri
        return builder.toString();
    }
}
