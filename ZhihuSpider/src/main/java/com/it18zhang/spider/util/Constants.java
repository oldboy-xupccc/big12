package com.it18zhang.spider.util;

import java.util.List;

/**
 * 常量类
 */
public class Constants {


	// 是否启用db
	public final static String SPIDER_DB_ENABLE_NAME = "spider.db.enable";
	public final static boolean SPIDER_DB_ENABLE = PropertiesUtil.getBoolean(SPIDER_DB_ENABLE_NAME);

	// 是否启用代理
	public final static String SPIDER_PROXY_ENABLE_NAME = "spider.proxy.enable";
	public final static boolean SPIDER_PROXY_ENABLE = PropertiesUtil.getBoolean(SPIDER_PROXY_ENABLE_NAME);

	public final static String SOCKET_TIMEOUT_MS_NAME = "socket.timeout.ms";
	public final static int SOCKET_TIMEOUT_MS = PropertiesUtil.getInt(SOCKET_TIMEOUT_MS_NAME);

	public static final String SOCKET_CONNECT_TIMEOUT_MS_NAME = "socket.connect.timeout.ms" ;
	public static final int SOCKET_CONNECT_TIMEOUT_MS = PropertiesUtil.getInt(SOCKET_CONNECT_TIMEOUT_MS_NAME) ;

	public final static String SPIDER_USERAGENT_NAME = "spider.useragent";
	public final static String SPIDER_USERAGENT = PropertiesUtil.getString(SPIDER_USERAGENT_NAME);

	// 用户代理名称
	public final static String SPIDER_USERAGENTS_NAME = "useragents";
	public final static List<String> SPIDER_USERAGENTS = DictUtil.getValues(SPIDER_USERAGENTS_NAME);


	// threadoop_detailpage
	public final static String SPIDER_THREADPOOL_DETAILPAGE_POOLNAME_NAME = "spider.threadpool.detailpage.poolname";
	public final static String SPIDER_THREADPOOL_DETAILPAGE_POOLNAME = PropertiesUtil.getString(
			SPIDER_THREADPOOL_DETAILPAGE_POOLNAME_NAME);

