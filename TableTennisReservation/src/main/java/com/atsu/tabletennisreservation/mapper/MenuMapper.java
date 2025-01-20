package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    //查询菜单
    List<Menu> getMenuList(@Param("menu") Menu menu);
    //保存菜单
    int  saveMenu(@Param("menu") Menu menu);
    //修改菜单
    int updateMenuById(@Param("menu") Menu menu);
    //根据编码模糊查询出所有子级
    List<Menu> getMenuCodeLike(@Param("guid") String guid);
    //删除菜单
    int deleteMenu(@Param("menu") Menu menu);
    //根据角色查询有对应权限的菜单数据
    List<Menu> getRoleMenuList(@Param("roleId") String roleId);
}
