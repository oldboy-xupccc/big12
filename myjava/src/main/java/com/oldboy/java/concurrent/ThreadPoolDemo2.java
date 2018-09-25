package com.oldboy.java.concurrent;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 线程池
 */
public class ThreadPoolDemo2 {
	public static void main(String[] args) throws Exception {

		//固定数量线程池
		ThreadPoolExecutor e = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		//执行任务
		Future f = e.submit(new MyRunnable("1"));
		f.get();
		e.submit(new MyRunnable("2")).get();
		e.submit(new MyRunnable("3")).get();
		e.submit(new MyRunnable("4")).get();
	}

	static class MyRunnable implements Runnable{
		private String s ;
		public MyRunnable(String s){
			this.s = s ;
		}
		public void run() {
			Random r = new Random();
			try {
				Thread.sleep(r.nextInt((r.nextInt(5) + 1) * 1000) );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(s);
		}
	}
}
