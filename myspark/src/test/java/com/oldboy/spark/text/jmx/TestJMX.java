package com.oldboy.spark.text.jmx;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2018/10/11.
 */
public class TestJMX {
	@Test
	public void testHost() throws Exception {
		String ip = InetAddress.getLocalHost().getHostAddress();
		System.out.println(ip);

		String name = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println(name);
		System.out.println(name);

		String tname = Thread.currentThread().getName() ;
		System.out.println(tname);
		System.out.println(this);
	}
}
