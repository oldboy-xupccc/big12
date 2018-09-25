package com.oldboy.java.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/6.
 */
public class TestList {
	@Test
	public void testArrayList(){
		List<Integer> list = new ArrayList<Integer>() ;
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);
		list.add(9);
		list.add(10);
		list.add(11);
	}
	@Test
	public void testLinkedList(){
		List<Integer> list = new LinkedList<Integer>() ;
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(0 , 100) ;
	}

	@Test
	public void testBatchInsert(){
		List<Integer> list1 = new ArrayList<Integer>() ;
		long start  = System.currentTimeMillis() ;
		int n = 100000 ;
		for(int i = 0 ; i < n ; i ++){
			list1.add(0 , i);
		}
		System.out.println("arrayList : " + (System.currentTimeMillis() - start));

		List<Integer> list2 = new LinkedList<Integer>() ;
		start = System.currentTimeMillis() ;
		for (int i = 0; i < n; i++) {
			list2.add(0, i);
		}
		System.out.println("linkedlist : " + (System.currentTimeMillis() - start));
	}

	@Test
	public void testFind(){
		List<Integer> list1 = new ArrayList<Integer>() ;
		int n = 100000 ;
		for(int i = 0 ; i < n ; i ++){
			list1.add(0 , i);
		}
		long start  = System.nanoTime() ;
		list1.get(50000) ;
		System.out.println("arrayList : " + (System.nanoTime() - start));

		List<Integer> list2 = new LinkedList<Integer>() ;
		for (int i = 0; i < n; i++) {
			list2.add(0, i);
		}
		start = System.nanoTime() ;
		list2.get(50000) ;
		System.out.println("linkedlist : " + (System.nanoTime() - start));
	}
	@Test
	public void testListContains(){
		List<Integer> list1 = new ArrayList<Integer>() ;
		list1.add(1);
		System.out.println(list1.contains(1));
	}
}
