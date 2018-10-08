package com.it18zhang.spider.proxy;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.download.ZhiHuDownloader;
import com.it18zhang.spider.util.Constants;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 代理主机地址可用性测试任务，确保主机是否可用
 */
public class ProxyHostTestTask implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(ProxyHostTestTask.class);
	private ProxyHost proxyHost;

	public ProxyHostTestTask(ProxyHost proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void run() {
		long startTime = System.currentTimeMillis();
		// get请求
		HttpGet request = new HttpGet(Constants.SPIDER_ZHIHU_START_URL);

		try {
			RequestConfig requestConfig = RequestConfig.custom()
												  .setSocketTimeout(Constants.SOCKET_TIMEOUT_MS)
												  .setConnectTimeout(Constants.SOCKET_TIMEOUT_MS)
												  .setConnectionRequestTimeout(Constants.SOCKET_TIMEOUT_MS)
												  .setProxy(new HttpHost(proxyHost.getIp(), proxyHost.getPort()))
												  .setCookieSpec(CookieSpecs.STANDARD)
												  .build();
			request.setConfig(requestConfig);

			PageInfo pageInfo = ZhiHuDownloader.getInstance().downloadPage(request);
			//
			long endTime = System.currentTimeMillis();

			String logStr = Thread.currentThread().getName() + " " + proxyHost.getProxyStr() + "  executing request " + pageInfo.getUrl() + " response statusCode:" + pageInfo.getStatusCode() + "  request cost time:" + (endTime - startTime) + "ms";
			if (pageInfo == null || pageInfo.getStatusCode() != 200) {
				logger.warn(logStr);
				return;
			}
			request.releaseConnection();
			logger.debug(proxyHost.toString() + "---------" + pageInfo.toString());
			logger.debug(proxyHost.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
			// 添加代理主机到代理主机队列
			ProxyHostPool.PROXY_HOST_QUEUE.add(proxyHost);
		}
		catch (IOException e) {
			logger.debug("IOException:", e);
		}
		finally {
			if (request != null) {
				request.releaseConnection();
			}
		}
	}

	private String getProxyStr() {
		return proxyHost.getIp() + ":" + proxyHost.getPort();
	}
}