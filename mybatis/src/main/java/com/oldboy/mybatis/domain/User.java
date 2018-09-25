package com.oldboy.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 域模型
 */
public class User {
	private Integer id;
	private String name ;
	private int age ;
	//订单集合
	private List<Order> orders = new ArrayList<Order>() ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
}
