package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface RoleMapper {
    //添加角色
    int saveRole(@Param("role") Role role);
    //查询角色
    List<Role> getRoleList(@Param("role") Role role);
    //删除角色
    int deleteRole(@Param("role") Role role);
    //修改角色
    int updateRoleById(@Param("role") Role role);
}
