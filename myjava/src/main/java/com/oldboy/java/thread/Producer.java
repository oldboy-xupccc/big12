package com.oldboy.java.thread;

/**
 * 生产者
 */
public class Producer  extends Thread{
	public String pname ;
	public Pool pool ;
	public Producer(String pname , Pool pool){
		this.pname = pname ;
		this.pool = pool ;
	}

	public void run() {
		int index = 1 ;
		while(true){
			try {
				pool.add(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pname + " + : " + index);
			index ++ ;
		}
	}
}
