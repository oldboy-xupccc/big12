package com.oldboy.java.test;

import org.junit.Test;
import sun.misc.Cleaner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/13.
 */
public class TestJVM {
	/**
	 * 测试栈
	 */
	@Test
	public void testStack(){
		callSelf(1);
	}

	private void callSelf(int n){
		System.out.println(n);
		n++ ;
		callSelf(n);
	}

	public static void main(String[] args) throws InterruptedException {
		List<byte[]> list = new ArrayList<byte[]>() ;
		int n = 1024 * 1024 * 150;
		for(;;){
			byte[] bytes = new byte[n] ;
			list.add(bytes) ;
			System.out.println();
		}
	}
	
	@Test
	public void testJVM() throws InterruptedException {
		int n = 1024 * 1024 * 50;
		for (; ; ) {
			byte[] bytes = new byte[n];
			System.out.println();
		}
	}

	@Test
	public void testProcess() throws Exception {
		//5中gc算法
		String[] gcs = {
				"UseSerialGC" ,
				"UseParallelGC" ,
				"UseParallelOldGC" ,
				"UseConcMarkSweepGC" ,
				"UseG1C"
		} ;
		Runtime r = Runtime.getRuntime();

		for(String gc  :gcs){
			System.out.print(gc + "\t\t\t: ");
			for(int i = 0 ; i < 3 ; i ++){
				String javapc = String.format("java -Xms500m -Xmx500m -XX:NewSize=7m -XX:MaxNewSize=7m -XX:SurvivorRatio=5 -XX:+%s -cp d:/java Hello 10000 60000" , gc) ;
				long start = System.nanoTime() ;
				Process p = r.exec(javapc);
				p.waitFor();
				System.out.print((System.nanoTime() - start) + "\t");
			}
			System.out.println();
		}
	}

	/**
	 *测试离堆内存
	 */
	@Test
	public void testOffheapMemory() throws Exception {
		ByteBuffer buf = ByteBuffer.allocateDirect(1025 * 1024 * 500) ;
		Field f = buf.getClass().getDeclaredField("cleaner") ;
		f.setAccessible(true);
		Cleaner cleaner = (Cleaner) f.get(buf);
		cleaner.clean();
	}

	@Test
	public void testCopyInGeneral() throws Exception {
		FileInputStream fis = new FileInputStream("D:\\downloads\\iso\\ubuntu-12.04.5-server-amd64.iso") ;
		FileOutputStream fos = new FileOutputStream("f:\\2.iso") ;
		long start = System.currentTimeMillis() ;
		byte[] buf = new byte[1024] ;
		int len = 0 ;
		while((len = fis.read(buf)) != -1){
			fos.write(buf , 0 , len);
		}
		fos.close();
		fis.close();
		System.out.println(System.currentTimeMillis() - start);
	}

	/**
	 * 使用零拷贝技术实现复制,不超过2g
	 */
	@Test
	public void testCopyInZeroCopy() throws Exception {
		File f = new File("F:\\11.iso") ;
		FileInputStream fis = new FileInputStream(f);
		FileChannel fcSrc = fis.getChannel();

		FileOutputStream fos = new FileOutputStream("F:\\111.iso") ;
		FileChannel fcDest = fos.getChannel();

		//
		long start = System.currentTimeMillis();
		fcSrc.transferTo(0 , f.length() , fcDest) ;
		fcDest.close();
		fcSrc.close();
		System.out.println(System.currentTimeMillis() - start);
	}
}
