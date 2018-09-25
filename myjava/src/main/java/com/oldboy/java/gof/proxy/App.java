package com.oldboy.java.gof.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * App
 * 
 */
public class App {
	public static void main(String[] args) {

		//目标对象
		final WelcomeService target = new WelcomeServiceImpl() ;


		//类加载器
		ClassLoader loader = ClassLoader.getSystemClassLoader();

		//接口集合
		Class[] interfaces = {WelcomeService.class , WelcomeService2.class};

		//处理器
		InvocationHandler h = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				long start = System.nanoTime() ;
				Object ret = method.invoke(target , args) ;
				long dur = System.nanoTime() - start ;
				System.out.println(method.getName() + "耗时 : " + dur);
				return ret;
			}
		} ;


		//创建代理对象
		WelcomeService proxy = (WelcomeService) Proxy.newProxyInstance(loader , interfaces , h);
		//访问代理对象的方法
		((WelcomeServiceImpl)proxy).sayHello2("kkkk");
		proxy.sayHello("tomas");
		proxy.sayHello("tomas");
		proxy.sayHello("tomas");
		proxy.sayHello("tomas");
	}
}
