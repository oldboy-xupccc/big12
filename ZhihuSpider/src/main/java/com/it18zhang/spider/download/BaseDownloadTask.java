package com.it18zhang.spider.download;

import com.it18zhang.spider.dao.ZhihuDao;
import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.proxy.Direct;
import com.it18zhang.spider.proxy.ProxyHost;
import com.it18zhang.spider.proxy.ProxyHostPool;
import com.it18zhang.spider.util.Constants;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 基本下载任务，完成网页的下载，解析过程交给子类完成。
 */
public abstract class BaseDownloadTask implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(BaseDownloadTask.class);

	protected String url;

	protected HttpRequestBase request;

	// 是否使用代理下载
	protected boolean useProxy;

	// 当前线程使用的代理
	protected ProxyHost proxyHost;

	protected static ZhihuDao zhihudao;

	protected static ZhiHuDownloader downloader = ZhiHuDownloader.getInstance();

	public BaseDownloadTask(String url, boolean useProxy) {
		this.url = url;
		this.useProxy = useProxy;
	}

	public BaseDownloadTask(HttpRequestBase request, boolean useProxy) {
		this.request = request;
		this.useProxy = useProxy;
	}

	public void run() {
		long startTime = 0L;

		HttpGet request = null;
		try {
			PageInfo pageInfo = null;

			// url非空
			if (url != null) {
				// 使用代理
				if (useProxy) {
					request = new HttpGet(url);
					// 从队列中取出一个代理
					proxyHost = ProxyHostPool.PROXY_HOST_QUEUE.take();
					if (!(proxyHost instanceof Direct)) {
						// 使用代理服务器构造HttpHost对象
						HttpHost proxy = new HttpHost(proxyHost.getIp(), proxyHost.getPort());
						// 在请求中设置代理主机
						request.setConfig(DownloadUtil.getRequestConfigBuilder().setProxy(proxy).build());
					}
					// 计时
					startTime = System.currentTimeMillis();
					// 开始下载
					pageInfo = downloader.downloadPage(request);

				}
				// 不使用代理
				else {
					startTime = System.currentTimeMillis();
					pageInfo = downloader.downloadPage(url);
				}
			}
			// 再看request是否为null
			else if (this.request != null) {
				if (useProxy) {
					proxyHost = ProxyHostPool.PROXY_HOST_QUEUE.take();
					if (!(proxyHost instanceof Direct)) {
						HttpHost proxy = new HttpHost(proxyHost.getIp(), proxyHost.getPort());
						this.request.setConfig(DownloadUtil.getRequestConfigBuilder().setProxy(proxy).build());
					}
					startTime = System.currentTimeMillis();
					pageInfo = downloader.downloadPage(this.request);
				} else {
					startTime = System.currentTimeMillis();
					pageInfo = downloader.downloadPage(this.request);
				}
			}

			// 结束时间
			long endTime = System.currentTimeMillis();

			// 设置所用的代理服务器
			pageInfo.setProxyHost(proxyHost);

			// 状态码
			int statusCode = pageInfo.getStatusCode();
			String logStr = Thread.currentThread().getName() + " " + proxyHost + "  executing request " + pageInfo.getUrl() + " response statusCode:" + statusCode + "  request cost time:" + (endTime - startTime) + "ms";
			// ok
			if (statusCode == HttpStatus.SC_OK) {
				if (pageInfo.getHtml().contains("zhihu") && !pageInfo.getHtml().contains("安全验证")) {
					logger.debug(logStr);
					// 统计信息
					proxyHost.setSuccessCount(proxyHost.getSuccessCount() + 1);
					proxyHost.setSuccessTotalTime(proxyHost.getSuccessTotalTime() + (endTime - startTime));
					double avgTime = (proxyHost.getSuccessTotalTime() + 0.0) / proxyHost.getSuccessCount();
					proxyHost.setSuccessAvgTime(avgTime);
					proxyHost.setLastSuccessTime(System.currentTimeMillis());
					handle(pageInfo);
				}
				else {
					// 代理异常，没有正确返回目标url
					logger.warn("proxy exception:" + proxyHost.toString());
				}

			}
			/**
			 * 401--不能通过验证
			 */
			else if (statusCode == 404 || statusCode == 401 || statusCode == 410) {
				logger.warn(logStr);
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
			if (proxyHost != null) {
				// 累计失败次数
				proxyHost.setFailureCount(proxyHost.getFailureCount() + 1);
			}
			if (!downloader.getDetailListPageThreadPool().isShutdown()) {
				retry();
			}
		}
		finally {
			if (this.request != null) {
				this.request.releaseConnection();
			}
			if (request != null) {
				request.releaseConnection();
			}
			if (proxyHost != null && !proxyHost.isDiscard()) {
				proxyHost.setTimeInterval(Constants.SPIDER_IP_ACCESS_INTERVAL_MS);
				ProxyHostPool.PROXY_HOST_QUEUE.add(proxyHost);
			}
		}
	}

	protected abstract void handle(PageInfo pageInfo);

	protected abstract void retry();
}
