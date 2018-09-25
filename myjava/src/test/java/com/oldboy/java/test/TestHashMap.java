package com.oldboy.java.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;




/**
 * Created by Administrator on 2018/9/6.
 */
public class TestHashMap {
	@Test
	public void test1(){
		Map<Integer , String> map = new HashMap<Integer, String>();
		map.put(1, "tom1") ;
		map.put(2, "tom2") ;
		map.put(1, "tom2") ;
		map.put(3, "tom3") ;
		System.out.println(map.size());
	}

	@Test
	public void testMyKey(){
//		MyKey key = new MyKey() ;
//		Map<MyKey, String> map = new HashMap<MyKey, String>();
//		map.put(key , "tom1");
//		map.put(key , "tom1");
//		map.put(key, "tom1");
//		map.put(key , "tom1");
//		System.out.println(map.size());
	}
}
