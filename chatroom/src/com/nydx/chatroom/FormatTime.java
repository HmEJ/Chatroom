package com.nydx.chatroom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatTime {

    public static String date2String(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return simpleDateFormat.format(new Date());

    }

}
