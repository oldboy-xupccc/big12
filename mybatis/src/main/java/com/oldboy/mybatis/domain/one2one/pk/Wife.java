package com.oldboy.mybatis.domain.one2one.pk;

/**
 */
public class Wife {
	private Husband husband ;
	private String wname ;

	public Husband getHusband() {
		return husband;
	}

	public void setHusband(Husband husband) {
		this.husband = husband;
	}

	public String getWname() {
		return wname;
	}

	public void setWname(String wname) {
		this.wname = wname;
	}
}
