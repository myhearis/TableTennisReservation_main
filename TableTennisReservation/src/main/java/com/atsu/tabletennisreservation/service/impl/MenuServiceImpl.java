package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.configuration.RoleDataLoader;
import com.atsu.tabletennisreservation.dto.MenuNode;
import com.atsu.tabletennisreservation.mapper.MenuMapper;
import com.atsu.tabletennisreservation.pojo.Menu;
import com.atsu.tabletennisreservation.pojo.MenuTreeBuilder;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MenuService;
import com.atsu.tabletennisreservation.utils.StringTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private RoleDataLoader roleDataLoader;
    @Override
    public List<Menu> getMenuList(Menu menu) {
        return menuMapper.getMenuList(menu);
    }

    @Override
    public List<MenuNode> getMenuNodeList() {
        //获取菜单信息
        Menu menu=new Menu();
        List<Menu> menuList = menuMapper.getMenuList(menu);
        List<MenuNode> nodeList = parseMenuList(menuList);
        return nodeList;
    }

    @Override
    public int saveMenu(Menu menu) {
        int i = menuMapper.saveMenu(menu);
        return i;
    }

    @Override
    public int updateMenuById(Menu menu) {
        int i = menuMapper.updateMenuById(menu);
        return i;
    }

    @Override
    public Menu getMenuById(String guid) {
        Menu menu=new Menu();
        menu.setGuid(guid);
        List<Menu> menuList = menuMapper.getMenuList(menu);
        if (menuList!=null&&menuList.size()==1){
            return  menuList.get(0);
        }
        return null;
    }

    @Override
    public List<Menu> getMenuCodeLike(String guid) {
        return menuMapper.getMenuCodeLike(guid);
    }

    @Override
    public int deleteMenu(Menu menu) {
        int i = menuMapper.deleteMenu(menu);
        return i;
    }

    @Override
    public List<Menu> getMenuTree() {
        //获取菜单信息
        Menu menu=new Menu();
        List<Menu> menuList = menuMapper.getMenuList(menu);
        MenuTreeBuilder menuTreeBuilder=new MenuTreeBuilder();
        List<Menu> list = menuTreeBuilder.buildMenuTree(menuList);
        return list;
    }

    @Override
    public List<Menu> getThisUserMenuTreeList() {
        //根据当前用户的类型，返回菜单信息
        User user = commonService.getThisUserId();
        List<Menu> menuList = menuMapper.getRoleMenuList(user.getRoleId());
        //转化为菜单树
        MenuTreeBuilder menuTreeBuilder=new MenuTreeBuilder();
        List<Menu> list = menuTreeBuilder.buildMenuTree(menuList);
        return list;
    }

    @Override
    public List<String> getAllMenuUrl() {
        List<Menu> menuList = menuMapper.getMenuList(new Menu());
        List<String> urlList=new ArrayList<>();
        for (int i=0;i<menuList.size();i++){
            Menu menu = menuList.get(i);
            //只返回有值的url
            if (!StringTool.isNull(menu.getUrl())){
                urlList.add(menu.getUrl());
            }
        }
        return urlList;
    }

    //根据菜单列表，生成菜单树
    private  List<MenuNode> parseMenuList(List<Menu> menuList){
        List<MenuNode> nodeList=new ArrayList<>();
        Iterator<Menu> iterator = menuList.iterator();
        List<String> removeIdList=new ArrayList<>();//需要移除的内容
        List<MenuNode> re=new ArrayList<>();
        //转化node
        for (int i=0;i<menuList.size();i++){
            nodeList.add(new MenuNode(menuList.get(i)));
        }
        MenuTreeBuilder menuTreeBuilder=new MenuTreeBuilder();
        re = menuTreeBuilder.buildMenuNodeTree(nodeList);
        return  re;
    }

}
