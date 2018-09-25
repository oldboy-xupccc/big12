package com.oldboy.java.gof.respchain;

/**
 * Created by Administrator on 2018/9/8.
 */
public class App {
	public static void main(String[] args) {
		TireWorker w1 = new TireWorker();
		EngineWorker w2 = new EngineWorker();
		PaintWorker w3 = new PaintWorker();
		w1.setNextWorker(w2);
		w2.setNextWorker(w3);

		Car car = new Car();
		car.handledBy(w1);
	}
}
