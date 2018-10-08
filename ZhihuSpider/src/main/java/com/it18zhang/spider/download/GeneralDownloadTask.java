package com.it18zhang.spider.download;

import com.it18zhang.spider.domain.PageInfo;
import com.it18zhang.spider.util.Constants;

/**
 * 常规下载任务，不需要通过线程池
 */
public class GeneralDownloadTask extends BaseDownloadTask {

	private PageInfo pageInfo ;

	public GeneralDownloadTask(String url, boolean useProxy) {
		super(url, useProxy);
	}

	protected void handle(PageInfo pageInfo) {
		this.pageInfo = pageInfo;

	}

	protected void retry() {
		try {
			Thread.sleep(Constants.SPIDER_DOWNLOAD_RETRY_INTERVAL_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.run();//继续下载
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}
}
