package com.atsu.tabletennisreservation.pojo;

import com.atsu.tabletennisreservation.utils.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

//预订信息
public class Reserve implements Serializable {
    private static final long serialVersionUID = -2829294470751667712L;
    private BallTable ballTable;//预订对应的球桌信息
    private String guid;
    private String userId;//用户id
    private  String userName;//用户名
    private String tableId;//球桌id
    private  String startDate;//开始时间 yyyy-MM-dd HH:mm:ss
    private  Integer useTime;//使用时长（小时）
    private BigDecimal payAmt;
    private Integer reserveStatus;//预订状态:预订状态. -3 预订失败，0 预订成功，待付款，1 待匹配球友拼桌， 3 付款成功，待使用 4:已使用 6 预订取消 9 预订过期 ',
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;
    private String tableCode;
    private String processStartDate;//开始日期 yyyy-MM-dd,跟startDate的年月日相同
    private String tradeNo;//支付交易号 TRADE_NO
    private String outTradeNo;//商户订单号 OUT_TRADE_NO
    private String matchUserMatchInfoId; // MATCH_USER_MATCH_INFO_ID
    private Integer isDeleted;//逻辑删除 0 未删除 1 已删除

    //获取redis的key，用户id_球桌id_开始时间_使用时长
    public String getRedisKey() {
        StringBuffer key=new StringBuffer();
        key.append(userId.hashCode());
        key.append(tableId.hashCode());
        //日期
        DateUtil dateUtil=new DateUtil();
        Date parse = dateUtil.parse(startDate);
        key.append(parse.getTime());
        key.append(useTime);
        return key.toString();
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMatchUserMatchInfoId() {
        return matchUserMatchInfoId;
    }

    public void setMatchUserMatchInfoId(String matchUserMatchInfoId) {
        this.matchUserMatchInfoId = matchUserMatchInfoId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BallTable getBallTable() {
        return ballTable;
    }

    public void setBallTable(BallTable ballTable) {
        this.ballTable = ballTable;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public Integer getReserveStatus() {
        return reserveStatus;
    }

    public void setReserveStatus(Integer reserveStatus) {
        this.reserveStatus = reserveStatus;
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

    public String getProcessStartDate() {
        return processStartDate;
    }

    public void setProcessStartDate(String processStartDate) {
        this.processStartDate = processStartDate;
    }
    //生成预订队列的key
    public String getReserveDateKey(){
        StringBuffer key=new StringBuffer();
        key.append(this.tableCode).append("_").append(this.startDate.substring(0,10));//使用startDate，防止新增的时候不存在processStartDate
        return  key.toString();
    }
    //静态，生成预订队列的key
    public static String getReserveDateKey(String tableCode,String process_date){
        StringBuffer key=new StringBuffer();
        key.append(tableCode).append("_").append(process_date);
        return key.toString();
    }

    @Override
    public String toString() {
        return "Reserve{" +
                "ballTable=" + ballTable +
                ", guid='" + guid + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", tableId='" + tableId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", useTime=" + useTime +
                ", payAmt=" + payAmt +
                ", reserveStatus=" + reserveStatus +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", processStartDate='" + processStartDate + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", matchUserMatchInfoId='" + matchUserMatchInfoId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve reserve = (Reserve) o;
        return Objects.equals(ballTable, reserve.ballTable) &&
                Objects.equals(guid, reserve.guid) &&
                Objects.equals(userId, reserve.userId) &&
                Objects.equals(userName, reserve.userName) &&
                Objects.equals(tableId, reserve.tableId) &&
                Objects.equals(startDate, reserve.startDate) &&
                Objects.equals(useTime, reserve.useTime) &&
                Objects.equals(payAmt, reserve.payAmt) &&
                Objects.equals(reserveStatus, reserve.reserveStatus) &&
                Objects.equals(createDate, reserve.createDate) &&
                Objects.equals(createUser, reserve.createUser) &&
                Objects.equals(updateDate, reserve.updateDate) &&
                Objects.equals(updateUser, reserve.updateUser) &&
                Objects.equals(tableCode, reserve.tableCode) &&
                Objects.equals(processStartDate, reserve.processStartDate) &&
                Objects.equals(tradeNo, reserve.tradeNo) &&
                Objects.equals(outTradeNo, reserve.outTradeNo) &&
                Objects.equals(matchUserMatchInfoId, reserve.matchUserMatchInfoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ballTable, guid, userId, userName, tableId, startDate, useTime, payAmt, reserveStatus, createDate, createUser, updateDate, updateUser, tableCode, processStartDate, tradeNo, outTradeNo, matchUserMatchInfoId);
    }
}
