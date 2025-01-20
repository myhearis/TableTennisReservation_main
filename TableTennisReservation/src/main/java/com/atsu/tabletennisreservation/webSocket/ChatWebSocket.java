package com.atsu.tabletennisreservation.webSocket;

import com.atsu.tabletennisreservation.pojo.User;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
//客服会话
@Component
@ServerEndpoint(value = "/websocket/chat/{userId}",configurator = MyEndpointConfigurator.class)  // 接口路径 ws://localhost:8080/webSocket/{userId};
public class ChatWebSocket {
    private final Logger log=Logger.getLogger(this.getClass().getName());
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    /**
     * 用户ID
     */
    private String userId;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
    //  注：底下WebSocket是当前类名
    private static CopyOnWriteArraySet<ChatWebSocket> webSockets =new CopyOnWriteArraySet<>();
    // 用来存在线连接用户信息
    private static ConcurrentHashMap<String,Session> sessionPool = new ConcurrentHashMap<String,Session>();
    private static ConcurrentHashMap<String,String> kfAndUserMap = new ConcurrentHashMap<>();//保存客服和用户的信息
    private static ConcurrentHashMap<String,String> userAndKfMap = new ConcurrentHashMap<>();//保存用户和客服的信息
    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);//将当前用户的WebSocket保存到静态set集合中
            sessionPool.put(userId, session);//存入userid对WebSocket session的映射
            log.info("【聊天消息】有新的连接，总数为:"+webSockets.size());
        } catch (Exception e) {
        }
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            webSockets.remove(this);
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，总数为:"+webSockets.size());
            //如果存在客服和用户任意一方下线，移除信息
            if (kfAndUserMap.containsKey(this.userId)){
                //客服下线
                //获取用户信息
                String user_id = kfAndUserMap.get(this.userId);
                //移除用户连接
                userAndKfMap.remove(user_id);
                //移除客服数据
                kfAndUserMap.remove(this.userId);
            }
            if (userAndKfMap.containsKey(this.userId)){
                //用户下线
                String kf_id = userAndKfMap.get(this.userId);
                //移除客服处理的用户，将客服设置为空闲状态
                kfAndUserMap.remove(kf_id);//移除客服数据
                //移除用户对客服的映射
                userAndKfMap.remove(this.userId);
            }
        } catch (Exception e) {
        }
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端消息:"+message);
    }

    /** 发送错误时的处理
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.info("用户错误,原因:"+error.getMessage());
        error.printStackTrace();
    }


    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:"+message);
        for(ChatWebSocket webSocket : webSockets) {
            try {
                if(webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
        for(String userId:userIds) {
            Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    // 判断用户是否在线
    public boolean isOnline(String userId){
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()){
            return true;
        }
        return false;
    }
    //判断客服是否在线且空闲
    public boolean kfIsOnlineAndLeisure(String kfUserId){
        //判断客服是否在线
        if (isOnline(kfUserId)){
            //判断是否空闲
            String s = kfAndUserMap.get(kfUserId);
            if (s==null){
                return true;
            }
        }
        return false;
    }
    //客服与用户建立映射关系（单个）
    public boolean kfConnectUser(String userId,String kfUserId){
        //加同步锁,防止多线程冲突
        if (!kfIsOnlineAndLeisure(kfUserId)){
            return false;
        }
        synchronized (Object.class){
            //再判断一次数据，防止多线程导致数据被覆盖
            if (!kfIsOnlineAndLeisure(kfUserId)){
                return false;
            }
            //设置映射关系
            kfAndUserMap.put(kfUserId,userId);
            userAndKfMap.put(userId,kfUserId);
            return  true;
        }
    }
    //传入客服用户列表，与其中可用客服的进行连接
    public String kfConnectUser(String userId, List<User> kfUserList){
        //开始遍历
        for (int i=0;i<kfUserList.size();i++){
            User kf=kfUserList.get(i);
            //如果客服在线且空闲
            if (kfIsOnlineAndLeisure(kf.getGuid())){
                //开始竞争资源,如果竞争到了，直接返回
                if (kfConnectUser(userId,kf.getGuid())){
                    return kf.getGuid();
                }
                else
                    continue;

            }else
                continue;

        }
        return null;
    }
    //客服解除和客户的连接
    //返回被取消的连接用户id
    public String kfCancelConnectUser(String opUserId,String roleId){
        String beCancelUserId=null;//被取消的用户id
        synchronized (Object.class) {
            if ("customer".equals(roleId)) {
                String userId = kfAndUserMap.get(opUserId);
                userAndKfMap.remove(userId);
                kfAndUserMap.remove(opUserId);
                beCancelUserId = userId;
            }
            //用户主动取消连接
            else {
                //获取客服id
                String kfUserId = userAndKfMap.get(opUserId);
                //取消客服占用(如果存在且为当前操作用户)
                if (kfAndUserMap.containsKey(kfUserId) && kfAndUserMap.get(kfUserId).equals(opUserId)) {
                    //取消占用
                    kfAndUserMap.remove(kfUserId);
                    //移除用户对客服映射
                    userAndKfMap.remove(opUserId);
                    beCancelUserId = kfUserId;
                }
            }
        }
        return beCancelUserId;
    }
    //判断是否已经建立客服连接
    public static boolean isCreateKfConnect(String opUserId,String roleId){
        //客服角色
        if ("customer".equals(roleId)&&kfAndUserMap.containsKey(opUserId)&&kfAndUserMap.get(opUserId)!=null) {
           return true;
        }
        //用户角色
        else if ("normal".equals(roleId)&&userAndKfMap.containsKey(opUserId)&&userAndKfMap.get(opUserId)!=null){
            return true;
        }
        else
            return false;
    }
    public static ConcurrentHashMap<String, String> getKfAndUserMap() {
        return kfAndUserMap;
    }

    public static void setKfAndUserMap(ConcurrentHashMap<String, String> kfAndUserMap) {
        ChatWebSocket.kfAndUserMap = kfAndUserMap;
    }

    public static ConcurrentHashMap<String, String> getUserAndKfMap() {
        return userAndKfMap;
    }

    public static void setUserAndKfMap(ConcurrentHashMap<String, String> userAndKfMap) {
        ChatWebSocket.userAndKfMap = userAndKfMap;
    }
}
