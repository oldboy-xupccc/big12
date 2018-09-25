package com.oldboy.mybatis.test;

import com.oldboy.mybatis.domain.User;
import com.oldboy.mybatis.domain.one2one.pk.Husband;
import com.oldboy.mybatis.domain.one2one.pk.Wife;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 测试一对一主键关联
 */
public class TestOne2OnePK {
	@Test
	public void testInsert() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		Husband h = new Husband();
		h.setHname("tom2");

		Wife w = new Wife();
		w.setHusband(h);
		w.setWname("jerry2");

		sess.insert("husbands.insert" , h) ;
		sess.insert("wifes.insert" , w) ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}
	@Test
	public void testSelectHusband() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		Husband h = sess.selectOne("husbands.selectOne" , 3) ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}
}
