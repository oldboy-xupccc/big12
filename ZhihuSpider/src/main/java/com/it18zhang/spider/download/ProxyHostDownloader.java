package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.proxy.ProxyHost;
import com.it18zhang.spider.proxy.ProxyHostAddrStoreTask;
import com.it18zhang.spider.proxy.ProxyHostPool;
import com.it18zhang.spider.util.Constants;
import com.it18zhang.spider.util.NamedThreadPoolExecutor;
import com.it18zhang.spider.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 代理服务器下载器
 */
public class ProxyHostDownloader extends BaseDownloader{

	private static final Logger logger = LoggerFactory.getLogger(ProxyHostDownloader.class);

	private volatile static ProxyHostDownloader instance;

	public static Set<PageInfo> downloadFailureProxyPageSet = new HashSet<PageInfo>(ProxyHostPool.PROXY_HOST_MAP.size());

	public static ProxyHostDownloader getInstance() {
		if (instance == null) {
			synchronized (ProxyHostDownloader.class) {
				if (instance == null) {
					instance = new ProxyHostDownloader();
				}
			}
		}
		return instance;
	}

	// 代理测试线程池
	private NamedThreadPoolExecutor proxyTestThreadExecutor;
	// 代理网站下载线程池
	private NamedThreadPoolExecutor proxyDownloadThreadExecutor;

	public ProxyHostDownloader() {
		initThreadPool();
		initProxyHost();
	}

	/**
	 * 初始化线程池
	 */
	private void initThreadPool() {
		proxyTestThreadExecutor = ThreadPoolFactory.newPool("proxyTestThreadExecutor" ,100,100,0) ;
		proxyDownloadThreadExecutor = ThreadPoolFactory.newPool("proxyDownloadThreadExecutor", 100, 100, 0) ;
		new MonitorThread(proxyTestThreadExecutor, "ProxyTestThreadPool").start();
		new MonitorThread(proxyDownloadThreadExecutor, "ProxyDownloadThreadExecutor").start();
	}

	/**
	 * 从磁盘文件读入代理主机的信息，初始化proxy主机到内存集合。
	 */
	private void initProxyHost() {
		try {
			ProxyHost[] arr = (ProxyHost[]) DownloadUtil.deserializeObject(Constants.SPIDER_DOWNLOAD_PROXYPATH);
			int usableProxyCount = 0;
			for (ProxyHost host : arr) {
				if (host == null) {
					continue;
				}
				host.setTimeInterval(Constants.SPIDER_IP_ACCESS_INTERVAL_MS);
				host.setFailureCount(0);
				host.setSuccessCount(0);
				long now = System.currentTimeMillis();
				//上次成功离现在少于一小时
				if (now - host.getLastSuccessTime() < 1000 * 60 * 60) {
					ProxyHostPool.PROXY_HOST_QUEUE.add(host);
					ProxyHostPool.ALL_PROXY_HOSTS.add(host);
					usableProxyCount++;
				}
			}
			logger.info("反序列化proxy成功，" + arr.length + "个代理,可用代理" + usableProxyCount + "个");
		} catch (Exception e) {
			logger.warn("反序列化proxy失败");
		}
	}

	/**
	 * 抓取代理
	 */
	public void startCrawl() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (String url : ProxyHostPool.PROXY_HOST_MAP.keySet()) {
						// 下载代理主机地址，不需要走代理。
						proxyDownloadThreadExecutor.execute(new ProxyHostAddrDownloadTask(url, false));
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000 * 60 * 60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		// 保存代理地址到磁盘
		new Thread(new ProxyHostAddrStoreTask()).start();
	}

	public ThreadPoolExecutor getProxyTestThreadExecutor() {
		return proxyTestThreadExecutor;
	}

	public ThreadPoolExecutor getProxyDownloadThreadExecutor() {
		return proxyDownloadThreadExecutor;
	}
}
