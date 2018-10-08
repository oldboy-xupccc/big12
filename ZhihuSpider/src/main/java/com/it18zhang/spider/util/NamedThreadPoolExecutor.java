package com.it18zhang.spider.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 带有名称的线程池，jdk自带的线程池不能携带名称
 */
public class NamedThreadPoolExecutor extends ThreadPoolExecutor {

	private String poolName ;

	public NamedThreadPoolExecutor(String poolName , int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		this(poolName ,corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
	}

	public NamedThreadPoolExecutor(String poolName , int corePoolSize, int maximumPoolSize) {
		this(poolName ,corePoolSize, maximumPoolSize, 0, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
	}

	public NamedThreadPoolExecutor(String poolName,int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.poolName = poolName ;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
}
