package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.RoleMenuDto;
import com.atsu.tabletennisreservation.pojo.RoleMenu;

import java.util.List;

public interface RoleMenuService {
    int saveRoleMenu( RoleMenu roleMenu);
    int saveRoleMenu( RoleMenuDto roleMenuDto);
    List<RoleMenu> getRoleMenu( RoleMenu roleMenu);
    int deleteRoleMenu( RoleMenu roleMenu);
    int saveRoleMenuBatch( List<RoleMenu> roleMenuList);
    //返回当前用户角色的角色菜单对象
    RoleMenuDto getThisUserRoleMenuDto();
    RoleMenuDto getRoleMenuDto(String roleId);
    //返回所有的角色菜单数据
    List<RoleMenuDto>  getAllRoleMenuDto();
}
