package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.mapper.MessageMapper;
import com.atsu.tabletennisreservation.pojo.Message;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MessageService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private CommonService commonService;
    @Override
    public int saveMessage(Message message) {
        //如果不存在guid，则新生成一个
        if (StringTool.isNull(message.getGuid())){
            message.setGuid(commonService.getId());
        }
        if (StringTool.isNull(message.getCreateTime())){
            message.setCreateTime(commonService.getNowDateStr());
        }
        return messageMapper.saveMessage(message);
    }

    @Override
    public int deleteMessage(Message message) {
        return messageMapper.deleteMessage(message);
    }

    @Override
    public List<Message> getMessageList(Message message) {
        return messageMapper.getMessageList(message);
    }

    @Override
    public boolean saveSystemMessage(String text, String targetId) {
        Message message=new Message();
        message.setTargetId(targetId);
        message.setOriginName("system");
        message.setOriginId("system");
        message.setCreateTime(commonService.getNowDateStr());
        message.setGuid(commonService.getId());
        message.setType(1);//1 单点消息 2 全体消息
        message.setCategory(1);// 1 系统消息 2 用户消息
        message.setIsRead(0);//未读
        message.setValue(text);
        int i = messageMapper.saveMessage(message);
        return i>0;
    }

    @Override
    public PageResult<Message> getUserSystemMessageListPage(Integer pageNo, Integer pageSize,Message message) {
        PageHelper.startPage(pageNo,pageSize);//进行代理分页
//        Message message=new Message();
        User user = commonService.getThisUserId();
        message.setTargetId(user.getGuid());
        message.setType(1);//单点消息
        message.setCategory(1);//系统消息
        List<Message> messageList = messageMapper.getMessageList(message);
        PageInfo<Message> pageInfo=new PageInfo<>(messageList);
        //分页结果对象
        PageResult<Message> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<Message> getAllSystemMessageListPage(Integer pageNo, Integer pageSize, Message message) {
        PageHelper.startPage(pageNo,pageSize);//进行代理分页
        message.setType(2);//全体消息
        if (message.getCategory()==null){
            message.setCategory(1);//系统消息
        }
        List<Message> messageList = messageMapper.getMessageList(message);
        PageInfo<Message> pageInfo=new PageInfo<>(messageList);
        //分页结果对象
        PageResult<Message> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public boolean setMessageRead(List<String> ids, Integer isRead) {
        Message message=new Message();
        message.setIsRead(isRead);
        int i = messageMapper.updateMessageByIdBatch(message, ids);
        return i>0;
    }

    @Override
    public int deleteMessageByIdBatch(Message message, List<String> ids) {
        return messageMapper.deleteMessageByIdBatch(message,ids);
    }

    @Override
    public int getCount(Message message) {
        return messageMapper.getCount(message);
    }

    @Override
    public List<String> getIds(Message message) {
        return messageMapper.getIds(message);
    }

    @Override
    public int updateMessageById(Message message) {
        return messageMapper.updateMessageById(message);
    }

    @Override
    public boolean setToNotice(String guid) {
        //1、将原有公告消息设置为系统消息
        Message message=new Message();
        message.setCategory(7);
        List<Message> messageList = messageMapper.getMessageList(message);
        messageList.get(0).setCategory(1);
        messageMapper.updateMessageById(messageList.get(0));
        //2、设置当前指定的消息为公告
        message.setCategory(7);
        message.setGuid(guid);
        messageMapper.updateMessageById(message);
        return true;
    }

    @Override
    public int setThisUserSystemMessageRead() {
        User user = commonService.getThisUserId();
        Message condition=new Message();
        condition.setTargetId(user.getGuid());//目标用户为当前登录用户
        condition.setType(1);//单点消息
        condition.setCategory(1);//系统消息
        return  messageMapper.setRead(condition);
    }
    //获取当前用户离线消息数量
    @Override
    public int getThisUserChatMessageCount() {
        User user = commonService.getThisUserId();
        Message condition=new Message();
        condition.setTargetId(user.getGuid());
        condition.setType(1);//单点消息
        condition.setCategory(6);//用户聊天消息
        return  messageMapper.getCount(condition);
    }

}
