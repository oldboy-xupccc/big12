package com.oldboy.java.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class ThreadPoolDemo {
	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				String tname = Thread.currentThread().getName();
				System.out.println(tname + " : hello world");
			}
		} ;
		//固定数量线程池
		ThreadPoolExecutor e = (ThreadPoolExecutor) new ThreadPoolExecutor(1,
																				  2,
																				  0L,
																				  TimeUnit.MILLISECONDS,
																				  new LinkedBlockingQueue<Runnable>()) ;

		//执行任务
		e.execute(r);
		e.execute(r);
		e.execute(r);
		e.execute(r);
		e.execute(r);
		e.shutdown();
	}
}
