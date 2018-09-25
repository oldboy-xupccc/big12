package com.oldboy.spring;

import com.oldboy.spring.service.ByeService;
import com.oldboy.spring.service.WelcomService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/9/17.
 */
public class AppSpring2 {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans2.xml") ;
		WelcomService ws = (WelcomService) ac.getBean("welcomeService");
		ws = (WelcomService) ac.getBean("welcomeService");
		ws.sayHello();

	}
}