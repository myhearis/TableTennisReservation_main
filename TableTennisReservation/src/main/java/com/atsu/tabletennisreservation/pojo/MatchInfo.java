package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/*GUID VARCHAR(50) PRIMARY KEY COMMENT '主键ID',
		BILL_USER_ID VARCHAR(50) COMMENT '主单用户ID',
		BILL_USER_NAME  VARCHAR(100) COMMENT '主单用户名',
		BILL_USER_OUT_TRADE_NO VARCHAR(100) COMMENT '主单用户商户订单号',
		BILL_USER_TRADE_NO VARCHAR(100) COMMENT '主单用户支付交易号',
		MATCH_USER_ID VARCHAR(50) COMMENT '匹配用户ID',
		MATCH_USER_NAME  VARCHAR(100) COMMENT '匹配用户名',
		MATCH_USER_OUT_TRADE_NO VARCHAR(100) COMMENT '匹配用户商户订单号',
		MATCH_USER_TRADE_NO VARCHAR(100) COMMENT '匹配用户支付交易号',
		LEVEL INT COMMENT '技术水平: 1 初级、2 中级、3 高级',
		BILL_REMARK VARCHAR(200) COMMENT '主单用户备注描述',
		TABLE_ID VARCHAR(50) COMMENT '球桌ID',
		TABLE_CODE VARCHAR(20) COMMENT '球桌编码',
		START_DATE VARCHAR(20) COMMENT '开始时间',
		USE_TIME INT COMMENT '预订时长，单位(小时)',
		STATUS INT COMMENT '匹配单状态： 0 正在匹配  1 预匹配，等待主单用户同意 2 匹配成功',
		CREATE_DATE VARCHAR(20) COMMENT '创建日期',
		CREATE_USER VARCHAR(100) COMMENT '创建用户（用户名）',
		UPDATE_DATE VARCHAR(20) COMMENT '更新日期',
		UPDATE_USER VARCHAR(100) COMMENT '更新用户（用户名）'*/
//匹配单实体
public class MatchInfo implements Serializable {
    private static final long serialVersionUID = -3822292474751167711L;
    private String guid;
    private String billReserveId;
    private String billUserId;
    private String billUserName;
    private String billUserOutTradeNo;
    private String billUserTradeNo;
    private BigDecimal billUserNeedPayAmt;//主单用户应付金额 BILL_USER_NEED_PAY_AMT
    private String matchUserId;
    private String matchUserName;
    private String matchUserOutTradeNo;
    private String matchUserTradeNo;
    private BigDecimal matchUserNeedPayAmt;//匹配用户应付金额 MATCH_USER_NEED_PAY_AMT
    private Integer level;
    private String billRemark;
    private String tableId;
    private String tableCode;
    private String startDate;
    private Integer useTime;
    private Integer status;
    private String createDate;
    private String createUser;
    private String updateDate;
    private String updateUser;
    private String processStartDate;
    private BallTable ballTable;//对应的球桌数据

    public BallTable getBallTable() {
        return ballTable;
    }

    public void setBallTable(BallTable ballTable) {
        this.ballTable = ballTable;
    }

    public BigDecimal getBillUserNeedPayAmt() {
        return billUserNeedPayAmt;
    }

    public void setBillUserNeedPayAmt(BigDecimal billUserNeedPayAmt) {
        this.billUserNeedPayAmt = billUserNeedPayAmt;
    }

    public BigDecimal getMatchUserNeedPayAmt() {
        return matchUserNeedPayAmt;
    }

    public void setMatchUserNeedPayAmt(BigDecimal matchUserNeedPayAmt) {
        this.matchUserNeedPayAmt = matchUserNeedPayAmt;
    }

    public String getGuid() {
        return guid;
    }

    public String getBillUserTradeNo() {
        return billUserTradeNo;
    }

