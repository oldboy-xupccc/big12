package com.oldboy.java.qq.common;

import com.oldboy.java.qq.util.DataUtil;

import java.io.ByteArrayOutputStream;

/**
 * 服务器私聊
 */
public class ServerChatMessage extends BaseMessage {
	private  byte[] senderAddrBytes ;
	private byte[] recvAddrBytes ;
	private byte[] messageBytes ;

	public byte[] getSenderAddrBytes() {
		return senderAddrBytes;
	}

	public void setSenderAddrBytes(byte[] senderAddrBytes) {
		this.senderAddrBytes = senderAddrBytes;
	}

	public byte[] getRecvAddrBytes() {
		return recvAddrBytes;
	}

	public void setRecvAddrBytes(byte[] recvAddrBytes) {
		this.recvAddrBytes = recvAddrBytes;
	}

	public byte[] getMessageBytes() {
		return messageBytes;
	}

	public void setMessageBytes(byte[] messageBytes) {
		this.messageBytes = messageBytes;
	}

	public int getMessageType() {
		return SERVER_TO_CLIENT_CHAT;
	}

	public byte[] popPack() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		//消息类型
		baos.write(getMessageType());
		//发送地址长度
		baos.write(senderAddrBytes.length);
		//发送地址
		baos.write(senderAddrBytes);

		//消息长度
		baos.write(DataUtil.int2Bytes(messageBytes.length));
		baos.write(messageBytes);

		return baos.toByteArray() ;
	}
}
