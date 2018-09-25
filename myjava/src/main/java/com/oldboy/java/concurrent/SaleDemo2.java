package com.oldboy.java.concurrent;

import java.util.ArrayList;
import java.util.List;

/**
 * 轻量级锁实现售票问题
 */
public class SaleDemo2 {

	public static void main(String[] args) throws Exception {
		int n = 20 ;

		List<Sales2> allSales = new ArrayList<Sales2>() ;
		for(int i = 0 ; i < n ; i ++){
			allSales.add(new Sales2("s" + i)) ;
		}

		long start = System.currentTimeMillis() ;
		for(Sales2 s: allSales){
			s.start();
		}
		for(Sales2 s: allSales){
			s.join();
		}
		System.out.println(System.currentTimeMillis() - start);
	}


	//票池
	static class TicketPool2{
		//票数
		private int tickets = 200000 ;

		//
		private static TicketPool2 instance ;

		//
		private TicketPool2(){
		}

		public static TicketPool2 getInstance(){
			if(instance != null){
				return instance ;
			}
			synchronized (TicketPool2.class){
				if (instance == null) {
					instance = new TicketPool2();
				}
			}
			return instance ;
		}

		/**
		 * 取票
		 */
		public int getTicket(){
			synchronized (TicketPool2.class){
				int tmp = tickets;
				if (tmp == 0) {
					return 0;
				} else {
					tickets--;
					return tmp;
				}
			}
		}
	}

	/**
	 * 售票员
	 */
	static class Sales2 extends Thread{
		private String ssname ;
		private Sales2(String ssname){
			this.ssname = ssname ;
		}

		public void run() {
			TicketPool2 pool = TicketPool2.getInstance();
			for(;;){
				int n = pool.getTicket();
				if(n == 0){
					break;
				}
				else{
					System.out.printf("%s : %d\r\n" , ssname , n);
				}
			}
		}
	}

}
