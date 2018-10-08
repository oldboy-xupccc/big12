package com.it18zhang.spider.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 */
public class DictUtil {
	private static Map<String,List<String>> cache ;
	static{
		try {
			cache = new HashMap<String, List<String>>() ;
			InputStream in = ResourcesUtil.getResourceAsInputStream("dict.config") ;
			BufferedReader br = new BufferedReader(new InputStreamReader(in)) ;
			String line = null ;
			List<String> values = null ;
			while((line = br.readLine()) != null){
				// 新条目
				if(!line.trim().equals("")){
					if(line.startsWith("[")){
						String itemName = line.substring(1 , line.length() - 1);
						values = new ArrayList<String>() ;
						cache.put(itemName ,values) ;
					}
					else{
						values.add(line) ;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getValues(String itemName){
		return cache.get(itemName) ;
	}

	public static String getValue(String itemName){
		return cache.get(itemName).get(0) ;
	}
}
