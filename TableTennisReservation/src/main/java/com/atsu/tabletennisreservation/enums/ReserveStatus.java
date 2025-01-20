package com.atsu.tabletennisreservation.enums;

import com.atsu.tabletennisreservation.pojo.Reserve;

//预订状态枚举类
public enum ReserveStatus {
    //预订状态枚举
    RESERVE_FAILED(-3,"预订失败"),
    RESERVE_DUE(0,"预订成功，待付款"),
    RESERVE_MATCHING(1,"待匹配球友拼桌"),
    RESERVE_SUCCESS(3,"付款成功，待使用"),
    RESERVE_USED(4,"已使用"),
    RESERVE_CANCEL(6,"预订取消"),
    RESERVE_EXPIRED(9,"预订过期");
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

     ReserveStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    //通过编码，返回对应的枚举类
    //通过编码码获取对应的枚举类对象,否则返回空
    public static ReserveStatus getReserveStatus(int code){
        ReserveStatus[] values = values();
        for (ReserveStatus value : values) {
            if (value.getCode()==code)
                return value;
        }
        return null;
    }
    //根据传入的状态和预订单，生成消息文本信息
    public static String getReserveStatusMessage(int code, Reserve reserve){
        ReserveStatus reserveStatus = getReserveStatus(code);
        StringBuffer text=new StringBuffer();
        if (reserveStatus!=null){
            text.append("[").append(reserveStatus.getMsg()).append("] ");

        }
        return "";
    }
}
