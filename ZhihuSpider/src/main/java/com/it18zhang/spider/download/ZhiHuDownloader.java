package com.it18zhang.spider.download;

import com.it18zhang.spider.util.Constants;
import com.it18zhang.spider.util.NamedThreadPoolExecutor;
import com.it18zhang.spider.util.ThreadPoolFactory;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 知乎客户端，单例模式
 */
public class ZhiHuDownloader extends BaseDownloader {

	private static Logger logger = LoggerFactory.getLogger(ZhiHuDownloader.class);
	private volatile static ZhiHuDownloader instance;
	/**
	 * 统计用户数量
	 */
	public static AtomicInteger parseUserCount = new AtomicInteger(0);

	private static long startTime = System.currentTimeMillis();
	public static volatile boolean stop = false;

	public static ZhiHuDownloader getInstance() {
		if (instance == null) {
			synchronized (ZhiHuDownloader.class) {
				if (instance == null) {
					instance = new ZhiHuDownloader();
				}
			}
		}
		return instance;
	}

	/**
	 * 详情页下载线程池
	 */
	private NamedThreadPoolExecutor detailPageThreadPool;
	/**
	 * 列表页下载线程池
	 */
	private NamedThreadPoolExecutor listPageThreadPool;
	/**
	 * 详情列表页下载线程池
	 */
	private NamedThreadPoolExecutor detailListPageThreadPool;
	/**
	 * 答案页下载线程池
	 */
	private NamedThreadPoolExecutor answerPageThreadPool;
	/**
	 * request　header
	 * 获取列表页时，必须带上
	 */
	private static String authorization;

	private ZhiHuDownloader() {
		initDB();
		initThreadPool() ;

	}

	/**
	 * 初始化HttpClient
	 */
	public void initDB() {
		if (Constants.SPIDER_DB_ENABLE) {
			//TODO Dao
			// ZhiHuDao1Imp.DBTablesInit();
		}
	}

	/**
	 * 初始化线程池
	 */
	private void initThreadPool() {
		detailPageThreadPool = ThreadPoolFactory.newPool(
				Constants.SPIDER_THREADPOOL_DETAILPAGE_POOLNAME,
				Constants.SPIDER_THREADPOOL_DETAILPAGE_SIZE_CORE,
				Constants.SPIDER_THREADPOOL_DETAILPAGE_SIZE_MAX,
				Constants.SPIDER_THREADPOOL_DETAILPAGE_alive);

		listPageThreadPool = ThreadPoolFactory.newPool(
				Constants.SPIDER_THREADPOOL_LISTPAGE_POOLNAME,
				Constants.SPIDER_THREADPOOL_LISTPAGE_SIZE_CORE,
				Constants.SPIDER_THREADPOOL_LISTPAGE_SIZE_MAX,
				Constants.SPIDER_THREADPOOL_LISTPAGE_alive);

		detailListPageThreadPool = ThreadPoolFactory.newPool(
				Constants.SPIDER_THREADPOOL_DETAILLISTPAGE_POOLNAME,
				Constants.SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_CORE,
				Constants.SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_MAX,
				Constants.SPIDER_THREADPOOL_DETAILLISTPAGE_alive);

		new MonitorThread(detailPageThreadPool, "DetailPageDownloadThreadPool").start();
		new MonitorThread(listPageThreadPool, "ListPageDownloadThreadPool").start();
		new MonitorThread(detailListPageThreadPool, "DetailListPageThreadPool").start();
	}

	/**
	 * 开启爬虫
	 */
	public void startCrawl() {
		String startToken = Constants.SPIDER_ZHIHU_START_USER_TOKEN;
		String startUrl = String.format(Constants.SPIDER_ZHIHU_USER_FOLLOWEES_URL, startToken, 0);
		HttpGet request = new HttpGet(startUrl);
		detailListPageThreadPool.execute(new DetailListDownloadTask(request, Constants.SPIDER_PROXY_ENABLE));
		manageHttpClient();
	}

