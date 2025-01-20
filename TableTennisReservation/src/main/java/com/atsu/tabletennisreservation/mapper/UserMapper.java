package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User getUserById(@Param("userId") String userId);

    int savaUser(@Param("user") User user);

    List<User> getUserList();

    User getUserByCode(@Param("code") String code);

    int deleteUserById(@Param("userId") String userId);

    int deleteUserByCode(@Param("code") String code);

    int updateUserById(@Param("user") User user);

    User getUserByIdAndStatus(@Param("userId") String userId,@Param("status") int status);
    //判断用户名是否存在
    int userCodeIsExist(@Param("userCode") String userCode);
    //根据条件查询用户
    List<User> getConditionUserCList(@Param("user") User user);
    //批量查询用户
    List<User> getUserBatch(@Param("ids") List<String> ids);
}
