package com.oldboy.java.gof.respchain;

/**
 * 工人抽象类
 */
public abstract class Worker {
	//下一个工人
	private Worker nextWorker ;

	public Worker(){
	}
	public Worker(Worker nextWorker){
		this.nextWorker = nextWorker ;
	}

	public void setNextWorker(Worker nextWorker) {
		this.nextWorker = nextWorker;
	}

	public void work(){
		doWork();
		if(nextWorker != null){
			nextWorker.work();
		}
	}

	public abstract void doWork()  ;
}
