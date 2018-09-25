package com.oldboy.mybatis.domain;

/**
 * Created by Administrator on 2018/9/16.
 */
public class Item {
	private Integer id;
	private String iname ;
	private Order order ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIname() {
		return iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
