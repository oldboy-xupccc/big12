package com.it18zhang.spider.proxy;

import com.it18zhang.spider.download.DownloadUtil;
import com.it18zhang.spider.download.ZhiHuDownloader;
import com.it18zhang.spider.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理主机地址存储任务,保存代理主机地址集合串行到磁盘
 */
public class ProxyHostAddrStoreTask implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(ProxyHostAddrStoreTask.class);

	public void run() {
		while (!ZhiHuDownloader.stop) {
			try {
				// store 代理主机地址的间隔时间(1分钟)
				Thread.sleep(1000 * 60 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ProxyHost[] proxyHosts = null;
			ProxyHostPool.lock.lock();
			try {
				proxyHosts = new ProxyHost[ProxyHostPool.ALL_PROXY_HOSTS.size()];
				int i = 0;
				for (ProxyHost p : ProxyHostPool.ALL_PROXY_HOSTS) {
					if (!p.isDiscard()) {
						proxyHosts[i++] = p;
					}
				}
			} finally {
				ProxyHostPool.lock.unlock();
			}

			DownloadUtil.serializeObject(proxyHosts, Constants.SPIDER_DOWNLOAD_PROXYPATH);
			logger.info("成功序列化" + proxyHosts.length + "个代理");
		}
	}
}
