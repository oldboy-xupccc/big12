package com.oldboy.springmvc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController {

	@RequestMapping("/index")
	public String index(){
		System.out.println("hello world");
		return "index" ;
	}
}
