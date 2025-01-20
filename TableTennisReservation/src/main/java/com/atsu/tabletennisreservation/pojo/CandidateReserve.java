package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

//候补预订单
public class CandidateReserve implements Serializable {
    private static final long serialVersionUID = -2089294470752667712L;
    private  String guid;
    private String userId;
    private String tableId;
    private String startDate;
    private Integer useTime;
    private Integer status;
    private String tableCode;
    private String processStartDate;
    private String createDate;
    private BigDecimal payAmt;//交易金额
    private String tradeNo;//支付交易号 TRADE_NO
    private String outTradeNo;//商户订单号 OUT_TRADE_NO
    private String userName;//用户名

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public String getProcessStartDate() {
        return processStartDate;
    }

    public void setProcessStartDate(String processStartDate) {
        this.processStartDate = processStartDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }
    //根据当前候补单信息，转化成预定单
    public Reserve parseReserve(){
        Reserve reserve=new Reserve();
        reserve.setTableCode(this.tableCode);
        reserve.setTableId(this.tableId);
        reserve.setStartDate(this.startDate);
        reserve.setUseTime(this.useTime);
        reserve.setOutTradeNo(this.outTradeNo);
        reserve.setTradeNo(this.tradeNo);
        reserve.setPayAmt(this.payAmt);
        //生成用户的基本信息
        reserve.setUserId(this.userId);
        reserve.setUserName(this.userName);
        return reserve;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    @Override
    public String toString() {
        return "CandidateReserve{" +
                "guid='" + guid + '\'' +
                ", userId='" + userId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", useTime=" + useTime +
                ", status=" + status +
                ", tableCode='" + tableCode + '\'' +
                ", processStartDate='" + processStartDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", payAmt=" + payAmt +
                ", tradeNo='" + tradeNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                '}';
    }
}
