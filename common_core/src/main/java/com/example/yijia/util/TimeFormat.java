package com.example.yijia.util;

import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
    public static String getCompareNowString(Date time){
        //当前时间
        String now = CurrentTimeUtils.now();//createdTime

        DateFormat df = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");

        try {
            //todo 时间比较
            Date now_d = df.parse(now);
            long diff = now_d.getTime() - time.getTime();//这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if(diff<60000){
                return "一分钟前";
            }else if(diff<60000*60){
                return minutes+"分钟前";
            }else if(diff<60000*60*24){
                return hours+"小时前";
            }else if(diff<60000*60*24*2){
                return "一天前";
            }else {
                SimpleDateFormat format = new SimpleDateFormat("M月d日");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(time);
                return format.format(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("M月d日");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(time);
        return format.format(time);
    }



    public static class CurrentTimeUtils {
        public static String now() {
            Time localTime = new Time("Asia/Hong_Kong");
            localTime.setToNow();
            return localTime.format("%Y-%m-%d %H:%M:%S");
        }
    }
}
