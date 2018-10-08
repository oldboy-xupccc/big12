package com.it18zhang.spider.domain;

import com.it18zhang.spider.proxy.ProxyHost;

/**
 * 页面信息
 */
public class PageInfo {
	private String url;					// URL地址
	private int statusCode;				// 响应状态码
	private String html;				// html代码
	private ProxyHost proxyHost;	// 代理

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public ProxyHost getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(ProxyHost proxyHost) {
		this.proxyHost = proxyHost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PageInfo pageInfo = (PageInfo) o;

		return url.equals(pageInfo.url);
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}
}
