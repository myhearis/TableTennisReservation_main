package com.atsu.tabletennisreservation.pojo;
//期间：用于代表可预定的日期期间
public class Period {
    private String tableCode;//球桌号
    private String dateStr;//年-月-日
    private Integer startTime;//24小时制,期间开始时间
    private Integer endTime;//24小时制，期间结束时间
    private Integer useTime;//使用时长：endTime-startTime

    public String getTableCode() {
        return tableCode;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Period{" +
                "dateStr='" + dateStr + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
