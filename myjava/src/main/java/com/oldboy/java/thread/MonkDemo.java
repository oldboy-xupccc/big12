package com.oldboy.java.thread;

/**
 * 和尚吃馒头问题
 */
public class MonkDemo {
	public static void main(String[] args) {
		Boss boss = new Boss();
		for(int i = 0 ; i < 10 ; i ++){
			new Monk(boss , "tom" + i).start(); ;
		}
	}

	static class Boss{
		//剩余的馒头数
		public static int breadNum = 30 ;

		//未吃馒头的和尚数
		public static int uneatedMonks = 10 ;

		//获取馒头
		public synchronized int getBread(Monk monk){
			//不足最小值
			if(monk.eated < Monk.MIN){
				//取出最上方的馒头
				int tmp = breadNum ;
				breadNum -- ;
				if(monk.eated == 0){
					uneatedMonks -- ;
				}
				return tmp ;

			}
			if(monk.eated == Monk.MAX){
				return 0 ;
			}
			//判断是否有多余的馒头
			if(breadNum > (uneatedMonks * Monk.MIN)){
				int tmp = breadNum ;
				breadNum -- ;
				return tmp ;
			}
			return 0 ;
		}
	}

	/**
	 * 和尚线程类
	 */
	static class Monk extends Thread{
		private String mname ;
		public static int MAX = 4 ;
		public static int MIN = 2 ;

		//已经吃了多少个
		private int eated  ;
		private String breadNumStr = "" ;

		private Boss boss ;

		public Monk(Boss boss , String mname){
			this.boss = boss ;
			this.mname = mname ;
		}

		public void run() {
			for(;;){
				int breadNo = boss.getBread(this) ;
				if(breadNo == 0){
					System.out.printf("%s吃了%d:(%s)\r\n" , mname , eated , breadNumStr);
					break ;
				}
				else{
					breadNumStr = breadNumStr + "," + breadNo ;
					eated ++ ;
				}
			}
		}
	}
}
