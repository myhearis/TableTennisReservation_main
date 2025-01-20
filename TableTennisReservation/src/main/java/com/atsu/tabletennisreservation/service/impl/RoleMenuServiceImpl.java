package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.configuration.RoleDataLoader;
import com.atsu.tabletennisreservation.dto.RoleMenuDto;
import com.atsu.tabletennisreservation.mapper.RoleMenuMapper;
import com.atsu.tabletennisreservation.pojo.Role;
import com.atsu.tabletennisreservation.pojo.RoleMenu;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.RoleMenuService;
import com.atsu.tabletennisreservation.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleMenuServiceImpl implements RoleMenuService {
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleDataLoader roleDataLoader;
    @Override
    public int saveRoleMenu(RoleMenu roleMenu) {
        //生成id
        roleMenu.setGuid(commonService.getId());
        return roleMenuMapper.saveRoleMenu(roleMenu);
    }

    @Override
    public int saveRoleMenu(RoleMenuDto roleMenuDto) {
        //先删除后插入
        RoleMenu delete_condition=new RoleMenu();
        delete_condition.setRoleId(roleMenuDto.getRoleId());
        roleMenuMapper.deleteRoleMenu(delete_condition);
        //插入
        //构造插入数据
        List<String> nodeIds = roleMenuDto.getNodeIds();
        List<RoleMenu> roleMenuList=new ArrayList<>();
        for (int i=0;i<nodeIds.size();i++){
            RoleMenu roleMenu=new RoleMenu();
            roleMenu.setGuid(commonService.getId());
            roleMenu.setRoleId(roleMenuDto.getRoleId());
            roleMenu.setMenuId(nodeIds.get(i));
            roleMenuList.add(roleMenu);
        }
        int i = roleMenuMapper.saveRoleMenuBatch(roleMenuList);
        //批量插入数据
        return i;
    }

    @Override
    public List<RoleMenu> getRoleMenu(RoleMenu roleMenu) {
        return roleMenuMapper.getRoleMenu(roleMenu);
    }

    @Override
    public int deleteRoleMenu(RoleMenu roleMenu) {
        return roleMenuMapper.deleteRoleMenu(roleMenu);
    }

    @Override
    public int saveRoleMenuBatch(List<RoleMenu> roleMenuList) {
        //批量生成id
        for (int i=0;i<roleMenuList.size();i++){
            roleMenuList.get(i).setGuid(commonService.getId());
        }
        return roleMenuMapper.saveRoleMenuBatch(roleMenuList);
    }

    @Override
    public RoleMenuDto getThisUserRoleMenuDto() {
        //获取当前用户角色
        User user = commonService.getThisUserId();
        String roleId = user.getRoleId();
        //查询角色菜单
        RoleMenu roleMenu=new RoleMenu();
        roleMenu.setRoleId(roleId);
        List<RoleMenu> list = roleMenuMapper.getRoleMenu(roleMenu);
        //封装成dto
        RoleMenuDto roleMenuDto=new RoleMenuDto();
        roleMenuDto.setRoleId(roleId);
        List<String> nodeIds=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            nodeIds.add(list.get(i).getMenuId());
        }
        roleMenuDto.setNodeIds(nodeIds);
        return roleMenuDto;
    }

    @Override
    public RoleMenuDto getRoleMenuDto(String roleId) {
        //查询角色菜单
        RoleMenu roleMenu=new RoleMenu();
        roleMenu.setRoleId(roleId);
        List<RoleMenu> list = roleMenuMapper.getRoleMenu(roleMenu);
        //封装成dto
        RoleMenuDto roleMenuDto=new RoleMenuDto();
        roleMenuDto.setRoleId(roleId);
        List<String> nodeIds=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            nodeIds.add(list.get(i).getMenuId());
        }
        roleMenuDto.setNodeIds(nodeIds);
        return roleMenuDto;
    }

    @Override
    public List<RoleMenuDto> getAllRoleMenuDto() {
        //获取所有角色
        List<Role> roleList = roleService.getRoleList(new Role());
        for (int i=0;i<roleList.size();i++){

        }
        return null;
    }
}
