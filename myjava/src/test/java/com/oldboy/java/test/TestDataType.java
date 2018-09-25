package com.oldboy.java.test;

import org.junit.Test;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2018/9/5.
 */
public class TestDataType {
	@Test
	public void test1(){
		int b = 128;
		System.out.println((byte)b);
		//10进制
		System.out.println(124);
		//八进制
		System.out.println(0174);
		//16机制
		System.out.println(0x7c);
		System.out.println((byte)0xff);
		System.out.println((byte)0x7f);

		byte b1 = -0x7f ;

		System.out.println(~0);
		System.out.println(~-1);

		// <<
		//1000 0000
		System.out.println((byte)(128 << 1));
		// >> ,有符号
		System.out.println(-128 >> 1);
		// >>>
		System.out.println((byte)(-128 >>> 1));
	}

	@Test
	public void testCharset() throws Exception {
		char c = '中' ;
		c = '\u0061' ;
		c = '\u4e2d' ;
		System.out.println(c);

		String s = "a中b" ;
		//编码
		byte[] bytes = s.getBytes("unicode");
		System.out.println(new String(bytes , "unicode"));
		System.out.println((char)63);

		//中若人
	}

	/**
	 * 获取字符串的
	unicode表示方式
	 */
	@Test
	public void testUnicode(){
		System.out.println(getUnicodeString("中国人abc"));
		String str = "\u4e2d\u56fd\u4eba\u0061\u0062\u0063" ;
		System.out.println(str);
	}

	private static String getUnicodeString(String str){
		StringBuilder builder = new StringBuilder() ;
		char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e','f'} ;
		for(int i = 0 ; i < str.length() ; i ++){
			int ii = (int) str.charAt(i);
			builder.append("\\u");
			builder.append(chars[ii >> 12 & 0xf]);
			builder.append(chars[ii >> 8 & 0xf]);
			builder.append(chars[ii >> 4 & 0xf]);
			builder.append(chars[ii >> 0 & 0xf]);
		}
		return builder.toString() ;
	}

	@Test
	public void testMd5() throws Exception {
		MessageDigest md = MessageDigest.getInstance("md5") ;
		byte[] bytes = md.digest("abc".getBytes()) ;
		StringBuilder builder = new StringBuilder();
		char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		for (byte b : bytes) {
			builder.append(chars[b >> 4 & 0xf]);
			builder.append(chars[b >> 0 & 0xf]);
		}
		System.out.println(builder.toString());
	}

	/**
	 * 字符全量输出
	 */
	@Test
	public void outAllChars(){
		int cols = 0 ;
		for(int i = 1 ; i <= 0xffff ; i ++ ){
			System.out.printf("%d : %s\t" , i , (char)i);
			cols ++ ;
			if(cols > 10){
				System.out.println();
				cols = 0 ;
			}
		}
	}
}
