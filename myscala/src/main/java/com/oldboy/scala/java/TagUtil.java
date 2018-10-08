package com.oldboy.scala.java;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.internal.instrumentation.TypeMapping;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TagUtil {

	/**
	 * 从json中抽取评论集合
	 */
	public static List<String> extractTag(String json){
		List<String> list = new ArrayList<String>() ;
		//将字符串解析成json对象
		JSONObject obj = JSON.parseObject(json) ;
		JSONArray arr = obj.getJSONArray("extInfoList");
		if(arr != null && arr.size() > 0){
			//得到数组的第一个json对象
			JSONObject firstObj= arr.getJSONObject(0) ;
			JSONArray values = firstObj.getJSONArray("values") ;
			if(values != null && values.size() > 0){
				for(int i = 0 ; i< values.size() ; i ++){
					String tag = values.getString(i) ;
					list.add(tag) ;
				}
			}
		}
		return list ;
	}

	/**
	 * java方法定义泛型
	 */
	public static <T> T getMiddle(List<T> list){
		return list.get(list.size() / 2) ;
	}

	@Test
	public void testMethod(){
		List<Integer> list = new ArrayList<Integer>() ;
		list.add(1);
		list.add(2);
		list.add(3);
		System.out.println(getMiddle(list)) ;
	}
}
