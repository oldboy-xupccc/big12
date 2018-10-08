package com.it18zhang.spider.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 属性工具类
 */
public class PropertiesUtil {
	//
	static Properties prop ;

	static{
		try {
			prop = new Properties() ;
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			prop.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到字符串
	 */
	public static String getString(String propName){
		return prop.getProperty(propName) ;
	}

	/**
	 * 得到字符串
	 */
	public static int getInt(String propName){
		return Integer.parseInt(prop.getProperty(propName)) ;
	}

	/**
	 * 读取boolean值
	 */
	public static boolean getBoolean(String propName){
		return getString(propName).toLowerCase().equals("true") ;
	}

	/**
	 * 读取boolean值
	 */
	public static float getFloat(String propName){
		return Float.parseFloat(getString(propName));
	}
}
