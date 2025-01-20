package com.atsu.tabletennisreservation.dto;

import java.io.Serializable;
import java.util.List;
//用于接受前台保存的数据,以及响应给前台
public class RoleMenuDto implements Serializable {
    private static final long serialVersionUID = -2839224291701667711L;
    private String roleId;//角色id
    private List<String> nodeIds;//授权的菜单id

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(List<String> nodeIds) {
        this.nodeIds = nodeIds;
    }

    @Override
    public String toString() {
        return "RoleMenuDto{" +
                "roleId='" + roleId + '\'' +
                ", nodeIds=" + nodeIds +
                '}';
    }
}
