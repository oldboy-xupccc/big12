package com.oldboy.java.thread;

import java.util.LinkedList;
import java.util.List;

/**
 * 容器
 */
public class Pool {
	private List<Integer> list  = new LinkedList<Integer>() ;
	public static int MAX = 100 ;

	public synchronized void add(Integer i) throws Exception {
		while(list.size() == MAX){
			this.wait();
		}
		list.add(i) ;
		this.notify();
	}

	public synchronized Integer remove() throws InterruptedException {
		while(list.isEmpty()){
			this.wait();
		}
		int i  = list.remove(0) ;
		this.notify();
		return i ;
	}
}

