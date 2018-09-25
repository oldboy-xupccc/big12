package com.oldboy.mybatis.domain.one2one.fk;

/**
 * Created by Administrator on 2018/9/17.
 */
public class HusbandFK {
	private  Integer id ;
	private String hname ;
	private WifeFK wife ;

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

	public WifeFK getWife() {
		return wife;
	}

	public void setWife(WifeFK wife) {
		this.wife = wife;
	}
}
