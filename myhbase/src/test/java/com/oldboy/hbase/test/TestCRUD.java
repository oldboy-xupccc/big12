package com.oldboy.hbase.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

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

	/**
	 * 表
	 */
	@Test
	public void testPut() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Table t = conn.getTable(TableName.valueOf("ns2:t2")) ;
		Put put = new Put(Bytes.toBytes("row2")) ;
		put.addColumn(Bytes.toBytes("f1") , Bytes.toBytes("id") , Bytes.toBytes(2)) ;
		put.addColumn(Bytes.toBytes("f1") , Bytes.toBytes("name") , Bytes.toBytes("tom2")) ;
		put.addColumn(Bytes.toBytes("f1") , Bytes.toBytes("age") , Bytes.toBytes(11)) ;
		t.put(put);
		t.close();
		conn.close();
	}
	/**
	 * 表
	 */
	@Test
	public void testDelete() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Table t = conn.getTable(TableName.valueOf("ns2:t2")) ;
		Delete d = new Delete(Bytes.toBytes("row1")) ;
		d.addColumn(Bytes.toBytes("f1") , Bytes.toBytes("id")) ;
		t.delete(d);
		t.close();
		conn.close();
	}
	/**
	 * 表
	 */
	@Test
	public void testGet() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Table t = conn.getTable(TableName.valueOf("ns2:t2")) ;
		Get get = new Get(Bytes.toBytes("row1")) ;
		//get.addColumn(Bytes.toBytes("f1") , Bytes.toBytes("name")) ;
		Result r = t.get(get) ;
		outputResult(r);
		t.close();
		conn.close();
	}

	/**
	 * 表
	 */
	@Test
	public void testScan() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf) ;
		Table t = conn.getTable(TableName.valueOf("ns2:t2")) ;
		Scan scan = new Scan() ;
		ResultScanner scanner = t.getScanner(scan) ;
		Iterator<Result> it = scanner.iterator();
		while(it.hasNext()){
			outputResult(it.next());
		}
		t.close();
		conn.close();
	}

	private static void outputResult(Result r){
		List<Cell> cells = r.listCells();
		System.out.println("=================");
		for (Cell cell : cells) {
			String row = Bytes.toString(CellUtil.cloneRow(cell));
			String f = Bytes.toString(CellUtil.cloneFamily(cell));
			String col = Bytes.toString(CellUtil.cloneQualifier(cell));
			long ver = cell.getTimestamp();
			String val = Bytes.toString(CellUtil.cloneValue(cell));
			System.out.printf("%s/%s:%s/%d=%s\r\n" , row,f,col,ver,val);
		}
	}

	public static void main(String[] args) throws Exception {
//		testBatchInsert();
	}
	/**
	 * 测试批量插入
	 */
	@Test
	public void testBatchInsert() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		HTable t = (HTable) conn.getTable(TableName.valueOf("ns2:t2"));
		//关闭自动清理缓冲区
		t.setAutoFlush(false);

		DecimalFormat df = new DecimalFormat("000000") ;
		long start = System.currentTimeMillis() ;
		for(int i =  1 ; i <= 2000 ; i ++){
			Put put = new Put(Bytes.toBytes("row" + df.format(i)));
			put.setDurability(Durability.SKIP_WAL) ;
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"), Bytes.toBytes(i));
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("tom" + i));
			put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("age"), Bytes.toBytes(i % 100));
			t.put(put);
		}
		System.out.println(System.currentTimeMillis() -start);
		t.close();
		conn.close();
	}

	@Test
	public void testBatchInsert2() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		long start = System.currentTimeMillis() ;
		Thread d1 = insertInThread(1 , 30000 );
		Thread d2 = insertInThread(30000 , 60000 );
		Thread d3 = insertInThread( 60000 , 100000 );
		d1.join();
		d2.join();
		d3.join();
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * 启动分线程执行插入
	 */
	private static Thread insertInThread(final int start,final int end){
		Thread d = new Thread(){
			public void run(){
				try {
					Configuration conf = HBaseConfiguration.create();
					Connection conn = ConnectionFactory.createConnection(conf);
					HTable t = (HTable) conn.getTable(TableName.valueOf("ns2:t2"));
					//关闭自动清理缓冲区
					t.setAutoFlush(false);
					DecimalFormat df = new DecimalFormat("000000");
					for (int i = start; i <= end; i++) {
						Put put = new Put(Bytes.toBytes("row" + df.format(i)));
						put.setDurability(Durability.SKIP_WAL);
						put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"), Bytes.toBytes(i));
						put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("tom" + i));
						put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("age"), Bytes.toBytes(i % 100));
						t.put(put);
					}
					t.close();
					conn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		d.start();
		return d ;
	}

	/**
	 * 带缓存查询
	 */
	@Test
	public void cacheScan() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		Table t = conn.getTable(TableName.valueOf("ns2:t2"));
		Scan scan = new Scan();
		//切忌全表扫描，设置起始结束行
		scan.setStartRow(Bytes.toBytes("row000001")) ;
		scan.setStopRow(Bytes.toBytes("row010001")) ;
		System.out.println(scan.getCaching());
		scan.setCaching(10000) ;

		ResultScanner scanner = t.getScanner(scan);

		Iterator<Result> it = scanner.iterator();
		long start = System.currentTimeMillis() ;
		int index = 0 ;
		while (it.hasNext()) {
			it.next().getRow();
			//System.out.println(index ++);
		}
		System.out.println(System.currentTimeMillis() - start);
		t.close();
		conn.close();
	}

	/**
	 * 批次查询
	 */
	@Test
	public void batchScan() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Connection conn = ConnectionFactory.createConnection(conf);
		Table t = conn.getTable(TableName.valueOf("ns2:t3"));
		Scan scan = new Scan();
		scan.setBatch(0);
		Iterator<Result> r = t.getScanner(scan).iterator() ;
		while(r.hasNext()){
			outputResult(r.next());
		}
		t.close();
		conn.close();
	}


}
