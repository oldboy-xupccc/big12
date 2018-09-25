package com.oldboy.mybatis.domain.one2one.pk;

/**
 * Created by Administrator on 2018/9/16.
 */
public class Husband {
	private Integer id ;
	private String hname ;
	private Wife wife ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public Wife getWife() {
		return wife;
	}

	public void setWife(Wife wife) {
		this.wife = wife;
	}
}
