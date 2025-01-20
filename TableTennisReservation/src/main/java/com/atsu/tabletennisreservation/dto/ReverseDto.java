package com.atsu.tabletennisreservation.dto;

import com.atsu.tabletennisreservation.pojo.CandidateReserve;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.utils.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
//用于接受前台预订请求并转化为Reverse实体
public class ReverseDto implements Serializable {
    private static final long serialVersionUID = -2829294211701667711L;
    private String ballTableId;
    private String startDate;//预订日期  yyyu-MM-dd
    private Integer useDate;//当前使用开始时间(24小时)
    private Integer countHour;//使用时长 (使用小时数量)
    private String guid;//id
    private String userId;
    private String code;
    private String payAmt;

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getUseDate() {
        return useDate;
    }

    public void setUseDate(Integer useDate) {
        this.useDate = useDate;
    }

    public Integer getCountHour() {
        return countHour;
    }

    public void setCountHour(Integer countHour) {
        this.countHour = countHour;
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

    public String getBallTableId() {
        return ballTableId;
    }

    public void setBallTableId(String ballTableId) {
        this.ballTableId = ballTableId;
    }

    //dto转化为Reverse
    public Reserve parseReserve(){
        Reserve reserve=new Reserve();
        String startDate = this.getStartDate();
        String[] split = startDate.split("-");
        LocalDateTime localDateTime=LocalDateTime.of(Integer.parseInt(split[0]),Integer.parseInt(split[1]),Integer.parseInt(split[2]),this.useDate,0);
        // 转换为 Instant 对象
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        // 获取毫秒数
        long milliseconds = instant.toEpochMilli();
        //转化为Date
        Date date=new Date(milliseconds);
        DateUtil dateUtil=new DateUtil();
        String dateToStr = dateUtil.dateToStr(date);
        reserve.setStartDate(dateToStr);
        reserve.setUseTime(this.countHour);
        reserve.setTableId(this.ballTableId);
        reserve.setTableCode(this.code);
        //设置金额
        reserve.setPayAmt(new BigDecimal(this.payAmt));
        return reserve;
    }
    public CandidateReserve parseCandidateReserve(){
        Reserve reserve = parseReserve();
        CandidateReserve candidateReserve=new CandidateReserve();
        candidateReserve.setUseTime(reserve.getUseTime());
        candidateReserve.setStartDate(reserve.getStartDate());
        candidateReserve.setTableId(reserve.getTableId());
        candidateReserve.setTableCode(reserve.getTableCode());
        return candidateReserve;
    }

    @Override
    public String toString() {
        return "ReverseDto{" +
                "startDate='" + startDate + '\'' +
                ", useDate=" + useDate +
                ", countHour=" + countHour +
                ", guid='" + guid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
