package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private UserService userService;
    @Resource
    private CommonService commonService;
    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }
    @GetMapping("/register")
    public String register(){
        return "auth/register";
    }
    //处理登录请求
    @PostMapping("/processLogin")
    public String processLogin(HttpServletResponse response,HttpSession session,@RequestParam Map<String,String> map){
        User user=new User();
        String userName = map.get("userName");
        String password=map.get("password");
        String remember=map.get("remember");//前台是否需要记住用户名
        user.setUserName(userName);
        user.setPassword(password);
        User userByCode = userService.getUserByCode(user.getUserName());
        if (userByCode==null||!userByCode.getPassword().equals(password)){
            return "auth/login";
        }
        session.setAttribute("user",userByCode);
        if ("on".equals(remember)){
            // 创建一个名为 "myCookie" 的新 cookie
            Cookie cookie = new Cookie("table_sys_user_name", userName);
            // 设置 cookie 的路径，这里可以根据需要进行配置
//            cookie.setPath("/user/login");
            // 可选：设置 cookie 的有效期，单位是秒，这里设置为 7天
            cookie.setMaxAge(3600*24*7);
            // 添加 cookie 到响应中
            response.addCookie(cookie);
        }
        logger.info("["+userName+"]用户登录成功!");
        return "test/testPage";
    }

    //处理注册请求
    @PostMapping("/processRegister")
    public String processRegister( @RequestParam Map<String,String> map,HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //获取用户信息
        User user=new User();
        String userName = map.get("userName");
        String password=map.get("password");
        user.setUserName(userName);
        user.setPassword(password);
        user.setRoleId("normal");
        int i = userService.savaUser(user);
        if (i>0){
            session.setAttribute("user",user);
            //获取服务器url
            String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
            //重定向到主页
            response.sendRedirect(baseUrl+"/dashboard?pageNo=1");
            commonService.sendMsg(user.getGuid(),"登录成功!");
        }
        //请求转发到注册页
        return "auth/register";
    }

    @GetMapping("/manager")
    public String manager(){
        return "user/userManager";
    }
    //返回当前登录用户信息
    @GetMapping("/info/")
    @ResponseBody
    public Result getUserInfo(HttpSession session, HttpServletRequest request){
        ServletContext servletContext = request.getServletContext();
        String baseUrl = (String) servletContext.getAttribute("baseUrl");
        String imgUrl = (String) servletContext.getAttribute("img_url");
        Result result=null;
        //获取当前session中用户的guid
        User  user = (User) session.getAttribute("user");
        //查询用户详细信息
        User nowUser = userService.getUserById(user.getGuid());
        //密码置空
        nowUser.setPassword("");
        result=  Result.success(nowUser);
        //补充基础数据
        result.setImgUrl(imgUrl);
        result.setBaseUrl(baseUrl);
        //响应
        return result;
    }
    //更新用户信息
    @PutMapping("/info/")
    @ResponseBody
    public Result updateUserInfo(@RequestBody User user,HttpSession session){
        boolean b = userService.updateUser(user);
        //如果更新成功，将用户信息重新载入Session中
        if (b){
            User nowUser = userService.getUserById(user.getGuid());
            nowUser.setPassword(null);
            session.setAttribute("user",nowUser);
            logger.info("用户信息修改成功，重新载入信息到session完成");
        }
        return Result.success(b,"修改成功!");
    }
    //返回userId
    @GetMapping("/getUserId")
    @ResponseBody
    public Result getUserId(HttpSession session){
        User user = (User) session.getAttribute("user");
        return  Result.success(user.getGuid(),"ok");
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        //退出登录
        session.setAttribute("user",null);
        return "redirect:/user/processLogin";
    }
    //校验用户是否允许注册,用于预处理注册时的校验
    @PostMapping("/verifyCanRegister")
    @ResponseBody
    public Result verifyCanRegister(@RequestBody User user){
        Result result=null;
        try {
            boolean site = userService.verifyCanRegister(user);

            if (site){
                result=Result.success("ok","校验通过");
            }
            else {
                result=Result.failed("失败","信息不合法!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result=Result.failed("失败",e.getMessage());
        }
        finally {
            return result;
        }

    }
    //获取用户的头像信息 用户id:头像url
    @PostMapping("/getUserImgPath")
    @ResponseBody
    public Result getUserImgPath(@RequestBody JsonNode jsonData){
        Result result=null;
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> idList = objectMapper.convertValue(jsonData.get("ids"), List.class);
        //更新状态信息
        if (idList!=null&&idList.size()>0){
            Map userImgMap = userService.getUserImg(idList);
            result=Result.success(userImgMap,"获取头像数据成功");
        }
        else {
            result=Result.failed("失败","传入的id为空!");
        }
        return result;
    }

}
