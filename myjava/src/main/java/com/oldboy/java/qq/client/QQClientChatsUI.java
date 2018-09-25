package com.oldboy.java.qq.client;

import com.oldboy.java.qq.common.ClientChatsMessage;
import com.oldboy.java.qq.util.QQUtil;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 客户端群聊界面
 */
public class QQClientChatsUI extends JFrame implements ActionListener {
	//所有私聊窗口
	public Map<String,QQClientChatSingleUI> allSingleChart = new HashMap<String,QQClientChatSingleUI>() ;

	//通信线程
	public QQClientCommThread commThread ;

	//历史聊天区
	private JTextArea taHistory;

	//好友列表
	private JList<String> lstFriends;

	//消息输入区
	private JTextArea taInputMessage;

	//发送按钮
	private JButton btnSend;

	//刷新好友列表按钮
	private JButton btnRefresh;

	public QQClientChatsUI() {
		init();
		this.setVisible(true);
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		this.setTitle("QQClient");
		this.setBounds(100, 100, 800, 600);
		this.setLayout(null);

		//历史区
		taHistory = new JTextArea();
		taHistory.setBounds(0, 0, 600, 400);

		JScrollPane sp1 = new JScrollPane(taHistory);
		sp1.setBounds(0, 0, 600, 400);
		this.add(sp1);

		//lstFriends
		lstFriends = new JList<String>();
		lstFriends.setBounds(620, 0, 160, 400);
		//添加鼠标监听器
		lstFriends.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//双击事件
				if(e.getClickCount() == 2){
					String recvAddr = lstFriends.getSelectedValue();
					QQClientChatSingleUI singleUI = allSingleChart.get(recvAddr) ;

					if(singleUI == null){
						singleUI = new QQClientChatSingleUI(recvAddr,commThread) ;
						allSingleChart.put(recvAddr , singleUI) ;
					}
					singleUI.setVisible(true);
				}
			}
		});
		this.add(lstFriends);

		//taInputMessage
		taInputMessage = new JTextArea();
		taInputMessage.setBounds(0, 420, 540, 160);
		this.add(taInputMessage);

		//btnSend
		btnSend = new JButton("发送");
		btnSend.setBounds(560, 420, 100, 160);
		btnSend.addActionListener(this);
		this.add(btnSend);

		//btnRefresh
		btnRefresh = new JButton("刷新");
		btnRefresh.setBounds(680, 420, 100, 160);
		btnRefresh.addActionListener(this);
		this.add(btnRefresh);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(-1);
			}
		});
	}

	/**
	 * 按钮的点击事件
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//发送按钮
		if(source == btnSend){
			String txt = taInputMessage.getText();
			if(txt != null && !txt.trim().equals("")){
				ClientChatsMessage msg = new ClientChatsMessage();
				msg.setMessage(txt);

				taInputMessage.setText("");
				try {
					commThread.sendMessage(msg);
					System.out.println("发送成功!");
				} catch (Exception e1) {
					System.out.println("发送失败! " + e1.getMessage());
				}
			}
		}
	}

	/**
	 * 刷新好友列表
	 */
	public void refreshFriendList(List<String> list) {
		String localAddr = QQUtil.getLocalAddr(commThread.sock) ;
		list.remove(localAddr) ;
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String s : list) {
			listModel.addElement(s);
		}
		lstFriends.setModel(listModel);
	}

	/**
	 * 更新历史区域内容
	 */
	public void updateHistory(String who ,String msg) {
		taHistory.append("[" + who + "]说:\r\n");
		String formatStr = msg.replace("\n", "\n\t");
		formatStr = "\t" + formatStr + "\r\n";
		taHistory.append(formatStr);
	}
}
