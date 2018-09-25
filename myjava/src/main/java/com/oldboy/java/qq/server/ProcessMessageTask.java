package com.oldboy.java.qq.server;

import com.oldboy.java.qq.common.BaseMessage;
import com.oldboy.java.qq.common.ServerChatMessage;
import com.oldboy.java.qq.util.MessageFactory;
import com.oldboy.java.qq.util.QQUtil;

import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 处理消息
 */
public class ProcessMessageTask implements  Runnable{

	private SelectionKey key  ; ;

	public ProcessMessageTask(SelectionKey key){
		this.key = key;
	}
	public void run() {
		QQServer server = QQServer.getInstance();
		SocketChannel sc = (SocketChannel) key.channel();
		ReentrantLock lock = (ReentrantLock) key.attachment();
		boolean b = lock.tryLock() ;
		if(b){
			try {
				BaseMessage msg = MessageFactory.parseClientMessageFromChannel(sc) ;
				if(msg != null){
					switch (msg.getMessageType()){
						case BaseMessage.SERVER_TO_CLIENT_CHATS:
							server.broadcastMessage(msg) ;
							break ;
						case BaseMessage.SERVER_TO_CLIENT_CHAT:
							server.forwardMessage(msg , new String(((ServerChatMessage)msg).getRecvAddrBytes()));
							break ;
						case BaseMessage.SERVER_TO_CLIENT_REFRESH_FRIENDS:
							server.forwardMessage(msg , QQUtil.getRemoteAddr(sc.socket()));
							break ;
					}
				}
			} catch (Exception e) {
				Socket s = ((SocketChannel) key.channel()).socket();
				System.out.println(QQUtil.getRemoteAddr(s) + "下线了!");
				server.remoteClient(QQUtil.getRemoteAddr(s));
				server.broadcastFriendLists();
			}
			lock.unlock();
		}
	}
}
