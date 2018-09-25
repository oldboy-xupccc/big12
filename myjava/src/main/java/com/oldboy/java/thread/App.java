package com.oldboy.java.thread;

/**
 * Created by Administrator on 2018/9/6.
 */
public class App {
	public static void main(String[] args) {
		Pool pool = new Pool();
		Producer p = new Producer("p1" , pool) ;
		Consumer c = new Consumer("c1" , pool) ;
		p.start();
		c.start();
	}
}
