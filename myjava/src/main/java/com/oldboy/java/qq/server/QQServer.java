package com.oldboy.java.qq.server;

import com.oldboy.java.qq.common.BaseMessage;
import com.oldboy.java.qq.common.ServerRefreshFriendsMessage;
import com.oldboy.java.qq.util.DataUtil;
import com.oldboy.java.qq.util.IConstants;
import com.oldboy.java.qq.util.QQUtil;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 服务器,单例模式
 */
public class QQServer {

	private static QQServer instance ;

	private QQServer(){
	}

	public static QQServer getInstance(){
		if(instance != null){
			return instance ;
		}
		synchronized (QQServer.class){
			if(instance ==null){
				instance = new QQServer() ;
			}
		}
		return instance ;
	}

	//所有客户端集合
	private Map<String , SocketChannel> allClients = new HashMap<String, SocketChannel>() ;

	/**
	 * 启动服务器
	 */
	public void start(){
		try {
			//启动线程池
			ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(IConstants.QQ_SERVER_THREAD_POOL_CORES);

			//创建通道
			ServerSocketChannel ssc = ServerSocketChannel.open();
			//配置阻塞模式
			ssc.configureBlocking(IConstants.QQ_SERVER_CHANNEl_BLOCKING_MODE) ;
			//绑定
			InetSocketAddress addr = new InetSocketAddress(IConstants.QQ_SERVER_BIND_HOST, IConstants.QQ_SERVER_BIND_PORT) ;
			ssc.bind(addr) ;

			//开启挑选器
			Selector sel = Selector.open();
			//注册
			ssc.register(sel, SelectionKey.OP_ACCEPT) ;

			for(;;){
				//开始挑选
				sel.select() ;
				Iterator<SelectionKey> it = sel.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next() ;
					try {
						//接受新连接，注册通道
						if(key.isAcceptable()){

							SocketChannel sc0 = ssc.accept() ;
							System.out.println("有人上线了!");
							//有新连接
							sc0.configureBlocking(IConstants.QQ_SERVER_CHANNEl_BLOCKING_MODE) ;
							SelectionKey key0 = sc0.register(sel , SelectionKey.OP_READ) ;
							//创建独占锁
							ReentrantLock lock = new ReentrantLock();
							//将lock和key关联
							key0.attach(lock) ;

							//放置信息到allClients集合中
							String remoteAddr = QQUtil.getRemoteAddr(sc0.socket()) ;
							allClients.put(remoteAddr, sc0) ;

							//广播好友列表
							broadcastMessage(genFriendListMessage());
						}

						//常规通道
						if(key.isReadable()){
							pool.execute(new ProcessMessageTask(key));
						}
					} catch (Exception e) {
						//撤销通道
						key.cancel();
						allClients.remove(QQUtil.getRemoteAddr(((SocketChannel)key.channel()).socket())) ;
					}
					finally {
						//删除key
						it.remove();
					}
				}
				//休眠毫秒数
				Thread.sleep(IConstants.QQ_SERVER_ROUNDROBIN_INTERVAL_SECONDS * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到好友列表
	 */
	public List<String> getFriendsList(){
		return new ArrayList<String>(allClients.keySet());
	}

	/**
	 * 得到好友列表
	 */
	public byte[] getFriendsBytes() throws Exception {
		return DataUtil.serialData(getFriendsList()) ;
	}

	/**
	 * 向所有client广播消息
	 */
	public void broadcastMessage(BaseMessage msg){
		System.out.println("广播消息");
		for(SocketChannel sc : allClients.values()){
			try {
				sc.write(ByteBuffer.wrap(msg.popPack())) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 转发消息(单发消息)
	 */
	public void forwardMessage(BaseMessage msg , String recvAddr) throws Exception {
		SocketChannel sc = allClients.get(recvAddr) ;
		if(sc != null){
			sc.write(ByteBuffer.wrap(msg.popPack())) ;
		}
	}

	/**
	 * 生成好友列表消息
	 */
	public ServerRefreshFriendsMessage genFriendListMessage() throws Exception {
		ServerRefreshFriendsMessage msg = new ServerRefreshFriendsMessage();
		msg.setFriendsBytes(getFriendsBytes());
		return msg ;
	}

	public void broadcastFriendLists(){
		try {
			ServerRefreshFriendsMessage msg = genFriendListMessage();
			broadcastMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从好友列表中删除指定用户
	 */
	public void remoteClient(String addr){
		allClients.remove(addr) ;
	}

}
