package com.oldboy.java.gof.pooling;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
public class App {
	public static void main(String[] args) {
		final DataSource ds = new MyDataSource();
		new Thread(){
			public void run() {
				try {
					Connection conn = ds.getConnection();
					conn.close();
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread(){
			public void run() {
				try {
					Connection conn = ds.getConnection();
					conn.close();
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread(){
			public void run() {
				try {
					Connection conn = ds.getConnection();
					conn.close();
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
		new Thread(){
			public void run() {
				try {
					Connection conn = ds.getConnection();
					conn.close();
					System.out.println();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
