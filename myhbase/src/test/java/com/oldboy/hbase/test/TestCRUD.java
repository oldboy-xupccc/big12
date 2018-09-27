package com.oldboy.hbase.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Administrator on 2018/9/27.
 */
public class TestCRUD {
	/**
	 * 创建空间
	 */
	@Test
	public void testCreateNamespace() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Admin admin = conn.getAdmin();
		//名字空间描述符
		NamespaceDescriptor nd = NamespaceDescriptor.create("ns2").build();
		admin.createNamespace(nd);
		admin.close();
	}
	/**
	 * 表
	 */
	@Test
	public void testCreatTable() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Admin admin = conn.getAdmin();
		//创建表明对象
		TableName tname = TableName.valueOf("ns2:t2") ;
		HTableDescriptor td = new HTableDescriptor(tname) ;
		//创建列族
		HColumnDescriptor f1 = new HColumnDescriptor("f1") ;
		HColumnDescriptor f2 = new HColumnDescriptor("f2") ;

		//给表添加列族
		td.addFamily(f1) ;
		td.addFamily(f2) ;

		admin.createTable(td);
		admin.close();
	}
}
