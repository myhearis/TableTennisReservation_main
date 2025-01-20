package com.atsu.tabletennisreservation.exception;

public class ReserveException extends Exception{
    private String  code;//1:候补 3：校验失败   5:其他
    private String msg;
    public ReserveException(String code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ReserveException() {
    }
}
