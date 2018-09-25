package com.oldboy.java.qq.util;

import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import com.oldboy.java.qq.common.*;
import com.oldboy.java.qq.server.QQServer;

/**
 *
 */
public class MessageFactory {

	/**
	 * 从通道中解析客户端消息，转换成服务器消息
	 */
	public static BaseMessage parseClientMessageFromChannel(SocketChannel sc) throws Exception {

		ByteBuffer buf1 = ByteBuffer.allocate(1) ;
		int len = sc.read(buf1);
		if(len != 1){
			return null ;
		}
		buf1.flip();

		//得到消息类型
		int msgType = buf1.get(0) ;

		switch (msgType){
			//群聊
			case BaseMessage.CLIENT_TO_SERVER_CHATS:
			{
				ServerChatsMessage msg = new ServerChatsMessage();
				//4字节缓冲区
				ByteBuffer buf4 = ByteBuffer.allocate(4);
				sc.read(buf4);
				//取出消息长度
				int msgLen = DataUtil.bytes2Int(buf4.array());

				//n个字节缓冲区
				ByteBuffer bufn = ByteBuffer.allocate(msgLen);
				sc.read(bufn);
				msg.setMessageBytes(bufn.array());
				msg.setSenderAddrBytes(QQUtil.getRemoteAddrBytes(sc.socket()));
				return msg;
			}

			//私聊
			case BaseMessage.CLIENT_TO_SERVER_CHAT:
			{
				ServerChatMessage msg = new ServerChatMessage();
				//接受者地址长度
				buf1.clear() ;
				sc.read(buf1) ;
				buf1.flip() ;
				int recvAddrLen = buf1.get(0) ;

				//接受者地址
				ByteBuffer bufn = ByteBuffer.allocate(recvAddrLen) ;
				sc.read(bufn) ;
				bufn.flip() ;
				msg.setRecvAddrBytes(bufn.array());

				//发送者地址
				msg.setSenderAddrBytes(QQUtil.getRemoteAddrBytes(sc.socket()));

				//消息长度
				ByteBuffer buf4 = ByteBuffer.allocate(4) ;
				sc.read(buf4) ;
				buf4.flip();
				int msgLen = DataUtil.bytes2Int(buf4.array()) ;

				//消息内容
				bufn = ByteBuffer.allocate(msgLen) ;
				sc.read(bufn);
				bufn.flip();
				msg.setMessageBytes(bufn.array());
				return msg ;
			}
			//刷新好友
			case BaseMessage.CLIENT_TO_SERVER_REFRESH_FRIENDS:
			{
				ServerRefreshFriendsMessage msg = new ServerRefreshFriendsMessage();
				msg.setFriendsBytes(QQServer.getInstance().getFriendsBytes());
				return msg ;
			}
		}
		return null ;
	}

	/**
	 * 从socket中解析服务器消息
	 */
	public static BaseMessage parseServerMesageFromSocket(Socket s) throws Exception {
		//
		InputStream in = s.getInputStream();
		//消息类型
		byte[] bytes1 = new byte[1] ;
		in.read(bytes1) ;
		int msgType = bytes1[0] ;

		//
		switch (msgType){
			//群聊
			case BaseMessage.SERVER_TO_CLIENT_CHATS:
			{
				in.read(bytes1);
				int senderAddrLen = bytes1[0];
				//读取发送地址
				byte[] bytesn = new byte[senderAddrLen];
				in.read(bytesn);
				//发送地址
				String senderAddrStr = new String(bytesn);

				byte[] bytes4 = new byte[4];
				in.read(bytes4);
				int msgLen = DataUtil.bytes2Int(bytes4);

				bytesn = new byte[msgLen];
				in.read(bytesn);
				String msg = new String(bytesn);

				ClientChatsMessage msg0 = new ClientChatsMessage();
				msg0.setMessage(msg);
				msg0.setSenderAddr(senderAddrStr);
				return msg0;
			}

			//私聊
			case BaseMessage.SERVER_TO_CLIENT_CHAT:
			{
				//
				in.read(bytes1);
				int senderAddrLen = bytes1[0];
				//读取发送地址
				byte[] bytesn = new byte[senderAddrLen];
				in.read(bytesn);
				//发送地址
				String senderAddrStr = new String(bytesn);

				byte[] bytes4 = new byte[4];
				in.read(bytes4);
				int msgLen = DataUtil.bytes2Int(bytes4);

				bytesn = new byte[msgLen];
				in.read(bytesn);
				String msg = new String(bytesn);

				ClientChatMessage msg0 = new ClientChatMessage();
				msg0.setMessage(msg);
				msg0.setSenderAddr(senderAddrStr);
				return msg0;
			}
			//好友列表
			case BaseMessage.SERVER_TO_CLIENT_REFRESH_FRIENDS:
			{
				byte[] bytes4 = new byte[4];
				in.read(bytes4);
				int friendsDataLen = DataUtil.bytes2Int(bytes4);

				byte[] bytesn = new byte[friendsDataLen];
				in.read(bytesn);
				List<String> friends = (List<String>) DataUtil.deserialData(bytesn);
				ClientRefreshFriendsMessage msg = new ClientRefreshFriendsMessage() ;
				msg.setFriendsList(friends);
				return msg ;
			}
		}
		return null ;
	}
}
