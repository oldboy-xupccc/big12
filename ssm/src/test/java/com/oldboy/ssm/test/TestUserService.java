package com.oldboy.ssm.test;

import com.oldboy.ssm.domain.User;
import com.oldboy.ssm.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/9/18.
 */
public class TestUserService {
	@Test
	public void testInsert(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml") ;
		UserService us = (UserService) ac.getBean("userService");
		User u = new User() ;
		u.setName("kkkkkkkk");
		u.setAge(122);
		us.insertUser(u);
	}
}
