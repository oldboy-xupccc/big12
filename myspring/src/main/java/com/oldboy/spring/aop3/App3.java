package com.oldboy.spring.aop3;

import com.oldboy.spring.aop3.Actor3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/9/17.
 */
public class App3 {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("aop3.xml");
		Actor3 actor = (Actor3) ac.getBean("singer");
		actor.show();


	}
}

