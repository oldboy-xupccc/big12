package com.oldboy.java.jvm;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2018/9/14.
 */
public class TestOffHeap {
	public static void main(String[] args) {
		ByteBuffer buf = ByteBuffer.allocateDirect(200 * 1024 * 1024);
		System.out.println();
		buf = null ;
		System.gc();
		System.out.println();

	}
}
