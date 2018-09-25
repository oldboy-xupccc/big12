package com.oldboy.spring.aop3;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 *
 */
public class Audience3 {
	/**
	 * 观看 , 环绕通知
	 */
	public Object watch(ProceedingJoinPoint pjp){
		try {
			System.out.println("sitdown");
			System.out.println("turnoffCellphone");
			//调用目标对象的方法
			Object obj = pjp.proceed();
			System.out.println("applaud");
			return obj ;

		} catch (Throwable e) {
			System.out.println("payOff");
			e.printStackTrace();
		}
		finally {
			System.out.println("gohome");
		}
		return null ;
	}
}
