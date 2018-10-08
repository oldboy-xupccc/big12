package com.it18zhang.spider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 资源工具类 , 读取资源文件，sql、sh等等。
 */
public class ResourcesUtil {
	//
	static ClassLoader loader;

	static{
		try {
			loader = Thread.currentThread().getContextClassLoader() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取资源串
	 */
	public static String getResourceAsString(String reource) throws IOException {
		return getResourceAsString(reource , Charset.defaultCharset().name()) ;
	}

	/**
	 * 读取资源串
	 */
	public static String getResourceAsString(String reource , String charset) throws IOException {
		InputStream in = getResourceAsInputStream(reource) ;
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		byte[] buf = new byte[1024] ;
		int len = -1 ;
		while((len = in.read(buf)) != -1){
			baos.write(buf , 0 , len);
		}
		baos.close();
		in.close();
		return new String(baos.toByteArray(), charset) ;
	}

	/**
	 * 读取资源流
	 */
	public static InputStream getResourceAsInputStream(String reource){
		InputStream in = loader.getResourceAsStream(reource) ;
		return in ;
	}
}
