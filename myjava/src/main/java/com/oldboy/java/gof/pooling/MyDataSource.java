package com.oldboy.java.gof.pooling;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源
 */
public class MyDataSource extends DataSourceAdaptor{

	public Connection getConnection() throws SQLException {
		try {
			return ConnectionPool.getInstance().getConnection() ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null ;
	}
}
