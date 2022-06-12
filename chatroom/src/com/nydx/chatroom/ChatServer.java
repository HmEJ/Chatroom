package com.nydx.chatroom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatServer {


    // 这个List集合就是用来存储所有的已连接的客户端信息，这些客户端都是独立的线程，互不影响
    private List<ClientConnection> connectionList = new ArrayList<>();



    public void startServer()  {
        ServerSocket serverSocket = null; //监听特定端口
        Socket socket;  //对接客户端的socket  等待客户端的连接

        try {
            serverSocket = new ServerSocket(2000); //实例化
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1); // 强行终止虚拟机
        }
        //👆服务器搭建完毕

        System.out.println("启动服务器，端口号：" + 2000);
        //让服务器进入等待状态//一直等客户端连接，等到了就用socket对接
        while(true) {   //死循环
            try {
                socket = serverSocket.accept(); //接受所有连上的客户端  返回值是一个新的套接字描述符，包含的是客户端的ip和port信息
                System.out.println(socket.getInetAddress()+":"+socket.getPort()+"连接");
                handleClientConnection(socket); //开始处理客户端连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //多线程
    public void handleClientConnection(Socket socket){

        ClientConnection con = new ClientConnection(socket);

        connectionList.add(con);
        Thread t = new Thread(con);

        t.start();
    }
    //1.继承Tread 2.实现Runnable(负责开启多线程，就一个run()方法) 3.线程池 4.实现Callable
     //一个类如果实现一个接口，它必须重写这个接口中的所有抽象方法
    class ClientConnection implements Runnable {

        private Socket socket;
        private PrintStream out = null;
        private BufferedReader in = null;

        public ClientConnection(Socket socket) {
            this.socket = socket;
        }

        // 我们这个线程要做什么事
        // 就把这些事写在run方法里，最终线程开始执行时，执行run方法
        @Override
        public void run() {

            // 接收客户端发来的登录请求，接收用户名和密码
            OutputStream socketOutput = null;
            InputStream socketInput = null;
            String useraccount = null;
            String userpwd = null;
            try {
                // 输出流，向客户端回复，帐号密码是否正确
                out = new PrintStream(socket.getOutputStream());
                // 输入流，用来读取客户端发送过来的数据
                //socket只能给字节，不能给字符
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//把字节流转换成字符流
                // 读取客户端发送过来的数据
                String input = in.readLine();
                System.out.println("服务器接收到的数据：" + input);
                while(true){
                    // 0是登录
                    // 1是聊天
                    if(input != null && input.startsWith("0")){
                        // 用户名和密码中间用;隔开
                        // 0123456;abcdefg
                        String[] temp = input.split(";");   //根据;拆分为两部分

                        useraccount = temp[0].substring(1);
                        userpwd = temp[1];

                        // 判断用户名和密码是否正确
                        UserDao userDao = new UserDao();
                        boolean flag = userDao.login(useraccount, userpwd);
                        System.out.println("flag：" + flag);
                        // 服务器端返回的数据："true#大蛇丸" "false#null"
                        out.println(new Boolean(flag) + "#" + userDao.getUser().getNickname());
                        out.flush();
                    }
                    if(input != null && input.startsWith("1")){
                        System.out.println("服务器接收到的聊天信息为：" + input);
                        // 把聊天信息返回给客户端
                        sendToAllClients(input);
                    }
                    input = in.readLine();
                }
                } catch (IOException e) {
                    System.out.println("客户端与服务器断开连接...");
                }finally {
                    if(in != null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(out != null){
                        out.close();
                    }
                }

            }

        // 把服务器收到的数据处理之后返回给发送客户端
        public void sendMessageToClient(String message) {
            try {
                out.println(message.substring(1));
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAllClients(String message) {
        // 遍历存储客户端的集合，分别发送给每个客户端
        for (ClientConnection con : connectionList) {
            con.sendMessageToClient(message);
        }
    }



    public static void main(String[] args) {
        new ChatServer().startServer();

    }
}