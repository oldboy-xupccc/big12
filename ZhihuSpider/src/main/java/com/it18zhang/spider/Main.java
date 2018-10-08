package com.it18zhang.spider;

import com.it18zhang.spider.download.ProxyHostDownloader;
import com.it18zhang.spider.download.ZhiHuDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) {
		//ProxyHostDownloader.getInstance().startCrawl();
		ZhiHuDownloader.getInstance().startCrawl();
	}
}
