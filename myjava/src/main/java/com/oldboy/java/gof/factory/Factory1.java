package com.oldboy.java.gof.factory;

/**
 */
public class Factory1 {

	/**
	 * 静态工厂
	 */
	public static TVSet newTVSet(){
		TVSet tv = new TVSet();
		tv.setBrand("songxia");
		tv.setColor("red");
		tv.setSize(120);
		return tv ;
	}
}
