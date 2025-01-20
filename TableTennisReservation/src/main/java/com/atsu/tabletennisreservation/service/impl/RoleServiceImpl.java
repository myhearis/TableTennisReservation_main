package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.configuration.RoleDataLoader;
import com.atsu.tabletennisreservation.mapper.RoleMapper;
import com.atsu.tabletennisreservation.pojo.Role;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleDataLoader roleDataLoader;
    @Resource
    private CommonService commonService;
    @Override
    public int saveRole(Role role) {
        return roleMapper.saveRole(role);
    }

    @Override
    public List<Role> getRoleList(Role role) {
        return roleMapper.getRoleList(role);
    }

    @Override
    public int deleteRole(Role role) {
        return roleMapper.deleteRole(role);
    }

    @Override
    public int updateRoleById(Role role) {
        return roleMapper.updateRoleById(role);
    }

    @Override
    public boolean verifyRoleMenu(String roleId,String url) {
        //获取内存中的映射关系
        Map<String, Set<String>> roleMenuMapping = roleDataLoader.getRoleMenuMapping();
        Set<String> urlSet = roleMenuMapping.get(roleId);
        //判断该url是否存在于集合中
        if (urlSet!=null&&urlSet.contains(url)){
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUserRoleMenu(String url) {
        //获取当前用户角色id
        User user = commonService.getThisUserId();
        return verifyRoleMenu(user.getRoleId(), url);
    }
}
