package com.nydx.chatroom;

import java.io.*;
import java.util.ArrayList;

public class UserDao {

    private static ArrayList<User> userlist = new ArrayList<>();

    private User user = new User(){
    };

    public static ArrayList<User> getUserlist() {
        return userlist;
    }

    public static void setUserlist(ArrayList<User> userlist) {
        UserDao.userlist = userlist;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // 静态代码块：类加载的同时就执行
    static {
        String usersFile = System.getProperty("user.dir") + File.separator +
                "files" + File.separator + "userstable.txt";
//        System.out.println(usersFile);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(usersFile));
            String line = reader.readLine();
            while(line != null){
                // 读到的数据是123456#abcdef#大蛇丸
                String[] temp = line.split("#");
                userlist.add(new User(temp[0],temp[1],temp[2]));

                line = reader.readLine();
            }
            System.out.println(userlist);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 实例块：创建对象时执行
    {

    }

    public boolean login(String useraccount,String userpwd){
        boolean flag = false;

        for (User user : userlist) {
            if(user.getUseraccount().equals(useraccount) && user.getUserpwd().equals(userpwd)){
                flag = true;
                this.user = user;
                return flag;
            }
        }

        return flag;
    }

    public static void main(String[] args) {
        UserDao dao = new UserDao();
        System.out.println(dao.login("123456", "abcdefg"));
    }

}