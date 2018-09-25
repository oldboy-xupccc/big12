package com.oldboy.java.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 虚拟内存
 */
public class VirtualMemoryDemo {
	public static void main(String[] args) throws Exception {
		//随机访问文件
		//hello world
		RandomAccessFile raf = new RandomAccessFile("d:\\java\\0.txt" ,"rw") ;
		MappedByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_WRITE , 1 , 9) ;
		for(int i = 1 ; i < 100000 ; i ++){
			buf.put((byte)(97 + (i % 20))) ;
			if(i % 9 == 0){
				buf.clear();
			}
		}
	}
}
