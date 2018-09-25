package com.oldboy.java.gof.singleton;

/**
 * 回收站
 */
public class Trash {

	private static Trash instance = new Trash();

	//懒汉式
	public static Trash getInstance(){
		if(instance != null){
			return instance ;
		}
		synchronized (Trash.class){
			if(instance == null){
				instance = new Trash() ;
			}
		}
		return instance ;
	}

	private Trash(){
	}

	public static void main(String[] args) {
		new Thread() {
			public void run() {
				Trash t = Trash.getInstance();
				System.out.println(t);
			}
		}.start();
		new Thread() {
			public void run() {
				Trash t = Trash.getInstance();
				System.out.println(t);
			}
		}.start();
	}
}
