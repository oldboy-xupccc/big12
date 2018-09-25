package com.oldboy.java.jvm;

/**
 *
 */
public class App3 {
	public static void main(String[] args) throws Exception {
		//类加载
		Class claz = Class.forName("JiafeiCat" , false , ClassLoader.getSystemClassLoader()) ;
		claz.getName();
		claz.newInstance();
		claz.newInstance();
		Thread.sleep(1000000);
	}
}
