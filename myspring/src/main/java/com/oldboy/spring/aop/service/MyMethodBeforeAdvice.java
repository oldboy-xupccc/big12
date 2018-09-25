package com.oldboy.spring.aop.service;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 前置通知:方法前通知
 */
public class MyMethodBeforeAdvice  implements MethodBeforeAdvice{
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("hello world!!!");
	}
}
