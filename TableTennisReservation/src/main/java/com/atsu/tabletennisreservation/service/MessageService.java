package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//消息业务
public interface MessageService {
    //新增消息
    int saveMessage( Message message);
    //删除消息(根据条件删除)
    int deleteMessage( Message message);
    //查询消息
    List<Message> getMessageList( Message message);
    //新增系统消息
    boolean saveSystemMessage(String text,String targetId);
    //分页获取当前用户的所有系统消息
    PageResult<Message> getUserSystemMessageListPage(Integer pageNo,Integer pageSize,Message message);
    //获取全体消息
    PageResult<Message> getAllSystemMessageListPage(Integer pageNo,Integer pageSize,Message message);
    //设置消息已读
    boolean setMessageRead(List<String> ids,Integer isRead);
    //根据id批量删除消息,message为额外条件
    int deleteMessageByIdBatch( Message message,  List<String> ids);
    //查询数量
    int getCount( Message message);
    //查询id
    List<String> getIds( Message message);
    //更新消息
    int updateMessageById(@Param("message") Message message);
    //设置指定消息为系统公告
    boolean setToNotice(String guid);
    //设置当前登录用户的所有系统消息为已读
    int setThisUserSystemMessageRead();
    //获取用户聊天的离线消息数量
    int getThisUserChatMessageCount();
}
