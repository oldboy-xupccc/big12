package com.oldboy.java.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class LockDemo2 {
	public static void main(String[] args) {
		//创建读写锁
		final ReentrantLock lock = new ReentrantLock() ;
		new Thread(){
			public void run() {
				lock.lock();
				int c = lock.getHoldCount();
				lock.lock();
				c = lock.getHoldCount();

				//
				boolean b = lock.isHeldByCurrentThread();

				System.out.println("1");
				lock.unlock();
			}
		}.start();
		new Thread() {
			public void run() {
				boolean b = lock.isHeldByCurrentThread();
				boolean r = lock.tryLock();
				System.out.println("1");
				lock.unlock();
			}
		}.start();
	}
}
