package com.oldboy.mybatis.domain.one2one.fk;

/**
 */
public class WifeFK {
	private Integer id ;
	private String wname ;
	private HusbandFK husband ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWname() {
		return wname;
	}

	public void setWname(String wname) {
		this.wname = wname;
	}

	public HusbandFK getHusband() {
		return husband;
	}

	public void setHusband(HusbandFK husband) {
		this.husband = husband;
	}
}
