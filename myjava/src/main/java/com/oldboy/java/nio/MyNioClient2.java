package com.oldboy.java.nio;

import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端
 */
public class MyNioClient2 {
	public static void main(String[] args) throws Exception {
		Socket sock = new Socket("localhost" , 8888);
		OutputStream out = sock.getOutputStream();
		int index = 0 ;
		for(;;){
			out.write(("hello" + index).getBytes());
			index ++ ;
			Thread.sleep(1000);
		}
	}
}
