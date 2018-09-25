package com.oldboy.spring.aop.service;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * 异常通知
 */
public class MyThrowsAdvice implements ThrowsAdvice {
	public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
		System.out.println("出错了！！" + ex.getMessage());
	}
}
