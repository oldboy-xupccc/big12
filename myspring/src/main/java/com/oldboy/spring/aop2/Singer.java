package com.oldboy.spring.aop2;

/**
 * 目标类
 */
public class Singer implements Actor{
	public void show() {
		System.out.println("~~~~啦啦啦");
		String s = null ;
		s.toLowerCase();
	}
}
