package com.oldboy.java.gof.respchain;

/**
 * 汽车类
 */
public class Car {
	public void handledBy(Worker worker){
		worker.work();
	}
}
