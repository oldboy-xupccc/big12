package com.oldboy.java.qq.common;

import com.oldboy.java.qq.util.DataUtil;

import java.io.ByteArrayOutputStream;

/**
 * 服务器好友列表
 */
public class ServerRefreshFriendsMessage extends BaseMessage {
	private  byte[] friendsBytes ;

	public byte[] getFriendsBytes() {
		return friendsBytes;
	}

	public void setFriendsBytes(byte[] friendsBytes) {
		this.friendsBytes = friendsBytes;
	}

	public int getMessageType() {
		return SERVER_TO_CLIENT_REFRESH_FRIENDS;
	}

	public byte[] popPack() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		//消息类性
		baos.write(getMessageType());

		//好友列表消息长度
		baos.write(DataUtil.int2Bytes(friendsBytes.length));
		baos.write(friendsBytes);

		return baos.toByteArray() ;
	}
}
