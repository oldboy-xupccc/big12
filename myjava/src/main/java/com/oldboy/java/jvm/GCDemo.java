package com.oldboy.java.jvm;

/**
 * Created by Administrator on 2018/9/14.
 */
public class GCDemo {
	public static void main(String[] args) {
		int n = 1024 * 1024 * 6 ;
		for (int i  = 0 ; i<= 10000 ;  i++) {
			byte[] bytes = new byte[n] ;
		}
	}
}
