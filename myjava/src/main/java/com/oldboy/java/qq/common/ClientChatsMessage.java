package com.oldboy.java.qq.common;

import com.oldboy.java.qq.util.DataUtil;

import java.io.ByteArrayOutputStream;

/**
 * 客户端群聊消息
 */
public class ClientChatsMessage extends BaseMessage{
	//消息内容
	private String message ;

	private String senderAddr ;

	public int getMessageType() {
		return CLIENT_TO_SERVER_CHATS;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderAddr() {
		return senderAddr;
	}

	public void setSenderAddr(String senderAddr) {
		this.senderAddr = senderAddr;
	}

	public byte[] popPack() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		//消息类型:1byte
		baos.write(getMessageType());
		//消息长度:4byte
		baos.write(DataUtil.int2Bytes(message.getBytes().length));
		//消息内容:n字节
		baos.write(message.getBytes());
		return baos.toByteArray();
	}
}
