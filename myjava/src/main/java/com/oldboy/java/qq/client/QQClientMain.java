package com.oldboy.java.qq.client;

/**
 * Created by Administrator on 2018/9/10.
 */
public class QQClientMain {
	public static void main(String[] args) {
		QQClientChatsUI ui = new QQClientChatsUI();
		QQClientCommThread t = new QQClientCommThread(ui);
		t.start();
		//互相设置依赖关系
		ui.commThread = t ;

	}
}
