package com.it18zhang.spider.util;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂
 */
public class ThreadPoolFactory {
	/**
	 * 新建线程池
	 */
	public static NamedThreadPoolExecutor newPool(String poolName , int coreSize , int maxSize , long aliveTime){
		return new NamedThreadPoolExecutor(poolName , coreSize,maxSize, aliveTime, TimeUnit.SECONDS , new LinkedBlockingQueue<Runnable>()){
			protected void beforeExecute(Thread t, Runnable r) {
				super.beforeExecute(t, r);
			}
		} ;
	}
}
