package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.util.List;

//角色
public class Role implements Serializable {
    private static final long serialVersionUID = -6921734470751667710L;
    private String guid;
    private String name;
    private  String  remark;
    private List<Menu> menuList;//角色可访问的菜单

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Role{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", menuList=" + menuList +
                '}';
    }
}
