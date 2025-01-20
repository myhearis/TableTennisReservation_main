package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper {
    int saveRoleMenu(@Param("roleMenu") RoleMenu roleMenu);
    List<RoleMenu> getRoleMenu(@Param("roleMenu") RoleMenu roleMenu);
    int deleteRoleMenu(@Param("roleMenu") RoleMenu roleMenu);
    int saveRoleMenuBatch(@Param("roleMenuList") List<RoleMenu> roleMenuList);
}
