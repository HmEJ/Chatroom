package com.nydx.chatroom;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.sound.sampled.Line;
import javax.swing.*;


public class LoginPage extends JFrame {

    //声明组件
    private JTextField useraccount = new JTextField();//声明用户名文本框
    private JLabel useraccounttext = new JLabel();//声明用户名标签（账号）
    private JLabel userpasswordtext = new JLabel();//声明用户名标签（密码）
    private JPasswordField userpassword = new JPasswordField();//声明密码文本框
    private JButton Loginbutton = new JButton();//声明登录按钮
    private JButton exit = new JButton();//声明关闭按钮
    private JButton register = new JButton();//声明注册按钮
    private JButton showPwd = new JButton();


    private JSeparator Line = new JSeparator();//分隔线
    private ImageIcon imageIcon;//声明图片
    private JLabel Label;//一个标签，把图片放给它，再把它放在容器中

    private BufferedReader in = null;
    private PrintStream out = null;
    JCheckBox checkBox;  //选中事件



    public LoginPage(){
        setTitle("聊天登录");//设置窗体标题
        getContentPane().setLayout(null);//获取容器，设置布局（null即是无布局，按坐标来）
        this.setLocation(350,80);//设置坐标（窗体位置）
        Label = new JLabel();//实例化了背景图片的载体
        //图片是不能直接放置在窗口上或者是容器中的
        String picFile = System.getProperty("user.dir")+File.separator + "pics" + File.separator + "image.jpg";//找到图片的路径(字符串)
        //但在开发中禁止使用绝对路径，需要是相对路径
        imageIcon = new ImageIcon(new ImageIcon(picFile).getImage().getScaledInstance(1000,550,Image.SCALE_DEFAULT));//实例化图片对象，把图片设置进去，(1000,550)是图片的大小
        //还需要把图片对象设置给JLabel
        Label.setHorizontalAlignment(SwingConstants.CENTER);//设置JLabel的内容的对齐方式
        Label.setIcon(imageIcon);//设置图片
        Label.setBounds(30,0,1130,500);//它控制的不是图片，前两个是组件左上角在容器中的坐标 后两个是组件的宽度和高度
        getContentPane().add(Label);
        //设置账号相关
        useraccounttext.setText("账号");//准备文本标签，设置标签内容
        useraccounttext.setBounds(120,550,100,50);//文本标签的坐标以及宽度和高度的设置
        getContentPane().add(useraccounttext);//把文本标签添加到容器上
        useraccount.setBounds(160,560,340,34);//准备文本框,设置它的坐标以及宽高
        getContentPane().add(useraccount);//把文本框放在容器上
        //设置密码相关

        userpasswordtext.setText("密码");//密码这俩字
        userpasswordtext.setBounds(120,650,100,50);//设置密码这俩字的坐标和宽高
        getContentPane().add(userpasswordtext);//放到容器中
        userpassword.setBounds(160,660,340,34);//
        getContentPane().add(userpassword);//
        //设置按钮，登录按钮和关闭按钮

        Loginbutton.setText("登录");//登录按钮上登录那俩字
        Loginbutton.setBounds(140,760,86,22);//登录按钮的坐标和宽高
        getContentPane().add(Loginbutton);//放到容器中

        exit.setText("关闭");
        exit.setBounds(260,760,86,22);
        getContentPane().add(exit);

        register.setText("注册");
        register.setBounds(380,760,86,22);
        getContentPane().add(register);



        checkBox= new JCheckBox("显示密码");
        checkBox.setBounds(510,660,100,34);
        getContentPane().add(checkBox);
        //水平分割线
        Line.setBackground(Color.black);
        Line.setBounds(0,520,3340,11);
        getContentPane().add(Line);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置默认关闭行为
        this.setResizable(false);//设置不可拉伸，即固定大小
        this.setSize(1200,900);//设置大小
        this.setVisible(true);//使窗口默认显示

        checkBox.addItemListener(new ItemListener() {   //触发条件：改变复选框选中状态
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange()==ItemEvent.SELECTED){
                    userpassword.setEchoChar((char)0);
                }else {
                    userpassword.setEchoChar('*');
                }
            }
        });

        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关闭窗体，可用dispose();但这样关不掉虚拟机
                System.exit(-1);
            }
        });

        Loginbutton.addActionListener(new LoginListener());//给登录按钮添加事件监听


        register.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开登陆界面
                dispose();
                Register cc = new Register();
            }
        });

    }
    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String useraccountValue = useraccount.getText();// 1、拿到文本框中输入的账号；
            String userpwdValue = userpassword.getText();//2、拿到文本框中输入的密码。
            if(e.getSource()==Loginbutton) {
                if(useraccountValue.equals("")) {//tips:用户名不能为空。
                    JOptionPane.showMessageDialog(null,"必须输入账号！","警告",JOptionPane.ERROR_MESSAGE);
                    return;//直接终止方法的执行。
                }
                if(userpwdValue.equals("")){
                    // 使用一个弹窗告诉提示：用户名不能为空
                    JOptionPane.showMessageDialog(null,"必须输入密码","警告",JOptionPane.ERROR_MESSAGE);
                    // 直接终止方法的执行
                    return;
                }
                Socket socket;
                try {
                    socket = new Socket("127.0.0.1",2000);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintStream(socket.getOutputStream());
                    // 按照约定，登录请求。帐号和密码拼接之前加一个0，帐号和密码中间加一个;
                    out.println("0" + useraccountValue + ";" + userpwdValue);
                    // 启动一个线程等待服务器返回结果
                    new Thread(new LoginHelper(socket)).start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,"服务器还没有打开！","提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if(e.getSource() == exit) {
                System.exit(-1);
            }
        }
    }


    // 需要处理服务器返回的对于帐号密码的验证结果的展示
    public class LoginHelper implements Runnable {

        private Socket socket;

        public LoginHelper(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("开始接收服务器返回的数据...");
            try {
                // 读取服务器返回的数据
                // "true#大蛇丸" "false#null"
                String login = in.readLine();
                System.out.println("接收到的服务器返回的数据为：" + login);
                while(login != null) {
                    // 根据#拆分字符串
                    // "true" "大蛇丸" "false" null
                    String [] temp = login.split("#");
                    if(temp[0].equals("true")){
                        // 用户名和密码正确的情况
                        // 如果登录成功
                        // 1.登录窗口关闭
                        dispose();
                        // 2.聊天窗口开启
                        ChatClient cc = new ChatClient(socket,temp[1]);
                        cc.lanchFrame();
                        // 在登录成功并打开聊天窗口成功以后，去获取服务器的连接
                        cc.doConnect(socket);
                        // 当前线程停止
                        Thread.currentThread().stop();
                    } else {
                        // 用户名或密码错误
                        JOptionPane.showMessageDialog(null,"用户名或密码错误","警告",JOptionPane.ERROR_MESSAGE);
                    }

                    login = in.readLine();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new LoginPage();
    }

}











