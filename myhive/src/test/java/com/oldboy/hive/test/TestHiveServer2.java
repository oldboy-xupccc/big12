package com.oldboy.hive.test;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Administrator on 2018/9/26.
 */
public class TestHiveServer2 {
	@Test
	public void testSelect() throws Exception {
		String driver = "org.apache.hive.jdbc.HiveDriver" ;
		Class.forName(driver) ;
		Connection conn = DriverManager.getConnection("jdbc:hive2://s101:10000/big12");
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from par1 sort by age desc") ;
		while(rs.next()){
			int id = rs.getInt("id") ;
			String name = rs.getString("name") ;
			String prov= rs.getString("prov") ;
			String city = rs.getString("city") ;
			System.out.printf("%d,%s,%s,%s\r\n" , id ,name , prov , city);
		}
		rs.close();
		conn.close();
	}
}
