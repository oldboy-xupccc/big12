package com.oldboy.java.qq.common;

import com.oldboy.java.qq.util.DataUtil;

import java.io.ByteArrayOutputStream;

/**
 * 客户端私聊消息
 */
public class ClientChatMessage extends BaseMessage{
	//消息内容
	private String message ;
	//接受地址
	private String recvAddr ;

	//发送者地址
	private String senderAddr ;

	public int getMessageType() {
		return CLIENT_TO_SERVER_CHAT;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRecvAddr() {
		return recvAddr;
	}

	public void setRecvAddr(String recvAddr) {
		this.recvAddr = recvAddr;
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
		//接受者地址长度
		baos.write(recvAddr.getBytes().length);
		//接受者地址
		baos.write(recvAddr.getBytes());
		//消息内容长度
		baos.write(DataUtil.int2Bytes(message.getBytes().length));
		//消息内容
		baos.write(message.getBytes());

		return baos.toByteArray();
	}
}
