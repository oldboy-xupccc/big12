package com.it18zhang.spider.proxy;

import com.it18zhang.spider.util.PropertiesUtil;
import com.it18zhang.spider.util.ResourcesUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 代理主机地址解析器工厂
 */
public class ProxyHostAddrParserFactory {

	private static Map<String,ProxyHostAddrParser> cache = new HashMap<String, ProxyHostAddrParser>();
	private static ReentrantLock lock = new ReentrantLock() ;

	/**
	 * 通过url地址获取解析器
	 */
	public static ProxyHostAddrParser getParser(String url){
		// 属性前缀
		String prefix = "spider.proxyhost_addr_parser." ;
		// 域名
		String domainName = getDomainName(url) ;
		// 属性key
		String key = prefix + domainName ;

		//
		ProxyHostAddrParser parser = cache.get(domainName) ;

		if(parser == null){
			lock.lock();
			parser = cache.get(domainName);
			if (parser == null) {
				String clz = PropertiesUtil.getString(key);
				try {
					Class clazz = Class.forName(clz);
					parser = (ProxyHostAddrParser) clazz.newInstance();
					cache.put(domainName, parser);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			lock.unlock();
		}
		return parser ;
	}

	/**
	 * 提取域名
	 */
	private static String getDomainName(String url){
		return url.split("\\.")[1] ;
	}
}
