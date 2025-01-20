package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.Message;

import java.util.List;
import java.util.Map;

//用户通信业务接口
public interface UserChatService {
    //发送消息给指定用户,返回补全后的最终消息的状态信息
    Message sendMsgToUser(Message message) throws Exception;
    //查询targetid为当前用户的所有离线消息,并进行分组，返回一个map，key为源id，value为消息集合
    Map<String, List<Message>> getThisUserOfflineMessage();
    boolean deleteThisUserOfflineMessage(List<String> ids);
    //查询当前用户收到的离线消息数量
    Integer getThisUserOffLineMesCount();
    //查询当前用户所发送的离线消息数量
    Integer getThisUserSendOffLineMesCount();
    //查询当前用户当天所发送的离线消息数量
    Integer getNowDayThisUserSendOffLineMesCount();
    //校验当前用户是否允许推送消息给用户
    boolean verifyThisUserAllowSendMsg(Message message) throws Exception;
    //查询当前用户对目标用户所发送的所有离线消息的id
    List<String> getThisUserSendOffLineMsgIds(String targetId);
}
