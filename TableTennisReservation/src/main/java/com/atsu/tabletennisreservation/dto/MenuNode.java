package com.atsu.tabletennisreservation.dto;

import com.atsu.tabletennisreservation.pojo.Menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuNode implements Serializable {
    private static final long serialVersionUID = -2261294474751667711L;
    private String id;
    private String data;
    private List<MenuNode> children;
    private String parentId;
    private Integer sort;

    public MenuNode() {
    }
    public MenuNode(Menu menu) {
        //根据menu初始化信息
        this.id = menu.getGuid();
        this.data=menu.getName();
        this.parentId= menu.getParentId();
        this.sort= menu.getSort();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuNode{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                ", children=" + children +
                ", parentId='" + parentId + '\'' +
                ", sort=" + sort +
                '}';
    }
}
