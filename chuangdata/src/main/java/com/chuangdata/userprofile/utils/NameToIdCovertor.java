package com.chuangdata.userprofile.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luxiaofeng
 * @date 2016-10-05
 */
public class NameToIdCovertor {

    private static Map<String, Integer> provinceMap = new HashMap<String, Integer>();
    private static Map<String, Integer> netWorkTypeMap = new HashMap<String, Integer>();

    static {
        provinceMap.put("ah", 1);
        provinceMap.put("am", 2);
        provinceMap.put("bj", 3);
        provinceMap.put("cq", 4);
        provinceMap.put("fj", 5);
        provinceMap.put("gd", 6);
        provinceMap.put("gs", 7);
        provinceMap.put("gx", 8);
        provinceMap.put("gz", 9);
        provinceMap.put("hb1", 10);
        provinceMap.put("hb2", 11);
        provinceMap.put("hl", 12);
        provinceMap.put("hn1", 13);
        provinceMap.put("hn2", 14);
        provinceMap.put("hn3", 15);
        provinceMap.put("jl", 16);
        provinceMap.put("js", 17);
        provinceMap.put("jt", 18);
        provinceMap.put("jx", 19);
        provinceMap.put("ln", 20);
        provinceMap.put("nm", 21);
        provinceMap.put("nx", 22);
        provinceMap.put("qh", 23);
        provinceMap.put("sc", 24);
        provinceMap.put("sd", 25);
        provinceMap.put("sh", 26);
        provinceMap.put("sx1", 27);
        provinceMap.put("sx2", 28);
        provinceMap.put("tj", 29);
        provinceMap.put("tw", 30);
        provinceMap.put("xg", 31);
        provinceMap.put("xj", 32);
        provinceMap.put("xz", 33);
        provinceMap.put("yn", 34);
        provinceMap.put("zj", 35);
        //  inintial networkType map
        netWorkTypeMap.put("ps", 0);
        netWorkTypeMap.put("lte", 0);
        netWorkTypeMap.put("cy", 1);

    }

    public static int getProvinceIdByName(String proStr) {
        return provinceMap.get(proStr);
    }

    public static int getNetWorkTypeIdByName(String proStr) {
        return netWorkTypeMap.get(proStr);
    }

}
