package com.atsu.tabletennisreservation.utils;

import com.atsu.tabletennisreservation.pojo.User;

//线程局部变量工具类
public class UserContext {
    private static ThreadLocal<User> userThreadLocal=new ThreadLocal<>();
    public static User getThisUser(){
        return userThreadLocal.get();
    }
    public static void setThisUser(User user){
        userThreadLocal.set(user);
    }
    public static void remove(){
        userThreadLocal.remove();
    }

}
