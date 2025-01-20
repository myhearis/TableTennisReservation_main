package com.atsu.tabletennisreservation.pojo;

import com.atsu.tabletennisreservation.dto.MenuNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuTreeBuilder {
    public List<MenuNode> buildMenuNodeTree(List<MenuNode> menuList) {
        List<MenuNode> menuTree = new ArrayList<>();
        for (MenuNode menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId().equals("")) { // 根节点判断条件，可以根据需要修改
                MenuNode menuNode = buildMenuNode(menu, menuList);
                menuTree.add(menuNode);
            }
        }
        //排序
        menuTree.sort(new Comparator<MenuNode>() {
            @Override
            public int compare(MenuNode o1, MenuNode o2) {
                if (o1.getSort()!=null&&o2.getSort()!=null){
                    return o1.getSort()-o2.getSort();
                }
                if (o1.getSort()!=null&&o2.getSort()==null){
                    return o1.getSort();
                }
                if (o1.getSort()==null&&o2.getSort()!=null){
                    return o2.getSort();
                }
                return 0;
            }
        });
        return menuTree;
    }
    private MenuNode buildMenuNode(MenuNode parentNode, List<MenuNode> menuList) {
        List<MenuNode> children = new ArrayList<>();
        for (MenuNode menu : menuList) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentNode.getId())) {
                MenuNode menuNode = buildMenuNode(menu, menuList);
                children.add(menuNode);
            }
        }
        if (children.size()==0){
            children=null;
        }
        parentNode.setChildren(children);
        return parentNode;
    }
    public List<Menu> buildMenuTree(List<Menu> menuList) {
        List<Menu> menuTree = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId().equals("")) { // 根节点判断条件，可以根据需要修改
                Menu menuNode = buildMenu(menu, menuList);
                menuTree.add(menuNode);
            }
        }
        //排序
        menuTree.sort(new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                if (o1.getSort()!=null&&o2.getSort()!=null){
                    return o1.getSort()-o2.getSort();
                }
                if (o1.getSort()!=null&&o2.getSort()==null){
                    return o1.getSort();
                }
                if (o1.getSort()==null&&o2.getSort()!=null){
                    return o2.getSort();
                }
                return 0;
            }
        });
        return menuTree;
    }

    private Menu buildMenu(Menu parentNode, List<Menu> menuList) {
        List<Menu> children = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() != null && menu.getParentId().equals(parentNode.getGuid())) {
                Menu menuNode = buildMenu(menu, menuList);
                children.add(menuNode);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }


}