package com.atsu.tabletennisreservation.pojo;

import com.atsu.tabletennisreservation.dto.MenuNode;

import java.io.Serializable;
import java.util.List;

//菜单实体
public class Menu implements Serializable {
    private static final long serialVersionUID = -2589294470752667712L;
    private String guid;//GUID
    private String parentId; //PARENT_ID
    private Integer isLeaf; //IS_LEAF
    private String name; //NAME
    private  String code; //CODE
    private  Integer sort; //SORT
    private  Integer type; //TYPE  类型 1: headr  菜单头 2: item 菜单项
    private  String url;//URL
    private List<Menu> children;

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "guid='" + guid + '\'' +
                ", parentId='" + parentId + '\'' +
                ", isLeaf='" + isLeaf + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sort=" + sort +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
