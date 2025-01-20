package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.util.Objects;

//角色菜单项
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = -6521734170451667710L;
    private  String guid;
    private  String roleId;
    private  String menuId;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
                "guid='" + guid + '\'' +
                ", roleId='" + roleId + '\'' +
                ", menuId='" + menuId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMenu roleMenu = (RoleMenu) o;
        return Objects.equals(guid, roleMenu.guid) &&
                Objects.equals(roleId, roleMenu.roleId) &&
                Objects.equals(menuId, roleMenu.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, roleId, menuId);
    }
}
