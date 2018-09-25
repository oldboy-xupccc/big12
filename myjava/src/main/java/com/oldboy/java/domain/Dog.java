package com.oldboy.java.domain;

/**
 * Created by Administrator on 2018/9/5.
 */
public class Dog  extends Animal{
	public Dog(String name){
		super(name);
		System.out.println("new Dog() : " + name);
	}
	public Dog(){
		this("tom");
	}
	public void run() {
		System.out.println("~~~~");
	}
}
