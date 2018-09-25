package com.oldboy.java.jvm;

/**
 *
 */
public class App2 {
	public static void main(String[] args) throws Exception {
		ClassLoader loader = new MyClassLoader();
		Class clz = loader.loadClass("ByeServiceImpl0") ;
		IByeService bs = (IByeService) clz.newInstance();
		bs.sayBye("tomtomt");
	}
}
