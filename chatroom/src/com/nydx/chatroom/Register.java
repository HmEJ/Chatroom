package com.nydx.chatroom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JFrame {

    private JTextField useraccount = new JTextField();//声明用户名文本框
    private JLabel useraccounttext = new JLabel();//声明用户名标签（输入账号）
    private JPasswordField userpassword = new JPasswordField();//声明密码文本框
    private JLabel userpasswordtext = new JLabel();//声明用户名标签（输入密码)
    private JPasswordField repeatpassword = new JPasswordField();//声明重复输入密码文本框
    private JLabel repeatpasswordtext = new JLabel();//声明用户名标签(重复输入密码)
    private JButton register = new JButton();//声明注册账号按钮
    private JButton back = new JButton();//声明返回登陆界面按钮
    private JTextField nickname = new JTextField();//声明昵称文本框
    private JLabel nicknametext = new JLabel();//声明昵称标签

    public Register() {
        setTitle("注册账号");
        getContentPane().setLayout(null);//获取容器，设置布局（null即是无布局，按坐标来）
        this.setLocation(350,80);//设置坐标（窗体位置）
        //设置账号相关
        useraccounttext.setText("账号");//准备文本标签，设置标签内容
        useraccounttext.setBounds(120,200,150,57);//文本标签的坐标以及宽度和高度的设置
        getContentPane().add(useraccounttext);//把文本标签添加到容器上
        useraccount.setBounds(160,215,340,34);//准备文本框,设置它的坐标以及宽高
        getContentPane().add(useraccount);//把文本框放在容器上


        //设置密码相关
        userpasswordtext.setText("密码");//密码这俩字
        userpasswordtext.setBounds(120,250,150,57);//设置密码这俩字的坐标和宽高
        getContentPane().add(userpasswordtext);//放到容器中
        userpassword.setBounds(160,265,340,34);//
        getContentPane().add(userpassword);//


        //设置重复密码相关
        repeatpasswordtext.setText("重复");
        repeatpasswordtext.setBounds(120,300,150,57);
        getContentPane().add(repeatpasswordtext);
        repeatpassword.setBounds(160,315,340,34);
        getContentPane().add(repeatpassword);


        //设置昵称相关
        nicknametext.setText("昵称");
        nicknametext.setBounds(120,150,150,57);
        getContentPane().add(nicknametext);
        nickname.setBounds(160,165,340,34);
        getContentPane().add(nickname);




        //设置按钮，注册钮和关闭按钮
        register.setText("注册");//登录按钮上注册那俩字
        register.setBounds(140,760,86,22);//注册按钮的坐标和宽高
        getContentPane().add(register);//放到容器中
        back.setText("返回");
        back.setBounds(260,760,86,22);
        getContentPane().add(back);


        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置默认关闭行为
        this.setResizable(false);//设置不可拉伸，即固定大小
        this.setSize(1200,900);//设置大小
        this.setVisible(true);//使窗口默认显示


        back.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginPage cc = new LoginPage();
            }
        });

        register.addActionListener(new LoginListener());//给登录按钮添加事件监听




    }


    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            String useraccountValue = useraccount.getText();// 1、拿到文本框中输入的账号；
            String userpwdValue = userpassword.getText();//2、拿到文本框中输入的密码；
            String userrepwdValue = repeatpassword.getText();//3、拿到文本框中的重复输入密码；
            String nicknameValue = nickname.getText();//4、拿到文本框中输入的昵称。

            if(e.getSource()==register) {
                if(useraccountValue.equals("")) {
                    //tips:账号不能为空。
                    JOptionPane.showMessageDialog(null,"请输入账号","警告",JOptionPane.ERROR_MESSAGE);
                    return;//直接终止方法的执行。
                }
                if(userpwdValue.equals("")){
                    // 使用一个弹窗告诉提示：用户名不能为空
                    JOptionPane.showMessageDialog(null,"请输入密码","警告",JOptionPane.ERROR_MESSAGE);
                    // 直接终止方法的执行
                    return;
                }
                if(userrepwdValue.equals("")){
                    //重复输入密码不能为空
                    JOptionPane.showMessageDialog(null,"请再次输入密码","警告",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(!userrepwdValue.equals(userpwdValue)) {
                    //要求再次输入的密码与上次输入的一致。
                    JOptionPane.showMessageDialog(null,"请保持输入密码一致","警告",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(nicknameValue.equals("")) {
                    //昵称不能为空
                    JOptionPane.showMessageDialog(null,"请输入昵称","警告",JOptionPane.ERROR_MESSAGE);
                    return;
                }



                String usersFile = System.getProperty("user.dir") + File.separator + "files" + File.separator + "userstable.txt";
                String usersinformation ="\r"+useraccountValue + "#"+userrepwdValue + "#"+ nicknameValue;


                try {
                    //创建 FileWriter
                    FileWriter file = new FileWriter(usersFile,true);

                    //创建 BufferedWriter
                    BufferedWriter output = new BufferedWriter(file);

                    //将字符串写入文件
                    output.write(usersinformation);

                    //关闭 writer
                    output.close();
                }

                catch (Exception e1) {
                    e1.getStackTrace();
                }
                if(e.getSource() == register) {
                    JOptionPane.showMessageDialog(null,"注册成功！","",JOptionPane.CLOSED_OPTION);
                    dispose();
                    LoginPage cc = new LoginPage();
                }
            }
        }
    }
}













