package com.oldboy.spark.sql;

import org.apache.spark.SparkConf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * 使用spark分布式查询引擎
 */
public class ThriftServerDemo {
	public static void main(String[] args) {
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver") ;
			Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.231.101:10000/big12") ;
			ResultSet rs = conn.createStatement().executeQuery("select * from orders") ;
			while(rs.next()){
				System.out.printf("%d/%s\r\n" , rs.getInt(1) , rs.getString(2));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
