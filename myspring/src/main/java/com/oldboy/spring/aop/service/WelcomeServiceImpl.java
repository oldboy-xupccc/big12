package com.oldboy.spring.aop.service;

/**
 * Created by Administrator on 2018/9/17.
 */
public class WelcomeServiceImpl implements WelcomeService {
	public void sayHello(String name) {
		String s = null ;
		s.toLowerCase();
		System.out.println( "hello : " + name);
	}
}
