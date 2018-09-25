package com.oldboy.java.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 客户端
 */
public class MyNioClient {
	public static void main(String[] args) throws Exception {
		//
		SocketChannel sc = SocketChannel.open();


		//连接到远程服务器
		InetSocketAddress srvAddr = new InetSocketAddress("localhost" , 8888) ;
		sc.connect(srvAddr) ;
		//
		sc.configureBlocking(false);

		//挑选器
		Selector sel = Selector.open();

		//注册
		SelectionKey key = sc.register(sel , SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT) ;

		int index = 1 ;
		while(true){
			sel.select() ;
			if(key.isConnectable()){
				System.out.println("可连接!");
				sc.finishConnect();
			}

			if(key.isWritable()){
				System.out.println("可写");
				String msg = "tom" + index ;
				ByteBuffer buf = ByteBuffer.wrap(msg.getBytes()) ;
				//发送消息给服务器
				sc.write(buf) ;
				index ++ ;
			}
			//清空挑选集合
			sel.selectedKeys().clear();
			Thread.sleep(1000);
		}
	}
}
