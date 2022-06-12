package com.nydx.chatroom;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;


public class ChatClient {

	// 声明组件
	// JTextArea 文本区
	private JTextArea output;
	// 输入区 JTextField文本框
	private JTextField input;
	// 发送按钮
	private JButton sendButton;
	// 关闭按钮
	private JButton quitButton;

	private String nickname;


	// 声明两个流
	// 输入流，用来读取服务器回来的消息
	private BufferedReader in = null;
	// 输出流，用来向服务器发送消息
	private PrintStream out = null;

	// 已经在登录的时候就连接上服务器的那个socket对象
	private Socket socket;

	public ChatClient(Socket socket,String nickname) {
		// 实例化组件
		output = new JTextArea(100, 100);
		output.setEditable(false);
		input = new JTextField("--在此输入聊天信息--",50);
		sendButton = new JButton("发送");
		quitButton = new JButton("关闭");
		// 把已经连接好服务器的那个socket对象存下来
		this.socket = socket;
		this.nickname = nickname;

		input.addFocusListener(new FocusListener() {    //event包下的一个接口
			@Override
			public void focusGained(FocusEvent e) {
				if (input.getText().equals("--在此输入聊天信息--")) {
					input.setText("");
					input.setForeground(Color.black);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (input.getText().equals("")){
					input.setForeground(Color.gray);
					input.setText("--在此输入聊天信息--");
				}
			}
		});
	}

	// 加载窗体
	public void lanchFrame() {
		// 窗体
		JFrame frame = new JFrame("欢迎：" + nickname);
		// 窗体本身是没有布局功能的
		// 容器是通过窗体获取的
		Container container = frame.getContentPane();

		// 为我们的容器设置边界布局（麻将布局）
		container.setLayout(new BorderLayout());


		// new JScrollPane(output)代表生成一个带有滚动条的文本区
		container.add(new JScrollPane(output),BorderLayout.CENTER);
		container.add(input,BorderLayout.SOUTH);


		// 创建一个面板
		JPanel panel = new JPanel();
		// 我们只需要把按钮钉在面板上
		panel.add(sendButton);
		panel.add(quitButton);


		container.add(panel,BorderLayout.EAST);
		// 到此为止，聊天界面布局搭建完成

		// 对窗体的默认关闭行为进行设置
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 对关闭按钮做处理
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 系统退出 -1 强行退出 0 正常退出
				System.exit(-1);
			}
		});


		// 对窗体的设置
		// 设置窗体的尺寸，宽和高
		frame.setSize(800, 600);
		frame.setLocation(450, 230);
		// 窗体默认是不显示的
		frame.setVisible(true);

		// 发送按钮添加点击事件
		sendButton.addActionListener(new SendHandler());


		// 当我在文本区输入内容时，敲键盘的回车键，同样可以发送
		input.addActionListener(new SendHandler());



	}

	private void sendMessageToServer() {
		// 获取文本框内输入的内容
		String text = input.getText();
		text = "1" + nickname + "：" + text;
		if(text.trim().split("：").length == 1){
			JOptionPane.showMessageDialog(null,"聊天信息不能为空","警告",JOptionPane.ERROR_MESSAGE);
			return;
		}
		out.println(text + FormatTime.date2String());
		// 要把文本框清空
		input.setText("");
	}

	public void doConnect(Socket socket) {
		try {


			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
			new Thread(new MessageReader()).start();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("无法连接服务器！");
		}
	}

	private class MessageReader implements Runnable {

		// 定义一个标记
		// 保持监听任务
		private boolean keepListening = true;

		@Override
		public void run() {
			while (keepListening == true) {
				try {
					ChatServer cs = new ChatServer();

					String nextLine = in.readLine();
					System.out.println("客户端接收到的服务器返回的聊天信息：" + nextLine);
					// 服务器返回的消息要在聊天区进行展示
					output.append(nextLine + "\n");

				} catch (IOException e) {
					e.printStackTrace();
					keepListening = false;
				}
			}
		}
	}

	private class SendHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 1.把信息发给服务器
			sendMessageToServer();
		}
	}

}
