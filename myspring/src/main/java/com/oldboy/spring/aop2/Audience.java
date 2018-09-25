package com.oldboy.spring.aop2;

import org.aspectj.lang.JoinPoint;

/**
 * 观众 , pojo
 */
public class Audience {
	/**
	 * 坐下
	 */
	public void sitdown(){
		System.out.println("sitdown");
	}

	/**
	 * 关机
	 */
	public void turnoffCellphone(JoinPoint jp){
		String method = jp.getSignature().getName();
		Object[] args = jp.getArgs();
		Object targ = jp.getTarget() ;
		Object proxy = jp.getThis() ;

		System.out.println("turnoffCellphone");
	}

	public void applaud(){
		System.out.println("applaud");
	}

	public void payOff(){
		System.out.println("退票");
	}
	
	public  void goHome(){
		System.out.println("goHome");
	}
}
