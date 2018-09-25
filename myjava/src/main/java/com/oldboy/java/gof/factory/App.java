package com.oldboy.java.gof.factory;

/**
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println(Factory1.newTVSet().getBrand()) ;

		Factory2 f2 = new Factory2();
		System.out.println(f2.newTVSet().getColor()) ;
	}
}
