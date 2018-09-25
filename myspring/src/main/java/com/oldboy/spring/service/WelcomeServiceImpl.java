package com.oldboy.spring.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 实现类
 */
@Service("welcomeService")
@Scope(value = "singleton")
public class WelcomeServiceImpl implements WelcomService {
	private String name ;

	@Resource(name="byeService")
	private ByeService bs ;
	
	public WelcomeServiceImpl(){
		System.out.println("1111");
	}
	public WelcomeServiceImpl(String str){
		System.out.println("2222");
	}
	public WelcomeServiceImpl(String str ,Integer str2){
		System.out.println("3333");
	}
	public WelcomeServiceImpl(Integer i , String str){
		System.out.println("4444");
	}
	public WelcomeServiceImpl(Integer i , int str){
		System.out.println("5555");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ByeService getBs() {
		return bs;
	}

	public void setBs(ByeService bs) {
		this.bs = bs;
	}

	public void sayHello() {
		bs.sayBye();
	}
}
