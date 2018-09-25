package com.oldboy.java.thread;

/**
 * Created by Administrator on 2018/9/7.
 */
public class BearDemo {
	public static void main(String[] args) {
		Box box = new Box() ;
		new Bear(box, "xxxxxx1").start();
		new Bear(box, "xxxxxx2").start();
		for(int i = 0 ; i < 30 ; i ++){
			new Bee(box , "B" + i).start();
		}
	}

	/**
	 * 罐子类，容器
	 */
	static class Box{
		//最大量
		public static int MAX = 50 ;

		//当前蜂蜜量
		private int honeyNum = 0 ;

		//向罐子追加蜂蜜
		public synchronized void add(int n){
			while(honeyNum == MAX){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			honeyNum ++ ;
			this.notify();
		}

		/**
		 * 消费行为
		 */
		public synchronized int clearAll(){
			while(honeyNum < Bear.MIN){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int n = honeyNum ;
			honeyNum = 0 ;
			notify();
			return n ;
		}
	}

	/**
	 * 熊
	 */
	static class Bear extends Thread {
		public static int MIN = 20 ;
		private String bearName ;
		private Box box ;
		public Bear(Box box, String bearName){
			this.box = box ;
			this.bearName = bearName ;
		}

		public void run() {
			for(;;){
				int n = box.clearAll();
				System.out.println(bearName + " : " + n);
			}
		}
	}

	/**
	 * 蜜蜂线程类
	 */
	static class Bee extends Thread {
		private String bname;
		private Box box ;
		public Bee(Box box, String bname) {
			this.box =box;
			this.bname = bname;
		}

		public void run() {
			int index = 1 ;
			for(;;){
				box.add(index);
				System.out.println(bname + " : " + index);
				index ++ ;
			}
		}
	}
}
