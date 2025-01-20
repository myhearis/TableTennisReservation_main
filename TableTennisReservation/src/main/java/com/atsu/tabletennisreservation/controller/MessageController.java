package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.Message;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MessageService;
import com.atsu.tabletennisreservation.service.UserService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.webSocket.ChatWebSocket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;
    @Resource
    private CommonService commonService;
    @Resource
    private ChatWebSocket chatWebSocket;
    @Resource
    private UserService userService;
    //消息主页
    @GetMapping("/")
    public String index(){
        return "message/message";
    }
    //消息主页
    @GetMapping("/pushAllUserMessage")
    public String pushAllUserMessageIndex(){
        return "message/pushAllUserMessage";
    }
    @GetMapping("/user/{pageNo}")
    @ResponseBody
    public Result getUserSystemMessageListPage(@PathVariable("pageNo") Integer pageNo,
                                               @RequestParam( value = "createTime",required = false) String createTime,
                                               @RequestParam(value = "isRead",required = false)  Integer isRead
                                               ){
        Message message=new Message();
        message.setCreateTime(createTime);
        message.setIsRead(isRead);
        PageResult<Message> pageResult = messageService.getUserSystemMessageListPage(pageNo, 5,message);
        return Result.success(pageResult);
    }
    //获取全体消息
    @GetMapping("/all/{pageNo}")
    @ResponseBody
    public Result getAllSystemMessageListPage(@PathVariable("pageNo") Integer pageNo,
                                              @RequestParam( value = "createTime",required = false) String createTime,
                                              @RequestParam(value = "isRead",required = false)  Integer isRead,
                                              @RequestParam(value = "category",required = false)  Integer category
    ){

        Message message=new Message();
        message.setCreateTime(createTime);
        message.setIsRead(isRead);
        message.setCategory(category);
        PageResult<Message> pageResult = messageService.getAllSystemMessageListPage(pageNo, 5,message);
        return Result.success(pageResult);
    }
    @PutMapping("/user")
    @ResponseBody
    public Result setMessageRead(@RequestBody JsonNode jsonData){
        Result result=null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> idList = objectMapper.convertValue(jsonData.get("ids"), List.class);
        //更新状态信息
        if (idList!=null&&idList.size()>0){
            boolean b = messageService.setMessageRead(idList, 1);
            if (b){
                result=Result.success("","状态更新成功!");
            }
        }
        else {
            result=Result.success("ok");
        }
        return result;
    }
    //客服聊天页
    @GetMapping("/customerService")
    public String customerServiceIndex(){
        return "message/customerService";
    }
    //处理客服聊天的消息
    @PostMapping("/customerService/msg")
    @ResponseBody
    public Result saveCustomerMsg(@RequestBody Message message){
        //校验当前用户是否已经建立客服连接
        User user = commonService.getThisUserId();
        boolean createKfConnect = ChatWebSocket.isCreateKfConnect(user.getGuid(), user.getRoleId());
        if (!createKfConnect)
            return Result.failed("失败","当前用户并未与客服建立间接!");
        //初始化数据信息
        message.setGuid(commonService.getId());
        //设置类型
        message.setType(1);//单点消息
        message.setCategory(2);//用户消息
        message.setIsRead(0);
        message.setCreateTime(commonService.getNowDateStr());
        //推送数据给前端
        Gson gson=new Gson();
        chatWebSocket.sendOneMessage(message.getTargetId(),gson.toJson(message));
        System.out.println(message);
        return Result.success("ok");
    }
    //用户请求连接客服(用户角色使用)
    @GetMapping("/customerService/connect")
    @ResponseBody
    public Result connectCustomer(){
        User user = commonService.getThisUserId();
//        User onlineCustomerUser = userService.getOnlineCustomerUser();
        //获取所有客服角色用户
        List<User> kfUserList = userService.getKfUserList();
        //判断客服是否在线
        //建立客服和当前用户映射关系
//        boolean b = chatWebSocket.kfConnectUser(user.getGuid(), onlineCustomerUser.getGuid());
        //竞争在线的客服，如果竞争到了，返回客服id
        String targetKfUserId = chatWebSocket.kfConnectUser(user.getGuid(), kfUserList);
        if (!StringTool.isNull(targetKfUserId)){
            //发送消息，请求客服连接
            Message message=new Message();
            message.setOriginId(user.getGuid());
            message.setOriginName(user.getUserName());
            message.setCategory(3);//请求连接消息
            Gson gson=new Gson();
            //发送给客服用户,等待客服确认连接
//            chatWebSocket.sendOneMessage(onlineCustomerUser.getGuid(),gson.toJson(message));
            chatWebSocket.sendOneMessage(targetKfUserId,gson.toJson(message));
            return Result.success(targetKfUserId,"等待客服确认连接");
        }
        else {
            return Result.failed("连接失败","连接失败，客服可能不在线或占线中!请稍后再试");
        }
    }
    //取消客服连接(客服用户角色使用)
    @GetMapping("/customerService/cancelConnect")
    @ResponseBody
    public Result cancelConnectCustomer(){
        String originId=null;
        String originName=null;
        String targetId=null;
        String value=null;
        //判断当前用户的角色是普通用户还是客服用户
        //将用户移除客服的映射
        User user = commonService.getThisUserId();//客服用户
        if ("customer".equals(user.getRoleId())){
            //客服主动结束连接
            value="客服已结束连接!";
        }
        else {
            //用户主动结束连接
            value="用户已主动结束连接!";
        }
        String cancelUserId = chatWebSocket.kfCancelConnectUser(user.getGuid(),user.getRoleId());
        //推送给用户
        Message message=new Message();
        message.setOriginId(user.getGuid());
        message.setOriginName(user.getUserName());
        message.setCategory(4);//拒绝连接消息
        message.setValue(value);
        message.setTargetId(cancelUserId);
        Gson gson=new Gson();
        chatWebSocket.sendOneMessage(cancelUserId,gson.toJson(message));
        return Result.success("ok");
    }
    //客服确认连接(客服角色用户使用)
    @GetMapping("/customerService/kfConfirmConnection")
    @ResponseBody
    public Result kfConfirmConnection(){
        //获取当前客服角色
        User user = commonService.getThisUserId();
        //响应消息给用户
        Message message=new Message();
        message.setOriginId(user.getGuid());
        message.setOriginName(user.getUserName());
        message.setCategory(5);//客服确认连接消息
        //响应消息给用户,获取当前建立连接的客服用户
        ConcurrentHashMap<String, String> kfAndUserMap = ChatWebSocket.getKfAndUserMap();
        String targetUserId = kfAndUserMap.get(user.getGuid());
        message.setTargetId(targetUserId);
        Gson gson=new Gson();
        chatWebSocket.sendOneMessage(targetUserId,gson.toJson(message));
        return Result.success("ok");
    }
    //保存全体消息
    @PostMapping("/saveAllUserMsg")
    @ResponseBody
    public Result saveAllUserMsg(@RequestBody Message message){
        Result re=null;
        //设置类型为全体消息
        message.setType(2);//全体消息
        message.setCategory(1);//系统消息
        message.setOriginId("system");
        message.setOriginName("system");
        //保存消息
        int i = messageService.saveMessage(message);
        if (i>0){
            re=Result.success("操作成功","ok");
        }
        else {
            re=Result.failed("失败","保存失败");
        }
        return re;
    }
    //设置消息为公告
    @PutMapping("/setToNotice/{guid}")
    @ResponseBody
    public Result setToNotice(@PathVariable("guid") String guid){
        messageService.setToNotice(guid);
        return Result.success("ok");
    }
    //设置当前登录用户的所有系统消息为已读
    @PutMapping("/setThisUserSystemMessageRead")
    @ResponseBody
    public Result setThisUserSystemMessageRead(){
        messageService.setThisUserSystemMessageRead();
        return Result.success("ok");
    }
    //获取当前用户离线消息数量
    @GetMapping("/getThisUserChatMessageCount")
    @ResponseBody
    public Result getThisUserChatMessageCount(){
        int count = messageService.getThisUserChatMessageCount();
        return Result.success(count);
    }
}
