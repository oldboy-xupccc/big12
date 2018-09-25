package com.oldboy.java.gof.adaptor;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Administrator on 2018/9/7.
 */
public class App {
	public static void main(String[] args) {
		JFrame f = new JFrame() ;
		f.setBounds(0 ,0 , 1366 , 768);
		f.setTitle("标题栏");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(-1);
			}
		});

		f.setVisible(true);
	}
}