    public void setBillUserTradeNo(String billUserTradeNo) {
        this.billUserTradeNo = billUserTradeNo;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getBillReserveId() {
        return billReserveId;
    }

    public void setBillReserveId(String billReserveId) {
        this.billReserveId = billReserveId;
    }

    public String getBillUserId() {
        return billUserId;
    }

    public void setBillUserId(String billUserId) {
        this.billUserId = billUserId;
    }

    public String getBillUserName() {
        return billUserName;
    }

    public void setBillUserName(String billUserName) {
        this.billUserName = billUserName;
    }

    public String getBillUserOutTradeNo() {
        return billUserOutTradeNo;
    }

    public void setBillUserOutTradeNo(String billUserOutTradeNo) {
        this.billUserOutTradeNo = billUserOutTradeNo;
    }

    public String getMatchUserId() {
        return matchUserId;
    }

    public void setMatchUserId(String matchUserId) {
        this.matchUserId = matchUserId;
    }

    public String getMatchUserName() {
        return matchUserName;
    }

    public void setMatchUserName(String matchUserName) {
        this.matchUserName = matchUserName;
    }

    public String getMatchUserOutTradeNo() {
        return matchUserOutTradeNo;
    }

    public void setMatchUserOutTradeNo(String matchUserOutTradeNo) {
        this.matchUserOutTradeNo = matchUserOutTradeNo;
    }

    public String getMatchUserTradeNo() {
        return matchUserTradeNo;
    }

    public void setMatchUserTradeNo(String matchUserTradeNo) {
        this.matchUserTradeNo = matchUserTradeNo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getBillRemark() {
        return billRemark;
    }

    public void setBillRemark(String billRemark) {
        this.billRemark = billRemark;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchInfo matchInfo = (MatchInfo) o;
        return Objects.equals(guid, matchInfo.guid) &&
                Objects.equals(billReserveId, matchInfo.billReserveId) &&
                Objects.equals(billUserId, matchInfo.billUserId) &&
                Objects.equals(billUserName, matchInfo.billUserName) &&
                Objects.equals(billUserOutTradeNo, matchInfo.billUserOutTradeNo) &&
                Objects.equals(billUserTradeNo, matchInfo.billUserTradeNo) &&
                Objects.equals(billUserNeedPayAmt, matchInfo.billUserNeedPayAmt) &&
                Objects.equals(matchUserId, matchInfo.matchUserId) &&
                Objects.equals(matchUserName, matchInfo.matchUserName) &&
                Objects.equals(matchUserOutTradeNo, matchInfo.matchUserOutTradeNo) &&
                Objects.equals(matchUserTradeNo, matchInfo.matchUserTradeNo) &&
                Objects.equals(matchUserNeedPayAmt, matchInfo.matchUserNeedPayAmt) &&
                Objects.equals(level, matchInfo.level) &&
                Objects.equals(billRemark, matchInfo.billRemark) &&
                Objects.equals(tableId, matchInfo.tableId) &&
                Objects.equals(tableCode, matchInfo.tableCode) &&
                Objects.equals(startDate, matchInfo.startDate) &&
                Objects.equals(useTime, matchInfo.useTime) &&
                Objects.equals(status, matchInfo.status) &&
                Objects.equals(createDate, matchInfo.createDate) &&
                Objects.equals(createUser, matchInfo.createUser) &&
                Objects.equals(updateDate, matchInfo.updateDate) &&
                Objects.equals(updateUser, matchInfo.updateUser) &&
                Objects.equals(processStartDate, matchInfo.processStartDate) &&
                Objects.equals(ballTable, matchInfo.ballTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid, billReserveId, billUserId, billUserName, billUserOutTradeNo, billUserTradeNo, billUserNeedPayAmt, matchUserId, matchUserName, matchUserOutTradeNo, matchUserTradeNo, matchUserNeedPayAmt, level, billRemark, tableId, tableCode, startDate, useTime, status, createDate, createUser, updateDate, updateUser, processStartDate, ballTable);
    }

    @Override
    public String toString() {
        return "MatchInfo{" +
                "guid='" + guid + '\'' +
                ", billReserveId='" + billReserveId + '\'' +
                ", billUserId='" + billUserId + '\'' +
                ", billUserName='" + billUserName + '\'' +
                ", billUserOutTradeNo='" + billUserOutTradeNo + '\'' +
                ", billUserTradeNo='" + billUserTradeNo + '\'' +
                ", billUserNeedPayAmt=" + billUserNeedPayAmt +
                ", matchUserId='" + matchUserId + '\'' +
                ", matchUserName='" + matchUserName + '\'' +
                ", matchUserOutTradeNo='" + matchUserOutTradeNo + '\'' +
                ", matchUserTradeNo='" + matchUserTradeNo + '\'' +
                ", matchUserNeedPayAmt='" + matchUserNeedPayAmt + '\'' +
                ", level=" + level +
                ", billRemark='" + billRemark + '\'' +
                ", tableId='" + tableId + '\'' +
                ", tableCode='" + tableCode + '\'' +
                ", startDate='" + startDate + '\'' +
                ", useTime=" + useTime +
                ", status=" + status +
                ", createDate='" + createDate + '\'' +
                ", createUser='" + createUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", processStartDate='" + processStartDate + '\'' +
                ", ballTable=" + ballTable +
                '}';
    }
}
