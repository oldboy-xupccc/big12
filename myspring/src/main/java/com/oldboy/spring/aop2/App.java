package com.oldboy.spring.aop2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/9/17.
 */
public class App {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("aop2.xml");
		Actor actor = (Actor) ac.getBean("singer");
		actor.show();


	}
}

