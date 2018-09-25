package com.oldboy.java.gof.pooling;

import java.sql.*;

/**
 * 装饰连接
 */
public class MyConnection extends ConnectionAdaptor {

	private Connection rawConnect ;

	private ConnectionPool pool ;
	public MyConnection(Connection rawConnect , ConnectionPool pool){
		this.rawConnect = rawConnect ;
		this.pool = pool ;
	}

	public Statement createStatement() throws SQLException {
		return rawConnect.createStatement() ;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return rawConnect.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return rawConnect.prepareCall(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		rawConnect.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return rawConnect.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		rawConnect.commit();
	}

	@Override
	public void rollback() throws SQLException {
		rawConnect.rollback();
	}

	//释放自己到连接池中
	public void close() throws SQLException {
		pool.backConnection(this);
	}
}
