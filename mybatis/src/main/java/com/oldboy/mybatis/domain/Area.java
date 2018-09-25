package com.oldboy.mybatis.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 自关联
 */
public class Area {
	private Integer id ;
	private String areaName ;
	private Area parentArea ;
	private List<Area> children = new ArrayList<Area>();

	public  Area(){
	}

	public  Area(String areaname){
		this.areaName = areaname ;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Area getParentArea() {
		return parentArea;
	}

	public void setParentArea(Area parentArea) {
		this.parentArea = parentArea;
	}

	public List<Area> getChildren() {
		return children;
	}

	public void setChildren(List<Area> children) {
		this.children = children;
	}

	/**
	 * 一次性关联上下关系
	 */
	public void addChildren(Area... areas){
		for(Area a: areas){
			this.getChildren().add(a) ;
			a.setParentArea(this);
		}
	}
}
