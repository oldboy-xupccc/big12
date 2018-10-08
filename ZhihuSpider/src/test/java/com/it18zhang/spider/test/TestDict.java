package com.it18zhang.spider.test;

import com.it18zhang.spider.util.DictUtil;
import org.junit.Test;

import java.util.List;

/**
 * Created by Administrator on 2018/10/1.
 */
public class TestDict {
	@Test
	public void test1(){
		List<?> list = DictUtil.getValues("useragents") ;
		System.out.println();

	}
}