	/**
	 * 爬取用户回答中图片
	 */
	public void startCrawlAnswerPic(String userToken) {
		answerPageThreadPool = ThreadPoolFactory.newPool(
				Constants.SPIDER_THREADPOOL_ANSWERPAGE_POOLNAME,
				Constants.SPIDER_THREADPOOL_ANSWERPAGE_SIZE_CORE,
				Constants.SPIDER_THREADPOOL_ANSWERPAGE_SIZE_MAX,
				Constants.SPIDER_THREADPOOL_ANSWERPAGE_alive);

		new MonitorThread(answerPageThreadPool, "AnswerPageThreadPool").start();
		// 用户回答地址
		String startUrl = String.format(Constants.SPIDER_ZHIHU_USER_ANSWER_URL, userToken, 0);

		HttpRequestBase request = new HttpGet(startUrl);

		//TODO 图片下载
		//answerPageThreadPool.execute(new PicAnswerTask(request, true, userToken));
	}

	/**
	 * 初始化authorization
	 *
	 * @return
	 */
	private static void initAuthorization() {
		logger.info("初始化authoriztion中...");

		String content = null;

		GeneralDownloadTask task = new GeneralDownloadTask(Constants.SPIDER_ZHIHU_START_URL, true);
		task.run();
		content = task.getPageInfo().getHtml();

		Pattern pattern = Pattern.compile("https://static\\.zhihu\\.com/heifetz/main\\.app\\.([0-9]|[a-z])*\\.js");
		Matcher matcher = pattern.matcher(content);
		String jsSrc = null;
		if (matcher.find()) {
			jsSrc = matcher.group(0);
		} else {
			throw new RuntimeException("not find javascript url");
		}
		String jsContent = null;
		GeneralDownloadTask jsPageTask = new GeneralDownloadTask(jsSrc, true);
		jsPageTask.run();
		jsContent = jsPageTask.getPageInfo().getHtml();

		pattern = Pattern.compile("oauth (([0-9]|[a-z])+)");
		matcher = pattern.matcher(jsContent);
		if (matcher.find()) {
			String a = matcher.group(1);
			logger.info("初始化authoriztion完成");
			authorization = a;
		} else {
			throw new RuntimeException("not get authorization");
		}
	}

	public static String getAuthorization() {
		if (authorization == null) {
//            initAuthorization();
		}
		return authorization;
	}

	/**
	 * 管理知乎客户端
	 * 关闭整个爬虫
	 */
	public void manageHttpClient() {
		while (true) {
			// 下载网页数
			long downloadPageCount = detailListPageThreadPool.getTaskCount();

			if (downloadPageCount >= Constants.SPIDER_ZHIHU_DOWNLOAD_PAGE_COUNT
						&& !detailListPageThreadPool.isShutdown()) {
				stop = true;
				MonitorThread.stop = true;
				detailListPageThreadPool.shutdown();
			}
			if (detailListPageThreadPool.isTerminated()) {
				//关闭数据库连接
				Map<Thread, Connection> map = DetailListDownloadTask.getConnMap();
				for (Connection cn : map.values()) {
					try {
						if (cn != null && !cn.isClosed()) {
							cn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//关闭代理检测线程池
				ProxyHostDownloader.getInstance().getProxyTestThreadExecutor().shutdownNow();
				//关闭代理下载页线程池
				ProxyHostDownloader.getInstance().getProxyDownloadThreadExecutor().shutdownNow();

				break;
			}
			double costTime = (System.currentTimeMillis() - startTime) / 1000.0;//单位s
			logger.debug("抓取速率：" + parseUserCount.get() / costTime + "个/s");
//            logger.info("downloadFailureProxyPageSet size:" + ProxyHttpClient.downloadFailureProxyPageSet.size());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ThreadPoolExecutor getDetailPageThreadPool() {
		return detailPageThreadPool;
	}

	public ThreadPoolExecutor getListPageThreadPool() {
		return listPageThreadPool;
	}

	public ThreadPoolExecutor getDetailListPageThreadPool() {
		return detailListPageThreadPool;
	}

	public ThreadPoolExecutor getAnswerPageThreadPool() {
		return answerPageThreadPool;
	}
}
