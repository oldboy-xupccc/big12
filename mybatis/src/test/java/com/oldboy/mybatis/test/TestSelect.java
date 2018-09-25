package com.oldboy.mybatis.test;

import com.oldboy.mybatis.domain.one2one.pk.Husband;
import com.oldboy.mybatis.domain.one2one.pk.Wife;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试一对一主键关联
 */
public class TestSelect {
	@Test
	public void testOrderCount() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		int count = sess.selectOne("selects.selectOrderCount") ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}
	@Test
	public void testOrderStat() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		Map map = sess.selectOne("selects.selectOrderStat") ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}

	@Test
	public void testOrderPage() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		Map<String, Integer> params = new HashMap<String, Integer>() ;
		params.put("offset" , 1) ;
		params.put("length" , 3) ;

		RowBounds rb = new RowBounds(1 , 3) ;
		List list = sess.selectList("selects.selectOrderPage2" , rb) ;

		sess.commit();
		sess.close();
		System.out.println("ok");
	}
}
