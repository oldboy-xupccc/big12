package com.oldboy.sm.test;

import com.oldboy.sm.domain.User;
import com.oldboy.sm.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Administrator on 2018/9/17.
 */
public class TestDataSource {
	@Test
	public void test1() throws SQLException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml") ;
		DataSource ds = (DataSource) ac.getBean("dataSource");
		Connection conn = ds.getConnection();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select version()") ;
		if(rs.next()){
			String version  = rs.getString(1);
			System.out.println(version);
		}
		rs.close();
		conn.close();
	}

	@Test
	public void testInsert() throws SQLException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml") ;
		UserService us = (UserService) ac.getBean("userService");
		User u = new User() ;
		u.setName("tommmmm");
		u.setAge(12);
		us.insertUser(u);
	}
	@Test
	public void testDelete() throws SQLException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml") ;
		UserService us = (UserService) ac.getBean("userService");
		us.delete(5) ;
	}
}
