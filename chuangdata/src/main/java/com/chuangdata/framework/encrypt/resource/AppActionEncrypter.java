package com.chuangdata.framework.encrypt.resource;

/**
 * @author luxiaofeng
 */
public class AppActionEncrypter extends Encrypter {

    @Override
    public String convert(String line, boolean strict) throws Exception {
        String[] info = split(line, SEPARATOR);
        if (info == null || info.length < 3) {
            return null;
        }
        String id = info[0];
        String host = info[1];
        String encryptedHost = encodeByMD5(host);
        String uri = info[2];
        StringBuilder builder = new StringBuilder();
        if (strict) {
            builder.append(id).append(SEPARATOR)
                    .append(encryptedHost).append(SEPARATOR)
                    .append(uri).append(SEPARATOR)
                    .append(0).append(SEPARATOR) // detail action
                    .append(0).append(SEPARATOR) // action name
                    .append(0).append(SEPARATOR) // interest
                    .append(0); // app
        } else {
            builder.append(id).append(SEPARATOR)
                    .append(encryptedHost).append(SEPARATOR)
                    .append(uri).append(SEPARATOR)
                    .append(info[3]).append(SEPARATOR) // detail action
                    .append(info[4]).append(SEPARATOR) // action name
                    .append(info[5]).append(SEPARATOR) // interest
                    .append(info[6]); // app
        }
        return builder.toString();
    }
}
