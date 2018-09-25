package com.oldboy.java.jvm;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * 自定义类加载
 */
public class MyClassLoader extends ClassLoader{
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			String path = "d:\\java\\" + name + ".class" ;
			FileInputStream fis = new FileInputStream(path) ;
			ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
			byte[] buf = new byte[1024] ;
			int len = -1 ;
			while((len = fis.read(buf)) != -1){
				baos.write(buf , 0 , len);
			}
			baos.close();
			fis.close();
			byte[] data = baos.toByteArray();
			Class clz = defineClass(data , 0 , data.length) ;
			return clz ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
}
