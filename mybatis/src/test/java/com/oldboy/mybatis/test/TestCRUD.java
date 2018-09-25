package com.oldboy.mybatis.test;

import com.oldboy.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 测试增伤改查
 */
public class TestCRUD {
	@Test
	public void testInsert() throws IOException {
		//加载配置文件
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		//创建会话工厂(builder模式)
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		//开启会话，相当于连接
		SqlSession sess = sf.openSession();
		User u = new User() ;
		u.setName("tom");
		u.setAge(12);
		sess.insert("users.insert" , u) ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}

	@Test
	public void testUpdate() throws IOException {
		//加载配置文件
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		//创建会话工厂(builder模式)
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		//开启会话，相当于连接
		SqlSession sess = sf.openSession();
		User u = new User() ;
		u.setName("tomas");
		u.setAge(22);
		u.setId(1);
		sess.update("users.update" , u) ;
		sess.commit();
		sess.close();
	}
	@Test
	public void testSelectOne() throws IOException {
		//加载配置文件
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		//创建会话工厂(builder模式)
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		//开启会话，相当于连接
		SqlSession sess = sf.openSession();
		User u = sess.selectOne("users.selectById" , 1) ;
		sess.commit();
		System.out.println(u.getName());
		sess.close();
	}
	@Test
	public void testSelectAll() throws IOException {
		//加载配置文件
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		//创建会话工厂(builder模式)
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		//开启会话，相当于连接
		SqlSession sess = sf.openSession();
		List<User> list = sess.selectList("users.selectAll" ) ;
		sess.commit();
		System.out.println(list.size());
		sess.close();
	}
	@Test
	public void deleteById() throws IOException {
		//加载配置文件
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		//创建会话工厂(builder模式)
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		sess.delete("users.delete" , 1) ;
		sess.commit();
		sess.close();
	}
}
