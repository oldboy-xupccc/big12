package com.oldboy.java.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class LockDemo {
	public static void main(String[] args) {
		//创建读写锁
		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock() ;
		new Thread(){
			public void run() {
				lock.writeLock().lock();
				System.out.println("1");
				lock.writeLock().unlock();
			}
		}.start();
		new Thread() {
			public void run() {
				lock.writeLock().tryLock();
				System.out.println("1");
				lock.writeLock().unlock();
			}
		}.start();
	}
}
