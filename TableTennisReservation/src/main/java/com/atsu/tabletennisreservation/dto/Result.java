package com.atsu.tabletennisreservation.dto;

import java.io.Serializable;

//响应给前端的统一数据
public class Result implements Serializable {
    private static final long serialVersionUID = -2829294210751667711L;
    private boolean success;
    private String msg;
    private Object data;
    //包装基础数据
    private String baseUrl;
    private String imgUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Result success(Object data){
        Result result = new Result();
        result.setData(data);
        result.setSuccess(true);
        return result;
    }
    public static Result success(Object data,String msg){
        Result result = new Result();
        result.setData(data);
        result.setSuccess(true);
        result.setMsg(msg);
        return result;
    }
    public static Result failed(Object data){
        Result result = new Result();
        result.setData(data);
        result.setSuccess(false);
        return result;
    }
    public static Result failed(Object data,String msg){
        Result result = new Result();
        result.setData(data);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }
}
