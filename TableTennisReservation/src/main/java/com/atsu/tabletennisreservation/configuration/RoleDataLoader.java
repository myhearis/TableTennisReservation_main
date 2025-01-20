package com.atsu.tabletennisreservation.configuration;

import com.atsu.tabletennisreservation.pojo.Menu;
import com.atsu.tabletennisreservation.pojo.Role;
import com.atsu.tabletennisreservation.service.MenuService;
import com.atsu.tabletennisreservation.service.RoleService;
import com.atsu.tabletennisreservation.utils.StringTool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Logger;

//角色菜单信息加载到内存中
@Component
public class RoleDataLoader implements CommandLineRunner {
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    private Logger logger=Logger.getLogger(this.getClass().getName());
    private Map<String,Set<String>> roleMenuMapping;//角色和菜单url的映射
    private Set<String> allMenuUrlSet;//当前所有菜单的集合
    public Map<String, Set<String>> getRoleMenuMapping() {
        return roleMenuMapping;
    }

    public void setRoleMenuMapping(Map<String, Set<String>> roleMenuMapping) {
        this.roleMenuMapping = roleMenuMapping;
    }

    @Override
    public void run(String... args) throws Exception {
        refresh();
    }
    //重新加载角色菜单信息加载到内存
    public void refresh(){
        Map<String, Set<String>> map=new HashMap<>();
        Set<String> set=new HashSet<>();
        List<Role> roleList = roleService.getRoleList(new Role());
        //构造角色菜单映射
        for (int i=0;i<roleList.size();i++){
            Role role=roleList.get(i);
            List<Menu> menuList = role.getMenuList();
            //构造url  set
            Set<String> urlSet=new HashSet<>();
            map.put(role.getGuid(),urlSet);
            for (int k=0;k<menuList.size();k++){
                Menu menu = menuList.get(k);
                if (!StringTool.isNull(menu.getUrl())){
                    urlSet.add(menu.getUrl());
                }
            }
        }
        //构造当前角色的所有菜单数据
        List<String> allMenuUrl = menuService.getAllMenuUrl();
        set=new HashSet<>(allMenuUrl);
        this.allMenuUrlSet=set;
        this.roleMenuMapping=map;
        logger.info("重新加载角色菜单信息加载到内存...");
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public Set<String> getAllMenuUrlSet() {
        return allMenuUrlSet;
    }

    public void setAllMenuUrlSet(Set<String> allMenuUrlSet) {
        this.allMenuUrlSet = allMenuUrlSet;
    }
}
