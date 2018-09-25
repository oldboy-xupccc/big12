package com.oldboy.java.gof.factory;

/**
 * 非静态工厂
 */
public class Factory2 {

	/**
	 * 非静态工厂
	 */
	public TVSet newTVSet(){
		TVSet tv = new TVSet();
		tv.setBrand("songxia");
		tv.setColor("red");
		tv.setSize(120);
		return tv ;
	}
}
