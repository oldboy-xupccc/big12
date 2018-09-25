package com.oldboy.java.test;

import org.junit.Test;

/**
 */
public class TestCalc {

	@Test
	public void testCountUnique() {
		int[] arr = {0, 1, 2, 3, 4, 1, 2, Integer.MAX_VALUE};
		System.out.println(countUnique(arr));
	}

	/**
	 * 去重统计整数个数
	 */
	public static int countUnique(int[] arr) {
		//计算行数
		int rows = Integer.MAX_VALUE / 8 + 1;

		//初始化字节数组
		byte[] bytes = new byte[rows];

		//计数器
		int count = 0;
		//
		for (int i : arr) {
			//定位行数
			int row = i / 8;
			//定位列数
			int col = i % 8;
			//
			int r = (byte) (bytes[row] & (1 << col));
			if (r == 0) {
				count++;
				bytes[row] = (byte) (bytes[row] | (1 << col));
			}
		}
		return count;
	}
}
