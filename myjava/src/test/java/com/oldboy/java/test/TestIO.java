package com.oldboy.java.test;

import com.oldboy.java.domain.Person;
import org.junit.Test;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/9/5.
 */
public class TestIO {
	@Test
	public void readFile() throws Exception {
		FileInputStream fin = new FileInputStream("d:\\java\\1.txt") ;
		FileOutputStream fos = new FileOutputStream("d:\\java\\111.txt") ;
		byte[] buf = new byte[1024] ;
		int len = 0;
		while((len = fin.read(buf)) != -1){
			fos.write(buf , 0 , len);
		}
		fos.close();
		fin.close();
	}
	@Test
	public void readFile2() throws Exception {
		FileInputStream fin = new FileInputStream("d:\\java\\1.txt") ;

		FileOutputStream fos = new FileOutputStream("d:\\java\\111.txt") ;
		int c = fin.read() ;

		fos.close();
		fin.close();
	}

	/**
	 * 写入一个字节
	 */
	@Test
	public void writeOneByte() throws Exception {
		FileOutputStream fos = new FileOutputStream("d:\\java\\333.txt") ;
		fos.write(new byte[]{-1});
		fos.close();
	}

	/**
	 * 读取一个字节
	 */
	@Test
	public void readOneByte() throws Exception {
		FileInputStream fin = new FileInputStream("d:\\java\\333.txt") ;
		int c = 0 ;
		while((c = fin.read()) != -1){
			System.out.println(c);
		}
		fin.close();
	}

	/**
	 * 读取一个字节
	 */
	@Test
	public void readFile3() throws Exception {
		FileInputStream fin = new FileInputStream("d:\\java\\999.txt") ;
		byte[] buf = new byte[30] ;
		int len = -1 ;
		while((len = fin.read(buf)) != -1){
			System.out.println(new String(buf , 0 , len));
		}
		fin.close();
	}
	/**
	 * 读取一个字节
	 */
	@Test
	public void readFile4() throws Exception {
		InputStreamReader fr = new InputStreamReader(new FileInputStream("d:\\java\\9999.txt") , "gbk") ;
		char[] buf = new char[2] ;
		int len = 0 ;
		while((len = fr.read(buf)) != -1){
			System.out.print(new String(buf , 0 , len));
		}
		fr.close();
	}

	/**
	 * 读取一个字节
	 */
	@Test
	public void writeUnicodeFile() throws Exception {
		FileOutputStream fr = new FileOutputStream("d:\\java\\8888.txt") ;
		fr.write("a中b".getBytes("unicode"));
		fr.close();
	}

	/**
	 * 内存流
	 */
	@Test
	public void writeByteArrayOutputStream() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		baos.write("abc".getBytes("unicode"));
		byte[] bytes = baos.toByteArray();
		baos.close();
		System.out.println(bytes.length);
	}

	/**
	 * 压缩流
	 */
	@Test
	public void testZip() throws Exception {
		FileOutputStream fos = new FileOutputStream("d:\\java\\xxx.zip") ;
		ZipOutputStream zos = new ZipOutputStream(fos) ;

		byte[] buf = new byte[1024] ;
		int len = -1 ;

		File dir = new File("d:\\java\\zip") ;
		File[] files = dir.listFiles();
		for(File f : files){
			if(f.isFile()){
				//文件名
				String fname = f.getName() ;
				FileInputStream fin = new FileInputStream(f) ;
				zos.putNextEntry(new ZipEntry(fname));
				while((len = fin.read(buf)) != -1){
					zos.write(buf , 0 , len);
				}
				fin.close();
			}
		}
		zos.close();
		fos.close();
	}

	/**
	 * 压缩流
	 */
	@Test
	public void testUnzip() throws Exception {
		FileInputStream fin = new FileInputStream("d:\\java\\xxx.zip") ;
		ZipInputStream zis = new ZipInputStream(fin) ;
		byte[] buf = new byte[1024] ;

		int len = 0 ;
		//
		ZipEntry entry ;

		String ouputDir = "d:\\java\\zip\\out" ;

		while((entry = zis.getNextEntry()) != null){
			String fname = entry.getName() ;
			File f = new File(ouputDir , fname) ;
			FileOutputStream fos = new FileOutputStream(f) ;
			while((len = zis.read(buf)) != -1){
				fos.write(buf , 0 , len);
			}
			fos.close();
		}
		zis.close();
	}

	/**
	 * 串行化
	 */
	@Test
	public void testSerial() throws Exception {
		Person p = new Person("");
		p.setId(15);
		p.setName("tomas");
		p.setAge(22);

		FileOutputStream fos = new FileOutputStream("d:\\java\\p.dat") ;
		ObjectOutputStream oos = new ObjectOutputStream(fos) ;
		oos.writeObject(p);
		oos.close();
		fos.close();
	}

	/**
	 * 串行化
	 */
	@Test
	public void testDeserial() throws Exception {
		FileInputStream fis = new FileInputStream("d:\\java\\ppp.dat") ;
		ObjectInputStream ois = new ObjectInputStream(fis) ;
		Person p = (Person) ois.readObject();
		ois.close();
		fis.close();
		System.out.println(p.getName());
		System.out.println(p.isMarried());
	}

	@Test
	public void testNewObject() throws Exception {
		Person p1 = new Person("p1") ;
		Person p2 = new Person("p2") ;
		Person p3 = new Person("p3") ;
		Person p4 = new Person("p4") ;

		Person p400 = new Person("p400") ;

		//设置关联关系
		p1.setFriend(p2);
		p2.setFriend(p3);
		p2.setDiren(p400);
		p3.setFriend(p4);

		Person p22 = (Person) deeplyCopy(p2);
		System.out.println();


	}

	/**
	 * 深度复制
	 */
	public static Object deeplyCopy(Object src){
		try {
			//串行
			ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
			ObjectOutputStream oos = new ObjectOutputStream(baos);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
}
