package com.oldboy.java.qq.util;

/**
 * 常量接口
 */
public class IConstants {
	//阻塞模式
	private static final String QQ_SERVER_CHANNEl_BLOCKING_MODE_NAME = "qq.server.channel.blocking.mode" ;
	public static final boolean QQ_SERVER_CHANNEl_BLOCKING_MODE = PropertiesUtil.getBooleanValue(QQ_SERVER_CHANNEl_BLOCKING_MODE_NAME) ;

	//服务器绑定ip
	private static final String QQ_SERVER_BIND_HOST_NAME = "qq.server.bind.host" ;
	public static final String QQ_SERVER_BIND_HOST = PropertiesUtil.getStringValue(QQ_SERVER_BIND_HOST_NAME) ;

	//服务器绑定端口
	private static final String QQ_SERVER_BIND_PORT_NAME = "qq.server.bind.port";
	public static final int QQ_SERVER_BIND_PORT = PropertiesUtil.getIntValue(QQ_SERVER_BIND_PORT_NAME);

	//线程池线程数
	private static final String QQ_SERVER_THREAD_POOL_CORES_NAME = "qq.server.thread.pool.cores";
	public static final int QQ_SERVER_THREAD_POOL_CORES= PropertiesUtil.getIntValue(QQ_SERVER_THREAD_POOL_CORES_NAME);


	//client绑定远程ip
	private static final String QQ_CLIENT_SERVER_IP_NAME = "qq.client.server.ip";
	public static final String QQ_CLIENT_SERVER_IP = PropertiesUtil.getStringValue(QQ_CLIENT_SERVER_IP_NAME);

	//服务器绑定端口
	private static final String QQ_CLIENT_SERVER_PORT_NAME = "qq.client.server.port";
	public static final int QQ_CLIENT_SERVER_PORT = PropertiesUtil.getIntValue(QQ_CLIENT_SERVER_PORT_NAME);

	private static final String QQ_SERVER_ROUNDROBIN_INTERVAL_SECONDS_NAME = "qq.server.roundrobin.interval.seconds";
	public static final int QQ_SERVER_ROUNDROBIN_INTERVAL_SECONDS = PropertiesUtil.getIntValue(QQ_SERVER_ROUNDROBIN_INTERVAL_SECONDS_NAME);




}
