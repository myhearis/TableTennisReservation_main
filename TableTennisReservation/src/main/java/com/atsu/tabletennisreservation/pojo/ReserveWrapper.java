package com.atsu.tabletennisreservation.pojo;

import com.atsu.tabletennisreservation.enums.ReserveStatus;

public class ReserveWrapper extends Reserve{
    private String statusName;//当前状态

    public ReserveWrapper() {
    }
    public ReserveWrapper(Reserve reserve){
        Integer reserveStatus = reserve.getReserveStatus();
        ReserveStatus status = ReserveStatus.getReserveStatus(reserveStatus);
        String msg = status.getMsg();
        this.statusName=msg;
        //初始化基本信息

    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
