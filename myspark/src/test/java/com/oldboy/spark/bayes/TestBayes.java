package com.oldboy.spark.bayes;

import org.junit.Test;

import java.util.Random;

/**
 * Created by Administrator on 2018/10/22.
 */
public class TestBayes {
	/**
	 * 测试随机数种子
	 */
	@Test
	public void testRandomSeed(){
		Random r1 = new Random(1000);
		System.out.println(r1.nextFloat());
		System.out.println(r1.nextFloat());
		System.out.println(r1.nextFloat());
		System.out.println(r1.nextFloat());
		System.out.println(r1.nextFloat());
		System.out.println("=========");
		Random r2 = new Random(1000);
		System.out.println(r2.nextFloat());
		System.out.println(r2.nextFloat());
		System.out.println(r2.nextFloat());
		System.out.println(r2.nextFloat());
		System.out.println(r2.nextFloat());

	}
}
