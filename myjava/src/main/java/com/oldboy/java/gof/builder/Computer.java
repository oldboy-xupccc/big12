package com.oldboy.java.gof.builder;

/**
 * 电脑
 */
public class Computer {
	private String cpu;
	private String memory ;
	private String hardDisk ;

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getHardDisk() {
		return hardDisk;
	}

	public void setHardDisk(String hardDisk) {
		this.hardDisk = hardDisk;
	}

	/**
	 * 内部构建器
	 */
	public static class Builder{
		private Computer computer = new Computer() ;

		public Builder setCpu(String cpu){
			computer.setCpu(cpu);
			return this ;
		}
		public Builder setMemory(String mem){
			computer.setMemory(mem);
			return this ;
		}
		public Builder setHardDisk(String disk){
			computer.setHardDisk(disk);
			return this ;
		}

		public Computer build(){
			return computer ;
		}
	}
}
