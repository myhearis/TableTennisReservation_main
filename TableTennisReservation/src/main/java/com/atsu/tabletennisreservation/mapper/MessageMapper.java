package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    //新增消息
    int saveMessage(@Param("message") Message message);
    //删除消息(根据条件删除)
    int deleteMessage(@Param("message") Message message);
    //查询消息
    List<Message> getMessageList(@Param("message") Message message);
    //更新消息
    int updateMessageById(@Param("message") Message message);
    //批量更新消息
    int updateMessageByIdBatch(@Param("message") Message message,@Param("ids") List<String> ids);
    //根据id批量删除消息,message为额外条件
    int deleteMessageByIdBatch(@Param("message") Message message,@Param("ids") List<String> ids);
    //查询数量
    int getCount(@Param("message") Message message);
    //查询id
    List<String> getIds(@Param("message") Message message);
    //设置消息已读
    int setRead(@Param("message") Message message);
}
