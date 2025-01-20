package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.exception.SysException;
import com.atsu.tabletennisreservation.pojo.Message;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MessageService;
import com.atsu.tabletennisreservation.service.UserChatService;
import com.atsu.tabletennisreservation.service.UserService;
import com.atsu.tabletennisreservation.webSocket.UserChatWebSocket;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserChatServiceImpl implements UserChatService {
    private static final  int DAY_ALLOW_OFF_LINE_COUNT=10;//当天允许发送离线消息的数量
    private static final  int ALL_ALLOW_OFF_LINE_COUNT=50;//允许用户存在的离线消息总量
    @Resource
    private CommonService commonService;
    @Resource
    private UserService userService;
    @Resource
    private UserChatWebSocket userChatWebSocket;
    @Resource
    private MessageService messageService;
    Gson gson=new Gson();
    @Override
    public Message sendMsgToUser(Message message) throws Exception {
        //补充信息
        message.setGuid(commonService.getId());
        message.setCreateTime(commonService.getNowDateStr());
        //消息类型 1 单点消息
        message.setType(1);
        //消息类别  6 用户聊天消息
        message.setCategory(6);
        //设置当前用户
        User user = commonService.getThisUserId();
        message.setOriginName(user.getUserName());
        message.setOriginId(user.getGuid());
        //设置状态未读
        message.setIsRead(0);
        //校验消息
        verifyThisUserAllowSendMsg(message);
        //判断目标用户是否在线
        boolean online = userChatWebSocket.isOnline(message.getTargetId());
        //如果在线，直接推送
        if (online){
            //如果是直接推送，设置消息已读
            message.setIsRead(1);
            userChatWebSocket.sendOneMessage(message.getTargetId(),gson.toJson(message));
            return message;
        }
        //如果不在线，数据保存到数据库
        else {
            //如果不在线，则为离线消息，设置消息为未读
            message.setIsRead(0);
            int i = messageService.saveMessage(message);
            if (i>0)
                return message;
        }
        return null;
    }

    @Override
    public Map<String, List<Message>> getThisUserOfflineMessage() {
        Map<String, List<Message>> map=new HashMap<String, List<Message>>();
        User user = commonService.getThisUserId();
        //查询当前用户的所有用户聊天消息
        Message message=new Message();
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        message.setTargetId(user.getGuid());
        List<Message> messageList = messageService.getMessageList(message);
        //开始生成映射
        for (int i=0;i<messageList.size();i++){
            Message msg = messageList.get(i);
            List<Message> list = map.get(msg.getOriginId());
            //存在映射时，加入list
            if (list!=null){
                list.add(msg);
            }
            //不存在映射时
            else {
                list=new ArrayList<Message>();
                list.add(msg);
                map.put(msg.getOriginId(),list);
            }
        }
        return map;
    }

    @Override
    public boolean deleteThisUserOfflineMessage(List<String> ids) {
        User user = commonService.getThisUserId();
        Message message=new Message();
        message.setTargetId(user.getGuid());
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        int i = messageService.deleteMessageByIdBatch(message, ids);
        return i>0;
    }
    //获取发送给当前用户的离线消息数量
    @Override
    public Integer getThisUserOffLineMesCount() {
        User user = commonService.getThisUserId();
        Message message=new Message();
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        message.setTargetId(user.getGuid());
        return   messageService.getCount(message);
    }

    @Override
    public Integer getThisUserSendOffLineMesCount() {
        User user = commonService.getThisUserId();
        Message message=new Message();
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        message.setOriginId(user.getGuid());
        return   messageService.getCount(message);
    }

    @Override
    public Integer getNowDayThisUserSendOffLineMesCount() {
        User user = commonService.getThisUserId();
        Message message=new Message();
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        message.setOriginId(user.getGuid());
        String nowDateStr = commonService.getNowDateStr();
        message.setProcessCreateDate(nowDateStr.substring(0,10));
        return  messageService.getCount(message);
    }
    //校验当前用户是否允许推送该消息
    @Override
    public boolean verifyThisUserAllowSendMsg(Message message) throws Exception {
        //1、检查发送的目标用户是否拉黑当前用户

        //2、检查是否为离线消息
        boolean online = userChatWebSocket.isOnline(message.getTargetId());
        if (!online){
            //3、检查当天离线消息总量是否超额
            if (getNowDayThisUserSendOffLineMesCount() > DAY_ALLOW_OFF_LINE_COUNT)
                throw new Exception("当前用户当前发送的离线消息总量已经超过当天限额["+DAY_ALLOW_OFF_LINE_COUNT+"]条，禁止推送离线消息！");
            //4、检查当前用户总离线消息数量是否超额
            if (getThisUserSendOffLineMesCount() > ALL_ALLOW_OFF_LINE_COUNT)
                throw new Exception("当前用户发送的离线消息总量已经超过限额["+ALL_ALLOW_OFF_LINE_COUNT+"]条，禁止推送离线消息！请先清理离线消息");
        }
        return true;
    }

    @Override
    public List<String> getThisUserSendOffLineMsgIds(String targetId) {
        User user = commonService.getThisUserId();
        //查询当前用户所发送给目标用户的离线消息id
        Message message=new Message();
        message.setTargetId(targetId);
        message.setOriginId(user.getGuid());
        message.setType(1);//单点消息
        message.setCategory(6);//用户聊天消息
        //查询id
        List<String> ids = messageService.getIds(message);
        return ids;
    }
}
