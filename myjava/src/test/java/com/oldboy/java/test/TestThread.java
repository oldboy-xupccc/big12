package com.oldboy.java.test;

/**
 */
public class TestThread {

	public static void main(String[] args) {
		Saler s1 = new Saler("s1");
		Saler s2 = new Saler("s2");
		s1.start();
		s2.start();
	}

	public void test1() {

	}

	/**
	 * 售票员
	 */
	static class Saler extends Thread {
		public static Object lock = new Object();
		public static int tickets = 100;
		public String sname;

		public Saler(String sname) {
			this.sname = sname;
		}

		//
		public void run() {
			synchronized (lock) {
				while (true) {
					int tmp = tickets;
					if (tmp > 1) {
						System.out.println(sname + " : " + tmp);
						tickets--;
						Thread.yield();
					} else {
						break;
					}
				}
			}
		}
	}
}
