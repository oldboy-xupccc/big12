package com.oldboy.mybatis.test;

import com.oldboy.mybatis.domain.many2many.Student;
import com.oldboy.mybatis.domain.many2many.Teacher;
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
 * 测试多对多
 */
public class TestMany2Many {
	@Test
	public void testInsert() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();

		//创建对象
		Teacher t1 = new Teacher();
		Teacher t2 = new Teacher();

		Student s1 = new Student();
		Student s2 = new Student();
		Student s3 = new Student();
		Student s4 = new Student();

		//设置关联
		t1.addStudents(s1 , s2 , s3);
		t2.addStudents(s2 , s3 , s4);

		sess.insert("teas.insert" , t1);
		sess.insert("teas.insert" , t2);

		sess.insert("stus.insert" , s1);
		sess.insert("stus.insert" , s2);
		sess.insert("stus.insert" , s3);
		sess.insert("stus.insert" , s4);

		//插入关系
		sess.insert("teas.insertLink" , t1);
		sess.insert("teas.insertLink" , t2);

		sess.commit();
		sess.close();
		System.out.println("ok");
	}

	@Test
	public void testSelectOne() throws IOException {
		InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(in);
		SqlSession sess = sf.openSession();
		Teacher t = sess.selectOne("teas.selectOne" , 7);
		sess.commit();
		System.out.println("ok");
	}
}
