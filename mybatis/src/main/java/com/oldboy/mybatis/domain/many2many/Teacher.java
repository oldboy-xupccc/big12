package com.oldboy.mybatis.domain.many2many;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/17.
 */
public class Teacher {
	private Integer id ;
	private String tname ;
	private List<Student> stus = new ArrayList<Student>() ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public List<Student> getStus() {
		return stus;
	}

	public void setStus(List<Student> stus) {
		this.stus = stus;
	}

	/**
	 * 方便关联关系
	 */
	public void addStudents(Student... stus){
		for(Student s : stus){
			this.getStus().add(s) ;
			s.getTeas().add(this) ;
		}
	}
}
