package com.oldboy.spring;

import com.oldboy.spring.service.WelcomService;
import com.oldboy.spring.service.WelcomeServiceImpl;

/**
 * Created by Administrator on 2018/9/17.
 */
public class App {
	public static void main(String[] args) {
		WelcomeServiceImpl ws = new WelcomeServiceImpl();
		ws.setName("tom");
		ws.sayHello();
	}
}
