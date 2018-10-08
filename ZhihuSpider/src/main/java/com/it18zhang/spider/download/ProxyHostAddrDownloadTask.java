package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.proxy.*;
import com.it18zhang.spider.util.Constants;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.it18zhang.spider.proxy.ProxyHostPool.PROXY_HOST_QUEUE;

/**
 * 代理主机地址下载任务
 */
public class ProxyHostAddrDownloadTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(ProxyHostAddrDownloadTask.class);
	protected String url;
	private boolean useProxy;		//是否通过代理下载
	private ProxyHost proxyHost;	//当前线程使用的代理

	protected static ProxyHostDownloader downloader = ProxyHostDownloader.getInstance();

	public ProxyHostAddrDownloadTask(String url, boolean useProxy) {
		this.url = url;
		this.useProxy = useProxy;
	}

	public void run() {
		long requestStartTime = System.currentTimeMillis();
		HttpGet request = null;
		try {
			PageInfo pageInfo = null;
			if (useProxy) {
				request = new HttpGet(url);
				proxyHost = PROXY_HOST_QUEUE.take();
				if (!(proxyHost instanceof Direct)) {
					HttpHost proxy = new HttpHost(proxyHost.getIp(), proxyHost.getPort());
					request.setConfig(DownloadUtil.getRequestConfigBuilder().setProxy(proxy).build());
				}
				pageInfo = downloader.downloadPage(request);
			} else {
				pageInfo = downloader.downloadPage(url);
			}
			pageInfo.setProxyHost(proxyHost);
			int status = pageInfo.getStatusCode();
			long requestEndTime = System.currentTimeMillis();
			String logStr = Thread.currentThread().getName() + " " + getProxyAddr(proxyHost) + "  executing request " + pageInfo.getUrl() + " response statusCode:" + status + "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
			if (status == HttpStatus.SC_OK) {
				logger.debug(logStr);
				handle(pageInfo);
			} else {
				logger.error(logStr);
				Thread.sleep(100);
				retry();
			}
		}
		catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
		catch (IOException e) {
			retry();
		}
		finally {
			if (proxyHost != null) {
				proxyHost.setTimeInterval(Constants.SPIDER_IP_ACCESS_INTERVAL_MS);
				PROXY_HOST_QUEUE.add(proxyHost);
			}
			if (request != null) {
				request.releaseConnection();
			}
		}
	}

	/**
	 * retry
	 */
	public void retry() {
		downloader.getProxyDownloadThreadExecutor().execute(new ProxyHostAddrDownloadTask(url, Constants.SPIDER_PROXY_ENABLE));
	}

	public void handle(PageInfo pageInfo) {
		if (pageInfo.getHtml() == null || pageInfo.getHtml().equals("")) {
			return;
		}

		ProxyHostAddrParser parser = ProxyHostAddrParserFactory.getParser(url);
		List<ProxyHost> proxyHosts = parser.parse(pageInfo.getHtml());
		for (ProxyHost host : proxyHosts) {
			ProxyHostPool.lock.lock();
			boolean contains = ProxyHostPool.ALL_PROXY_HOSTS.contains(host);
			ProxyHostPool.lock.unlock();
			if (!contains) {
				ProxyHostPool.lock.lock();
				if (ProxyHostPool.ALL_PROXY_HOSTS.add(host)) {
					downloader.getProxyTestThreadExecutor().execute(new ProxyHostTestTask(host));
				}
				ProxyHostPool.lock.unlock();
			}
		}
	}

	private String getProxyAddr(ProxyHost proxyHost) {
		if (proxyHost == null) {
			return "";
		}
		return proxyHost.getIp() + ":" + proxyHost.getPort();
	}
}
