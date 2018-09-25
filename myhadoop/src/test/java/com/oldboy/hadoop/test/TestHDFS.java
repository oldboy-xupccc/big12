package com.oldboy.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/9/20.
 */
public class TestHDFS {
	@Test
	public void testPut() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS" , "hdfs://s105:8020");
		FileSystem fs = FileSystem.get(conf) ;
		OutputStream out = fs.create(new Path("/user/centos/data/2.txt")) ;
		out.write("how are you??".getBytes());
		out.flush();
		out.close();
	}

	@Test
	public void testPut2() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS" , "hdfs://s101:8020");
		FileSystem fs = FileSystem.get(conf) ;
		OutputStream out = fs.create(new Path("/user/centos/data/5.txt"), true, 1024, (short)2, 1024) ;
		out.write("abc".getBytes());
		//out.flush();
		out.close();
	}
}
