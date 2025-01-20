package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.MenuNode;
import com.atsu.tabletennisreservation.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuService {
    //查询菜单
    List<Menu> getMenuList(Menu menu);
    //获取菜单树列表
    List<MenuNode> getMenuNodeList();
    //保存菜单
    int  saveMenu( Menu menu);
    //修改菜单
    int updateMenuById( Menu menu);
    //根据id查询菜单详细信息
    Menu getMenuById(String guid);
    List<Menu> getMenuCodeLike(String guid);
    int deleteMenu( Menu menu);
    //获取菜单树列表
    List<Menu> getMenuTree();
    //获取当前登录用户的菜单信息
    List<Menu> getThisUserMenuTreeList();
    //获取所有菜单的url集合
    List<String> getAllMenuUrl();
    //将角色和授权的菜单的基础数据加载到内存中

}
