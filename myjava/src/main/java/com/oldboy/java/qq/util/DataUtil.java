package com.oldboy.java.qq.util;

import java.io.*;

/**
 * 数据工具类
 */
public class DataUtil {
	/**
	 * 整型到字节数组转换
	 */
	public static byte[] int2Bytes(int n){
		byte[] bytes4 = new byte[4] ;
		bytes4[0] = (byte)(n >> 24);
		bytes4[1] = (byte)(n >> 16);
		bytes4[2] = (byte)(n >> 8);
		bytes4[3] = (byte)(n >> 0);
		return bytes4 ;
	}

	/**
	 * 字节数组转换成整数
	 */
	public static int bytes2Int(byte[] bytes){
		return ((bytes[0] & 0xff) << 24)
				| ((bytes[1] & 0xff) << 16)
				| ((bytes[2] & 0xff) << 8)
				| ((bytes[3] & 0xff) << 0) ;
	}

	/**
	 * 使用串行化深度复制
	 */
	public static Object deeplyCopy(Object src) throws Exception {
		//串行
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		ObjectOutputStream oos = new ObjectOutputStream(baos) ;
		oos.writeObject(src);
		oos.close();
		baos.close();

		//反串
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()) ;
		ObjectInputStream ois = new ObjectInputStream(bais) ;
		Object copy = ois.readObject() ;
		ois.close();
		bais.close();
		return copy ;
	}

	/**
	 * 串行化
	 */
	public static byte[] serialData(Object src) throws Exception {
		//串行
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		ObjectOutputStream oos = new ObjectOutputStream(baos) ;
		oos.writeObject(src);
		oos.close();
		baos.close();
		return baos.toByteArray() ;
	}
	/**
	 * 反串行化
	 */
	public static Object deserialData(byte[] bytes) throws Exception {
		//反串
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object copy = ois.readObject();
		ois.close();
		bais.close();
		return copy;
	}
}
