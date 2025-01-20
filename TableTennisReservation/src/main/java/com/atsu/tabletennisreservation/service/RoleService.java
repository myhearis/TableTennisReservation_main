package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.pojo.Role;

import java.util.List;

public interface RoleService {
    //添加角色
    int saveRole( Role role);
    //查询角色
    List<Role> getRoleList( Role role);
    //删除角色
    int deleteRole( Role role);
    //修改角色
    int updateRoleById( Role role);
    //校验指定角色是否有访问该菜单的权限
    boolean verifyRoleMenu(String roleId,String url);
    //校验当前用户是否有访问该菜单的权限
    boolean verifyUserRoleMenu(String url);
}
