package com.atsu.tabletennisreservation.utils;

import com.atsu.tabletennisreservation.pojo.Reserve;

import java.util.HashMap;
import java.util.Map;

//消息工具类
public class MessageUtil {
   public static String getMessageText(String mapping, Reserve reserve){
       StringBuffer text=new StringBuffer();
       //       预订成功
       if ("ydcg".equals(mapping)){
            text.append("[预订成功]\n").append("您已成功预订").append(reserve.getTableCode()).append("号球桌。\n");
            text.append("使用时间为：").append(reserve.getStartDate()).append("\n");
            text.append("使用时长(小时)：").append(reserve.getUseTime());
       }
       return text.toString();
   }

}
