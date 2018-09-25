package com.oldboy.java.gof.observer;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class MyObserver {
	public static void main(String[] args) {
		Thief a = new Thief();
		a.addObserver(new Police1());
		a.addObserver(new Police2());

		a.stealOneThing("iphone 6 plus");
	}

	/**
	 * 可观察对象
	 */
	static class Thief extends Observable{
		//偷东西
		public void stealOneThing(String thing){
			System.out.println("偷了一个东西 : " + thing);
			setChanged();
			notifyObservers();
		}
	}

	static class Police1 implements Observer{
		public void update(Observable o, Object arg) {
			System.out.println("行动1");
		}
	}

	static class Police2 implements Observer {
		public void update(Observable o, Object arg) {
			System.out.println("行动2");
		}
	}
}
