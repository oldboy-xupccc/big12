package com.oldboy.java.jvm;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/9/14.
 */
public class App {
	public static void main(String[] args) throws Exception {
		ClassLoader loader = new MyClassLoader();
		Class clz = loader.loadClass("Hello") ;
		System.out.println(clz);
		Method m = clz.getDeclaredMethod("main") ;
		m.setAccessible(true);
		m.invoke(null ) ;
		System.out.println();


	}
}
