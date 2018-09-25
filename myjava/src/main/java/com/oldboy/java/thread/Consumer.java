package com.oldboy.java.thread;

/**
 * 生产者
 */
public class Consumer extends Thread{
	public String cname;
	public Pool pool ;
	public Consumer(String cname, Pool pool){
		this.cname = cname;
		this.pool = pool ;
	}

	public void run() {
		while(true){
			Integer i = null;
			try {
				i = pool.remove();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(cname + "- : " + i);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}