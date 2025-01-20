package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = -6829794470751667710L;
    private String guid;
    private String userName;
    private String password;
    private String imgPath;
    private  String  mobilePhone;
    private String email;
    private Integer gender;
    private Integer age;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;
    private  Integer status;//是否可用
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public User() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "User{" +
                "guid='" + guid + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", status=" + status +
                ", roleId='" + roleId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(guid, user.guid) &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(imgPath, user.imgPath) &&
                Objects.equals(mobilePhone, user.mobilePhone) &&
                Objects.equals(email, user.email) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(age, user.age) &&
                Objects.equals(createDate, user.createDate) &&
                Objects.equals(createUser, user.createUser) &&
                Objects.equals(updateDate, user.updateDate) &&
                Objects.equals(updateUser, user.updateUser) &&
                Objects.equals(status, user.status) &&
                Objects.equals(roleId, user.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, userName, password, imgPath, mobilePhone, email, gender, age, createDate, createUser, updateDate, updateUser, status, roleId);
    }
}
