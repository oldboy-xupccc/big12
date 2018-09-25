package com.oldboy.mybatis.domain.many2many;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Student {
	private Integer id ;
	private String sname ;
	private List<Teacher> teas = new ArrayList<Teacher>() ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public List<Teacher> getTeas() {
		return teas;
	}

	public void setTeas(List<Teacher> teas) {
		this.teas = teas;
	}
}
