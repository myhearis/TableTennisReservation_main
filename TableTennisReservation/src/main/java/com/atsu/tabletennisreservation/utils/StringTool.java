package com.atsu.tabletennisreservation.utils;

import java.util.Map;

//工具类
public class StringTool {
    //判空
    public static boolean isNull(String s){
        if (s==null||s.length()<=0)
            return true;
        return false;
    }
    //取默认值
    public static String nvl(String value,String defaultValue){
        if (isNull(value))
            return defaultValue;
        return value;
    }
    public static Integer nvl(Integer value,Integer defaultValue){
        if (value==null)
            return defaultValue;
        return value;
    }
    //取map集合中的元素并转化为String类型
    public static String getValueForMapString(Map map,String key){
        Object o = map.get(key);
        if (o!=null&&o instanceof String){
             return (String)o;
        }
        return null;
    }
}
