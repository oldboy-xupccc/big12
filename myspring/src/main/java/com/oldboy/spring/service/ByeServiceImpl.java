package com.oldboy.spring.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 */
@Service("byeService")
@Scope("singleton")
public class ByeServiceImpl implements ByeService {

	public ByeServiceImpl(){
		System.out.println("new ByeServiceImpl()");
	}

	private String bye ;

	public String getBye() {
		return bye;
	}

	public void setBye(String bye) {
		this.bye = bye;
	}

	public void sayBye() {
		System.out.println(bye);
	}
}
