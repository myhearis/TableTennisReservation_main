package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.exception.SysException;
import com.atsu.tabletennisreservation.mapper.UserMapper;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.RoleService;
import com.atsu.tabletennisreservation.service.UserService;
import com.atsu.tabletennisreservation.utils.StringTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private RoleService roleService;
    @Override
    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public int savaUser(User user) {
        String nowDateStr = commonService.getNowDateStr();
        String id = commonService.getId();
        user.setGuid(id);
        user.setStatus(1);//设置状态
        user.setImgPath("admin.png");//默认头像路径
        user.setUpdateDate(nowDateStr);
        user.setCreateDate(nowDateStr);
        return  userMapper.savaUser(user);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public User getUserByCode(String code) {
        return userMapper.getUserByCode(code);
    }

    @Override
    public int deleteUserById(String userId) {
        return userMapper.deleteUserById(userId);
    }

    @Override
    public int deleteUserByCode(String code) {
        return userMapper.deleteUserByCode(code);
    }

    @Override
    public boolean registerUser(User user) throws Exception {
        //校验信息
        boolean b = verifyUser(user);
        //保存信息
        int i = userMapper.savaUser(user);
        if (i>0)
            return true;
        return false;
    }

    @Override
    public boolean userCodeIsExist(String userCode) {
        int i = userMapper.userCodeIsExist(userCode);
        if (i>0)
            return true;
        return false;
    }

    @Override
    public boolean verifyUser(User user) throws Exception {
        if (user.getUserName()==null||user.getPassword()==null){
            throw new Exception("用户名或密码为空!");
        }
        //校验用户名是否已经存在
        if (userCodeIsExist(user.getUserName())){
            throw new Exception("该用户名已存在!");
        }
        //正则校验
        //校验密码是否合法
        //校验用户名是否合法
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        return  userMapper.updateUserById(user)>0;
    }

    @Override
    public User getOnlineCustomerUser() {
        //获取对应角色的用户
        User user=new User();
        user.setRoleId("customer");
        //查询
        List<User> list = userMapper.getConditionUserCList(user);
        //取第一个在线的用户
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<User> getConditionUserCList(User user) {
        return userMapper.getConditionUserCList(user);
    }
    //获取客服角色用户
    @Override
    public List<User> getKfUserList() {
        //获取对应角色的用户
        User user=new User();
        user.setRoleId("customer");
        //查询
        List<User> list = userMapper.getConditionUserCList(user);
        return list;
    }

    @Override
    public String getUserEmail(String userId) {
        User user = userMapper.getUserById(userId);
        return user.getEmail();
    }
    //校验用户是否允许注册
    @Override
    public boolean verifyCanRegister(User user) throws Exception {
        if (StringTool.isNull(user.getUserName())){
            throw new Exception("用户名为空!");
        }
        if (StringTool.isNull(user.getPassword())){
            throw new Exception("密码为空!");
        }
        //用户名不重复
        User condition = new User();
        condition.setUserName(user.getUserName());
        List<User> list = userMapper.getConditionUserCList(condition);
        if (list!=null&&list.size()>0){
            throw new Exception("该用户名已经存在!");
        }
        //用户名是否规范
        String nameRegex = "^[\\u4e00-\\u9fa5a-zA-Z]{1,20}$";//长度不能超过20个字符，且内容为中文字符或者字母
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(user.getUserName());
        if (!nameMatcher.matches()) {
            throw new Exception("用户名不合法！长度不能超过20个字符，且内容为中文字符或者字母");
        }
        //密码是否规范
        String passwordRegex = "^[a-zA-Z0-9]{6,14}$";//密码：长度在6-14之间，且为数字或字母
        Pattern passwordPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
        if (!passwordMatcher.matches()) {
            throw new Exception("密码不合法！长度应在6-14之间，且为数字或字母");
        }
        return true;
    }
    //获取用户头像信息
    @Override
    public Map getUserImg(List<String> ids) {
        Map<String,String> map=new HashMap();
        //获取对应的用户信息
        List<User> userList = userMapper.getUserBatch(ids);
        for (int i=0;i<userList.size();i++){
            User user=userList.get(i);
            map.put(user.getGuid(),user.getImgPath());//构造用户：头像url映射
        }
        return map;
    }
}
