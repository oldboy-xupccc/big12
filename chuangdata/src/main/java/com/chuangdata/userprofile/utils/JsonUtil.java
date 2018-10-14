package com.chuangdata.userprofile.utils;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * @author luxiaofeng
 * @date 2016-10-05
 */
public class JsonUtil {
    static Gson gson = new Gson();

    public static <T> String mapToJson(Map<Object, T> map) {
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }

    public static <T> String listToJson(List<T> list) {
        String jsonStr = gson.toJson(list);
        return jsonStr;
    }
}
