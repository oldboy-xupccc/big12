package com.oldboy.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Administrator on 2018/9/13.
 */
public class JDBCDemo {
	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver") ;
		String  url = "jdbc:mysql://localhost:3306/big12" ;
		String user = "root" ;
		String pass = "root" ;
		Connection conn = DriverManager.getConnection(url , user ,pass) ;
		//关闭自动提交
		conn.setAutoCommit(false);
		//设置隔离级别
		conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		//
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select age from t1 where id = 1");
		if(rs.next()){
			int age = rs.getInt(1);
			System.out.println(age);
		}

		rs.close();
		conn.commit();
		conn.close();
	}
}
