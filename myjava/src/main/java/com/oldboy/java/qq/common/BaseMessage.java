package com.oldboy.java.qq.common;

/**
 * 消息的基本类
 */
public abstract class BaseMessage {
	/**
	 * 客户端给服务器的消息
	 */
	public static final int CLIENT_TO_SERVER_CHATS = 0 ;			//群聊
	public static final int CLIENT_TO_SERVER_CHAT = 1 ;				//私聊
	public static final int CLIENT_TO_SERVER_REFRESH_FRIENDS = 2 ;	//刷新好友

	/**
	 * 服务器给客户端的消息
	 */
	public static final int SERVER_TO_CLIENT_CHATS =  3 ;
	public static final int SERVER_TO_CLIENT_CHAT =  4 ;
	public static final int SERVER_TO_CLIENT_REFRESH_FRIENDS=  5 ;

	//
	public abstract int getMessageType() ;

	/**
	 * 组装报文
	 */
	public byte[] popPack() throws Exception{
		return null ;
	}
}