	public final static String SPIDER_THREADPOOL_DETAILPAGE_SIZE_CORE_NAME = "spider.threadpool.detailpage.size.core";
	public final static int SPIDER_THREADPOOL_DETAILPAGE_SIZE_CORE = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILPAGE_SIZE_CORE_NAME);

	public final static String SPIDER_THREADPOOL_DETAILPAGE_SIZE_MAX_NAME = "spider.threadpool.detailpage.size.max";
	public final static int SPIDER_THREADPOOL_DETAILPAGE_SIZE_MAX = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILPAGE_SIZE_MAX_NAME);

	public final static String SPIDER_THREADPOOL_DETAILPAGE_ALIVE_NAME = "spider.threadpool.detailpage.alive";
	public final static int SPIDER_THREADPOOL_DETAILPAGE_alive = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILPAGE_ALIVE_NAME);

	// threadpool_listpage
	public final static String SPIDER_THREADPOOL_LISTPAGE_POOLNAME_NAME = "spider.threadpool.listpage.poolname";
	public final static String SPIDER_THREADPOOL_LISTPAGE_POOLNAME = PropertiesUtil.getString(
			SPIDER_THREADPOOL_LISTPAGE_POOLNAME_NAME);

	public final static String SPIDER_THREADPOOL_LISTPAGE_SIZE_CORE_NAME = "spider.threadpool.listpage.size.core";
	public final static int SPIDER_THREADPOOL_LISTPAGE_SIZE_CORE = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_LISTPAGE_SIZE_CORE_NAME);

	public final static String SPIDER_THREADPOOL_LISTPAGE_SIZE_MAX_NAME = "spider.threadpool.listpage.size.max";
	public final static int SPIDER_THREADPOOL_LISTPAGE_SIZE_MAX = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_LISTPAGE_SIZE_MAX_NAME);

	public final static String SPIDER_THREADPOOL_LISTPAGE_ALIVE_NAME = "spider.threadpool.listpage.alive";
	public final static int SPIDER_THREADPOOL_LISTPAGE_alive = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_LISTPAGE_ALIVE_NAME);

	// threadpool_detaillistpage
	public final static String SPIDER_THREADPOOL_DETAILLISTPAGE_POOLNAME_NAME = "spider.threadpool.detaillistpage.poolname";
	public final static String SPIDER_THREADPOOL_DETAILLISTPAGE_POOLNAME = PropertiesUtil.getString(
			SPIDER_THREADPOOL_DETAILLISTPAGE_POOLNAME_NAME);

	public final static String SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_CORE_NAME = "spider.threadpool.detaillistpage.size.core";
	public final static int SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_CORE = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_CORE_NAME);

	public final static String SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_MAX_NAME = "spider.threadpool.detaillistpage.size.max";
	public final static int SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_MAX = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILLISTPAGE_SIZE_MAX_NAME);

	public final static String SPIDER_THREADPOOL_DETAILLISTPAGE_ALIVE_NAME = "spider.threadpool.detaillistpage.alive";
	public final static int SPIDER_THREADPOOL_DETAILLISTPAGE_alive = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_DETAILLISTPAGE_ALIVE_NAME);

	// threadpool_answerpage
	public final static String SPIDER_THREADPOOL_ANSWERPAGE_POOLNAME_NAME = "spider.threadpool.answerpage.poolname";
	public final static String SPIDER_THREADPOOL_ANSWERPAGE_POOLNAME = PropertiesUtil.getString(
			SPIDER_THREADPOOL_ANSWERPAGE_POOLNAME_NAME);

	public final static String SPIDER_THREADPOOL_ANSWERPAGE_SIZE_CORE_NAME = "spider.threadpool.answerpage.size.core";
	public final static int SPIDER_THREADPOOL_ANSWERPAGE_SIZE_CORE = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_ANSWERPAGE_SIZE_CORE_NAME);

	public final static String SPIDER_THREADPOOL_ANSWERPAGE_SIZE_MAX_NAME = "spider.threadpool.answerpage.size.max";
	public final static int SPIDER_THREADPOOL_ANSWERPAGE_SIZE_MAX = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_ANSWERPAGE_SIZE_MAX_NAME);

	public final static String SPIDER_THREADPOOL_ANSWERPAGE_ALIVE_NAME = "spider.threadpool.answerpage.alive";
	public final static int SPIDER_THREADPOOL_ANSWERPAGE_alive = PropertiesUtil.getInt(
			SPIDER_THREADPOOL_ANSWERPAGE_ALIVE_NAME);

	// 监控器线程间隔秒数
	public static final String SPIDER_MONITOR_THREAD_INTERVAL_MS_NAME = "spider.monitor.thread.interval.ms";
	public static final int SPIDER_MONITOR_THREAD_INTERVAL_MS = PropertiesUtil.getInt(
			SPIDER_MONITOR_THREAD_INTERVAL_MS_NAME);

	// zhihu账号
	public static final String SPIDER_ZHIHU_ACCOUNT_NAME = "spider.zhihu.account" ;
	public static final String SPIDER_ZHIHU_ACCOUNT = PropertiesUtil.getString(SPIDER_ZHIHU_ACCOUNT_NAME) ;

	// 密码
	public static final String SPIDER_ZHIHU_PASSWORD_NAME = "spider.zhihu.password" ;
	public static final String SPIDER_ZHIHU_PASSWORD = PropertiesUtil.getString(SPIDER_ZHIHU_PASSWORD_NAME) ;

	// 用户令牌
	public static final String SPIDER_ZHIHU_START_USER_TOKEN_NAME = "spider.zhihu.start_user_token" ;
	public static final String SPIDER_ZHIHU_START_USER_TOKEN = PropertiesUtil.getString(
			SPIDER_ZHIHU_START_USER_TOKEN_NAME) ;

	// 起始url
	public static final String SPIDER_ZHIHU_START_URL_NAME = "spider.zhihu.start_url";
	public static final String SPIDER_ZHIHU_START_URL = PropertiesUtil.getString(SPIDER_ZHIHU_START_URL_NAME);

	// 粉丝url
	public static final String SPIDER_ZHIHU_USER_FOLLOWEES_URL_NAME = "spider.zhihu.user_followees_url" ;
	public static final String SPIDER_ZHIHU_USER_FOLLOWEES_URL = PropertiesUtil.getString(
			SPIDER_ZHIHU_USER_FOLLOWEES_URL_NAME) ;
	// 回答
	public static final String SPIDER_ZHIHU_USER_ANSWER_URL_NAME = "spider.zhihu.user_answer_url" ;
	public static final String SPIDER_ZHIHU_USER_ANSWER_URL = PropertiesUtil.getString(
			SPIDER_ZHIHU_USER_ANSWER_URL_NAME) ;

	// 单个ip请求间隔
	public static final String SPIDER_IP_ACCESS_INTERVAL_MS_NAME = "spider.ip.access.interval.ms" ;
	public static final int SPIDER_IP_ACCESS_INTERVAL_MS = PropertiesUtil.getInt(SPIDER_IP_ACCESS_INTERVAL_MS_NAME) ;


	// 代理服务器失败次数
	public static final String SPIDER_PROXYSERVER_DISCARD_FAILURECOUNT_NAME = "spider.proxyserver.discard.failurecount" ;
	public static final int SPIDER_PROXYSERVER_DISCARD_FAILURECOUNT = PropertiesUtil.getInt(
			SPIDER_PROXYSERVER_DISCARD_FAILURECOUNT_NAME);

	public static final String SPIDER_PROXYSERVER_DISCARD_FAILURERATIO_NAME = "spider.proxyserver.discard.failureratio";
	public static final float SPIDER_PROXYSERVER_DISCARD_FAILURERATIO = PropertiesUtil.getFloat(
			SPIDER_PROXYSERVER_DISCARD_FAILURERATIO_NAME);

	// 下载失败重试间隔毫秒数
	public static final String SPIDER_DOWNLOAD_RETRY_INTERVAL_MS_NAME = "spider.download.retry.interval.ms" ;
	public static final int SPIDER_DOWNLOAD_RETRY_INTERVAL_MS = PropertiesUtil.getInt(
			SPIDER_DOWNLOAD_RETRY_INTERVAL_MS_NAME) ;

	public static final String SPIDER_ZHIHU_DOWNLOAD_PAGE_COUNT_NAME = "spider.zhihu.download.page.count" ;
	public static final int SPIDER_ZHIHU_DOWNLOAD_PAGE_COUNT =PropertiesUtil.getInt(
			SPIDER_ZHIHU_DOWNLOAD_PAGE_COUNT_NAME) ;

	public static final String SPIDER_DOWNLOAD_PROXYPATH_NAME = "spider.download.proxyPath" ;
	public static final String SPIDER_DOWNLOAD_PROXYPATH = PropertiesUtil.getString(SPIDER_DOWNLOAD_PROXYPATH_NAME) ;

}
