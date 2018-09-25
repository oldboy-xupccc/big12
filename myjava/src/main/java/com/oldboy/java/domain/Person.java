package com.oldboy.java.domain;

import java.io.Serializable;

/**
 * Person
 */
public class Person implements Serializable{

	public static final long serialVersionUID = 4358131788078372361L;

	//类加载期间的初始化过程
	static{
		System.out.println("kkk");
	}

	public Person(String name){
		this.name = name ;
		System.out.println(2222);
	}

	private Person(Integer n){
		System.out.println(3333);
	}
	private Person(int n){
		System.out.println(444);
	}
	private Person(int[] n){
		System.out.println(555);
	}
	private Person(Integer[][] n){
		System.out.println(666);
	}
	public Person(){
		System.out.println("1111");
	}


	private Integer id ;
	private String name ;
	private int age ;
	private boolean married ;

	private Person friend ;

	private transient Person diren ;

	public Person getDiren() {
		return diren;
	}

	public void setDiren(Person diren) {
		this.diren = diren;
	}

	public Person getFriend() {
		return friend;
	}

	public void setFriend(Person friend) {
		this.friend = friend;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

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
}