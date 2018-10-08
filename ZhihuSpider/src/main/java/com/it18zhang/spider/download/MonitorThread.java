package com.it18zhang.spider.download;

import com.it18zhang.spider.util.Constants;
import com.it18zhang.spider.util.NamedThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监视器线程，用来监视线程池状态
 */
public class MonitorThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(MonitorThread.class);
	//线程池
	private NamedThreadPoolExecutor pool ;

	private String monitorName ;

	//停止监视
	public static volatile boolean stop = false;

	public MonitorThread(NamedThreadPoolExecutor pool ,String monitorName){
		this.pool = pool ;
		this.monitorName = monitorName ;
	}

	public void run() {
		while(!stop){
			logger.debug(monitorName + String.format(
					"[monitor] [%d/%d] Active: %d, Completed: %d, queueSize: %d, Task: %d, isShutdown: %s, isTerminated: %s",
					this.pool.getPoolSize(), this.pool.getCorePoolSize(), this.pool.getActiveCount(), this.pool.getCompletedTaskCount(),
					this.pool.getQueue().size(), this.pool.getTaskCount(), this.pool.isShutdown(), this.pool.isTerminated()));
			try {
				Thread.sleep(Constants.SPIDER_MONITOR_THREAD_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("InterruptedException", e);
			}
		}
	}

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
}
