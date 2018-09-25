package com.oldboy.java.qq.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 属性工具类
 */
public class PropertiesUtil {
	private static Properties prop ;
	static{
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties") ;
			prop = new Properties();
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到字符串值
	 */
	public static String getStringValue(String name){
		return prop.getProperty(name) ;
	}

	/**
	 * 得到字符串值
	 */
	public static int getIntValue(String name){
		return Integer.parseInt(prop.getProperty(name));
	}

	/**
	 * 得到字符串值
	 */
	public static boolean getBooleanValue(String name){
		return prop.getProperty(name).toLowerCase().equals("true");
	}

}
