package com.it18zhang.spider.proxy;


import com.it18zhang.spider.util.Constants;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 代理服务器
 */
public class ProxyHost implements Delayed, Serializable {
	private static final long serialVersionUID = -7583883432417635332L;
	private long timeInterval;			// 任务间隔时间,单位ms
	private String ip;					// ip地址
	private int port;					// 端口
	private boolean available;			// 是否可用
	private boolean anonymous;			// 是否匿名
	private long lastSuccessTime;		// 最近一次请求成功时间
	private long successTotalTime;		// 请求成功总耗时
	private int failureCount;			// 请求失败次数
	private int successCount;			// 请求成功次数
	private double successAvgTime;		// 成功请求平均耗时

	public ProxyHost(String ip, int port, long timeInterval) {
		this.ip = ip;
		this.port = port;
		this.timeInterval = timeInterval;
		this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public long getTimeInterval() {
		return timeInterval;
	}

	public long getLastSuccessTime() {
		return lastSuccessTime;
	}

	public void setLastSuccessTime(long lastSuccessTime) {
		this.lastSuccessTime = lastSuccessTime;
	}

	public long getSuccessTotalTime() {
		return successTotalTime;
	}

	public void setSuccessTotalTime(long successTotalTime) {
		this.successTotalTime = successTotalTime;
	}

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
	}

	public long getDelay(TimeUnit unit) {
		return unit.convert(timeInterval - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public int compareTo(Delayed o) {
		ProxyHost element = (ProxyHost) o;
		if (successAvgTime == 0.0d || element.successAvgTime == 0.0d) {
			return 0;
		}
		return successAvgTime > element.successAvgTime ? 1 : (successAvgTime < element.successAvgTime ? -1 : 0);
	}

	public int getFailureCount() {
		return failureCount;
	}

	public void setFailureCount(int failureCount) {
		this.failureCount = failureCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public double getSuccessAvgTime() {
		return successAvgTime;
	}

	public void setSuccessAvgTime(double successAvgTime) {
		this.successAvgTime = successAvgTime;
	}

	public String toString() {
		return "Proxy{" + "timeInterval=" + timeInterval + ", ip='" + ip + '\'' + ", port=" + port + ", available=" + available + ", anonymous=" + anonymous + ", lastSuccessTime=" + lastSuccessTime + ", successTotalTime=" + successTotalTime + ", failureCount=" + failureCount + ", successCount=" + successCount + ", successAvgTime=" + successAvgTime + '}';
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProxyHost proxyHost = (ProxyHost) o;

		if (port != proxyHost.port)
			return false;
		return ip.equals(proxyHost.ip);

	}

	@Override
	public int hashCode() {
		int result = ip.hashCode();
		result = 31 * result + port;
		return result;
	}

	public String getProxyStr() {
		return ip + ":" + port;
	}

	/**
	 * 是否丢弃该代理服务器
	 * 失败次数大于3，且失败率超过60%，丢弃
	 */
	public boolean isDiscard(){
		// 失败率，失败次数/总次数
		float failRatio = (float)failureCount / (failureCount + successCount) ;
		if(failureCount >= Constants.SPIDER_PROXYSERVER_DISCARD_FAILURECOUNT
				&&  failRatio > Constants.SPIDER_PROXYSERVER_DISCARD_FAILURERATIO){
			return true ;
		}
		return false ;
	}
}