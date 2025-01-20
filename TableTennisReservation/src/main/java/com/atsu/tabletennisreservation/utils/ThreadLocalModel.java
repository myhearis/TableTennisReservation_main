package com.atsu.tabletennisreservation.utils;

import com.atsu.tabletennisreservation.pojo.User;

//当前线程局部变量
public class ThreadLocalModel {
    private static ThreadLocal<User> userThreadLocal=new ThreadLocal<>();
    public static User getThisUser(){
        return userThreadLocal.get();
    }
    public static void setThisUser(User user){
        userThreadLocal.set(user);
    }
}
