package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

//球桌表
public class BallTable implements Serializable {
    private static final long serialVersionUID = -2829294470751667711L;
    private String guid;
    private String imgPath;
    private String code;//球桌编号
    private String address;
    private String description;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;
    private Integer status;//是否可用 1 可用 2 不可用
    private String brand;//球桌品牌
    private String floorMaterial;//地面材质
    private String qualityRating;//完好程度
    private String startTime;//当天开放时间
    private String endTime;//当天结束时间
    private BigDecimal price;

    public BallTable(String guid, String code, Integer status) {
        this.guid = guid;
        this.code = code;
        this.status = status;
    }

    public BallTable() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFloorMaterial() {
        return floorMaterial;
    }

    public void setFloorMaterial(String floorMaterial) {
        this.floorMaterial = floorMaterial;
    }

    public String getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(String qualityRating) {
        this.qualityRating = qualityRating;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BallTable ballTable = (BallTable) o;
        return Objects.equals(guid, ballTable.guid) &&
                Objects.equals(imgPath, ballTable.imgPath) &&
                Objects.equals(code, ballTable.code) &&
                Objects.equals(address, ballTable.address) &&
                Objects.equals(description, ballTable.description) &&
                Objects.equals(createDate, ballTable.createDate) &&
                Objects.equals(createUser, ballTable.createUser) &&
                Objects.equals(updateDate, ballTable.updateDate) &&
                Objects.equals(updateUser, ballTable.updateUser) &&
                Objects.equals(status, ballTable.status) &&
                Objects.equals(brand, ballTable.brand) &&
                Objects.equals(floorMaterial, ballTable.floorMaterial) &&
                Objects.equals(qualityRating, ballTable.qualityRating) &&
                Objects.equals(startTime, ballTable.startTime) &&
                Objects.equals(endTime, ballTable.endTime) &&
                Objects.equals(price, ballTable.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, imgPath, code, address, description, createDate, createUser, updateDate, updateUser, status, brand, floorMaterial, qualityRating, startTime, endTime, price);
    }

    @Override
    public String toString() {
        return "BallTable{" +
                "guid='" + guid + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", code='" + code + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", status=" + status +
                ", brand='" + brand + '\'' +
                ", floorMaterial='" + floorMaterial + '\'' +
                ", qualityRating='" + qualityRating + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", price=" + price +
                '}';
    }
}
