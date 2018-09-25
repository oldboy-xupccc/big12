package com.oldboy.spring;

import com.oldboy.spring.service.ByeService;
import com.oldboy.spring.service.WelcomService;
import com.oldboy.spring.service.WelcomeServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/9/17.
 */
public class AppSpring {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml") ;
		WelcomService ws = (WelcomService) ac.getBean("welcomeService");
		ws.sayHello();

		ws = (WelcomService) ac.getBean("welcomeService");
		ws.sayHello();

		ByeService bs = (ByeService) ac.getBean("byeService");
		bs.sayBye();
	}
}
