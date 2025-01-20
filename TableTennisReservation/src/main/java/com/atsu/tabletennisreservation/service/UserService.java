package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserService {
    User getUserById( String userId);

    int savaUser( User user);

    List<User> getUserList();

    User getUserByCode( String code);

    int deleteUserById( String userId);

    int deleteUserByCode( String code);

    //处理注册用户请求
    boolean registerUser(User user) throws Exception;
    //判断用户名是否存在
    boolean userCodeIsExist(String userCode);
    //校验注册用户信息
    boolean verifyUser(User user) throws Exception;
    //更新用户信息
    boolean updateUser(User user);
    //获取当前空闲的客服（没有占线且在线的）
    User getOnlineCustomerUser();
    //根据条件查询用户
    List<User> getConditionUserCList( User user);
    //获取客服角色用户
    List<User> getKfUserList();
    //获取用户的邮箱
    String getUserEmail(String userId);
    boolean verifyCanRegister(User user) throws Exception;
    //获取用户头像信息
    Map getUserImg(List<String> ids);
}
