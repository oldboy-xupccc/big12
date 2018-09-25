package com.oldboy.java.gof;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 */
public class ProxyDemo {

	//接口
	static interface WelcomeService{
		public void sayHello() ;
	}

	//实现类
	static class WelcomeServiceImpl implements WelcomeService{
		public void sayHello() {
			System.out.println("hello world");
		}
	}

	public static void main(String[] args) {
		final WelcomeService ws = new WelcomeServiceImpl() ;
		InvocationHandler h = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				long start = System.nanoTime() ;
				Object obj = method.invoke(ws , args) ;
				System.out.println("执行耗时 ： " + (System.nanoTime() - start));
				return obj;
			}
		} ;

		ClassLoader loader = ClassLoader.getSystemClassLoader() ;

		WelcomeService proxy = (WelcomeService) Proxy.newProxyInstance(loader ,new Class[]{WelcomeService.class} , h);
		proxy.sayHello();


	}
}
