package com.oldboy.java.domain;

/**
 * Created by Administrator on 2018/9/5.
 */
public abstract class Animal {
	public Animal(String name ){
		System.out.println("new Animial() : " + name);
	}
	public  abstract  void run() ;
}
