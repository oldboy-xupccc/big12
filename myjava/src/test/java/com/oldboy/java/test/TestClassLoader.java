package com.oldboy.java.test;

import org.junit.Test;

/**
 */
public class TestClassLoader {
	@Test
	public void test1() throws ClassNotFoundException {
		ClassLoader loader = TestClassLoader.class.getClassLoader() ;
		ClassLoader ploader = loader.getParent();
		ClassLoader pploader = ploader.getParent();
		//得到系统类加载器
		ClassLoader sysLoader = ClassLoader.getSystemClassLoader() ;
		System.out.println();

		Class clazz = Class.forName("javax.jnlp.BasicService") ;

	}
}
