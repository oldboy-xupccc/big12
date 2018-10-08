package com.it18zhang.spider.proxy;

/**
 * 不经过代理服务器，直连。
 */
public class Direct extends ProxyHost {

	public Direct(String ip, int port, long timeInterval) {
		super(ip, port, timeInterval);
	}

	public Direct(long delayTime) {
		this("", 0, delayTime);
	}
}
