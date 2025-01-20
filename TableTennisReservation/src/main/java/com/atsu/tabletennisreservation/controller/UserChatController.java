package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.Message;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MessageService;
import com.atsu.tabletennisreservation.service.UserChatService;
import com.atsu.tabletennisreservation.service.UserService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.webSocket.UserChatWebSocket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//用户之间通信
@Controller
@RequestMapping("/userChat")
public class UserChatController {
    @Resource
    private UserChatService userChatService;
    //发送消息给指定用户,并返回补全信息后的消息
    @PostMapping("/msg")
    @ResponseBody
    public Result sendMsgToUser(@RequestBody Message message){
        Result re=null;
        boolean site = false;
        try {
            Message reMsg = userChatService.sendMsgToUser(message);
            re=Result.success(reMsg);
        } catch (Exception e) {
            re=Result.failed("推送失败",e.getMessage());
        }
        finally {
            return re;
        }
    }
    @GetMapping("/")
    public String chatIndex(Model model, @RequestParam( name = "targetId",required = false) String targetId,
                            @RequestParam( name = "targetName",required = false)   String targetName){
        if (!StringTool.isNull(targetId)){
            model.addAttribute("select_target_id",targetId);
        }
        if (!StringTool.isNull(targetName)){
            model.addAttribute("select_target_name",targetName);
        }
        return "chat/userChat";
    }
    //返回当前用户的所有离线消息
    @GetMapping("/msg")
    @ResponseBody
    public Result getThisUserOfflineMessage(){
        Map<String, List<Message>> map = userChatService.getThisUserOfflineMessage();
        return Result.success(map);
    }
    //删除当前用户的已经同步了的离线消息,消息id由前端回传
    @DeleteMapping("/msg")
    @ResponseBody
    public Result deleteThisUserOfflineMessage(@RequestBody JsonNode jsonData){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> idList = objectMapper.convertValue(jsonData.get("ids"), List.class);
        boolean site=true;
        if (idList.size()>0){
             site = userChatService.deleteThisUserOfflineMessage(idList);
        }
        if (site){
            return Result.success("","消息删除完成!");
        }
        else {
            return Result.failed("");
        }

    }
    //返回当前用户收到的离线消息数量
    @GetMapping("/offLineCount")
    @ResponseBody
    public Result  getThisUserOffLineMesCount(){
        Integer count = userChatService.getThisUserOffLineMesCount();
        return Result.success(count);
    }
    @GetMapping("/notRead/{targetId}")
    @ResponseBody
    //返回当前用户对目标用户未读的消息ids
    public Result  getThisUserSendOffLineMsgIds(@PathVariable("targetId") String targetId){
        List<String> ids = userChatService.getThisUserSendOffLineMsgIds(targetId);
        return Result.success(ids);
    }
}
