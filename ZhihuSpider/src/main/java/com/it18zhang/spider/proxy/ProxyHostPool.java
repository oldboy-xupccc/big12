package com.it18zhang.spider.proxy;

import com.it18zhang.spider.util.Constants;
import com.it18zhang.spider.util.DictUtil;
import com.it18zhang.spider.util.PropertiesUtil;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 代理主机池，国内国外有很多免费代理，比如
 * 西刺代理 http://www.xicidaili.com/
 * 秘密代理 http://www.mimiip.com/
 * 66ip代理 http://www.66ip.cn/
 */
public class ProxyHostPool {

	// 轻量级锁
	public final static ReentrantLock lock = new ReentrantLock();

	// 代理服务器集合
	public final static Set<ProxyHost> ALL_PROXY_HOSTS = new HashSet<ProxyHost>();

	// 代理服务器队列
	public final static DelayQueue<ProxyHost> PROXY_HOST_QUEUE = new DelayQueue();

	// 代理服务器与地址抽取类
	public final static Map<String, Class> PROXY_HOST_MAP = new HashMap<String, Class>();

	static {
		try {
			List<String> list = DictUtil.getValues("proxyserver") ;
			for(String url : list){
				String[] arr = url.split("\\.") ;
				String domainname = arr[0] ;
				String clz = PropertiesUtil.getString("proxyserver."+domainname+".addrextractor.class");
				PROXY_HOST_MAP.put(url , Class.forName(clz)) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PROXY_HOST_QUEUE.add(new Direct(Constants.SPIDER_IP_ACCESS_INTERVAL_MS));
	}
}
