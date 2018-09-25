package com.oldboy.java.gof.builder;

/**
 * Created by Administrator on 2018/9/7.
 */
public class App {
	public static void main(String[] args) {
		Computer c = new Computer.Builder()
							 .setCpu("intel")
							 .setMemory("sanxing")
							 .setHardDisk("xishu")
							 .build();

	}
}
