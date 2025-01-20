package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;

public class Message  implements Serializable {
    private static final long serialVersionUID = -2389294470752667712L;
    private String guid;
    private String originId;
    private String originName;
    private String targetId;
    private String targetName;
    private Integer type;
    private Integer category;
    private String value;
    private String createTime;
    private Integer isRead;
    private String processCreateDate;//计算列，只有查询时有效，录入时不需要赋值

    public String getProcessCreateDate() {
        return processCreateDate;
    }

    public void setProcessCreateDate(String processCreateDate) {
        this.processCreateDate = processCreateDate;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "guid='" + guid + '\'' +
                ", originId='" + originId + '\'' +
                ", originName='" + originName + '\'' +
                ", targetId='" + targetId + '\'' +
                ", targetName='" + targetName + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", value='" + value + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isRead=" + isRead +
                ", processCreateDate='" + processCreateDate + '\'' +
                '}';
    }
}
