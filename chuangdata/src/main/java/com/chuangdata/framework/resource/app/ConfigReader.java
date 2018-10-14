package com.chuangdata.framework.resource.app;

import com.chuangdata.framework.encrypt.AESEncrypt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
    private static final AESEncrypt ENCRYPT = AESEncrypt.getInstance();

    public static void read(String configFilePath, MatcherInitializer initializer, boolean isEncrypted) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(configFilePath)));
        String line;
        while ((line = reader.readLine()) != null) {
            if (isEncrypted) {
                try {
                    line = ENCRYPT.decrypt(line);
                } catch (Exception e) {
                    continue;
                }
            }
            String[] info = split(line, ",");
            initializer.init(info);
        }
        reader.close();
    }

    private static String[] split(String line, String separator) {
        List<String> result = new ArrayList<String>();
        int index = 0;
        while (line.indexOf(separator, index) >= 0) {
            int end = line.indexOf(separator, index);
            result.add(line.substring(index, end));
            index = end + separator.length();
        }
        // add last section
        result.add(line.substring(index));
        return result.toArray(new String[result.size()]);
    }

}
