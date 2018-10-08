package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.util.Constants;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 详情列表页的下载任务
 */
public class DetailListDownloadTask extends BaseDownloadTask{
	private static Logger logger = LoggerFactory.getLogger(DetailListDownloadTask.class);
	private static ZhihuUserListParser parser = new ZhihuUserListParser();
	/**
	 * Thread-数据库连接
	 */
	private static Map<Thread, Connection> connMap = new ConcurrentHashMap<Thread, Connection>();


	public DetailListDownloadTask(HttpRequestBase request, boolean useProxy) {
		super(request, useProxy);
	}

	protected void retry() {
		downloader.getDetailListPageThreadPool().execute(new DetailListDownloadTask(request, Constants.SPIDER_PROXY_ENABLE));
	}

	protected void handle(PageInfo page) {
//		if (!page.getHtml().startsWith("{\"paging\"")) {
//			//代理异常，未能正确返回目标请求数据，丢弃
//			currentProxy = null;
//			return;
//		}
//		List<ZhihuUserInfo> list = parser.parseListPage(page);
//		for (ZhihuUserInfo u : list) {
//			logger.info("解析用户成功:" + u.toString());
//			if (Constants.SPIDER_DB_ENABLE) {
//				Connection cn = getConnection();
//				if (zhiHuDao1.insertUser(cn, u)) {
//					parseUserCount.incrementAndGet();
//				}
//				for (int j = 0; j < u.getFollowees() / 20; j++) {
//					if (downloader.getDetailListPageThreadPool().getQueue().size() > 1000) {
//						continue;
//					}
//					String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
//					if (zhiHuDao1.insertUrl(cn, Md5Util.Convert2Md5(
//							nextUrl)) || downloader.getDetailListPageThreadPool().getActiveCount() == 1) {
//						//防止死锁
//						HttpGet request = new HttpGet(nextUrl);
////                        request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
//						downloader.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
//					}
//				}
//			} else if (!Config.dbEnable || downloader.getDetailListPageThreadPool().getActiveCount() == 1) {
//				parseUserCount.incrementAndGet();
//				for (int j = 0; j < u.getFollowees() / 20; j++) {
//					String nextUrl = String.format(USER_FOLLOWEES_URL, u.getUserToken(), j * 20);
//					HttpGet request = new HttpGet(nextUrl);
////                    request.setHeader("authorization", "oauth " + ZhiHuHttpClient.getAuthorization());
//					downloader.getDetailListPageThreadPool().execute(new DetailListPageTask(request, true));
//				}
//			}
//		}
	}

	/**
	 * 每个thread维护一个Connection
	 *
	 * @return
	 */
	private Connection getConnection() {
		Thread currentThread = Thread.currentThread();
		Connection cn = null;
//		if (!connMap.containsKey(currentThread)) {
//			cn = ConnectionManager.createConnection();
//			connMap.put(currentThread, cn);
//		} else {
//			cn = connMap.get(currentThread);
//		}
		return cn;
	}

	public static Map<Thread, Connection> getConnMap() {
		return connMap;
	}
}
