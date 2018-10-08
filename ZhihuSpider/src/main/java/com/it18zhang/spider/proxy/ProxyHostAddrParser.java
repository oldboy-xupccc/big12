package com.it18zhang.spider.proxy;

import java.util.List;

/**
 * 代理主机地址解析器
 */
public interface ProxyHostAddrParser {

	// 是否只要匿名代理
	static final boolean anonymous = true;

	// 解析html页面，抽取主机地址
	List<ProxyHost> parse(String html);
}
