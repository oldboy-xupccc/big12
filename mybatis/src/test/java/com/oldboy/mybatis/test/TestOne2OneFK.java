package com.oldboy.mybatis.test;

import com.oldboy.mybatis.domain.one2one.fk.HusbandFK;
import com.oldboy.mybatis.domain.one2one.fk.WifeFK;
import com.oldboy.mybatis.domain.one2one.pk.Husband;
import com.oldboy.mybatis.domain.one2one.pk.Wife;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * 测试一对一主键关联
 */
public class TestOne2OneFK {
	@Test
	public void testInsert() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		HusbandFK h = new HusbandFK();
		h.setHname("hhh");
		h.setId(4);

		WifeFK w = new WifeFK();
		w.setWname("wwwww");

		h.setWife(w);
		w.setHusband(h);

//		sess.insert("husbands_fk.insert" , h) ;
		sess.insert("wifes_fk.insert" , w) ;
		sess.commit();
		sess.close();
		System.out.println("ok");
	}
}
