package com.oldboy.java.qq.common;

import java.util.List;

/**
 * 客户端刷新好友
 */
public class ClientRefreshFriendsMessage extends BaseMessage{

	private List<String> friendsList ;

	public int getMessageType() {
		return CLIENT_TO_SERVER_REFRESH_FRIENDS;
	}

	public List<String> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<String> friendsList) {
		this.friendsList = friendsList;
	}

	public byte[] popPack() throws Exception {
		return new byte[]{(byte)getMessageType()} ;
	}
}
