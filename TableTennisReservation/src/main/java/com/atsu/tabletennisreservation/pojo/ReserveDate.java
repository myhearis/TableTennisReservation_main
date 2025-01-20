package com.atsu.tabletennisreservation.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

//预订时间类
public class ReserveDate implements Serializable {
    private static final long serialVersionUID = -2829254870751667712L;
    //用于保存redis中未支付订单的key,线程安全的
    private static Set<String> redisKeySet=new ConcurrentSkipListSet<>();
    //用于保存球友匹配未支付订单的redis key,线程安全的
    private static Set<String> matchInfoRedisKeySet=new ConcurrentSkipListSet<>();
    private BallTable ballTable;//球桌
    private String tableCode;//球桌号
    private String dateStr;//日期，yyyy-MM-dd
    private List<Integer> dateList;//日期信息24小时
    //初始化代码块
    {
        init();
    }
    public void init(){
        //初始化日期信息
        dateList=new ArrayList<>(24);//长度24，代表24小时制
        for (int i=0;i<24;i++){
            dateList.add(0);
        }
    }
    public void init(Reserve reserve){
        //根据传入的预订单初始化时间列表
        String startDate = reserve.getStartDate();//yyyy-MM-dd hh
        int hour=Integer.parseInt(startDate.substring(11,13));
        Integer useTime = reserve.getUseTime();
        this.dateStr=startDate.substring(0,10);//初始化日期
        updateDateList(hour,useTime);//根据预订请求信息，更新已预订的时间信息
        this.ballTable=reserve.getBallTable();
        this.tableCode=reserve.getTableCode();
    }
    /**
     * #Description 传入使用时间，判断是否允许预定
     * @param hour: 24小时制
     * @param useTime:  使用时间，小时
     * @return boolean
     * @author sujinbin
     * #Date 2024/2/6
     */
    public  boolean verifyCanReserve(Integer hour,Integer useTime){
        for (int i=hour;i<dateList.size()&&i<hour+useTime;i++){
           int site = this.dateList.get(i);
           if (site!=0){
               return false;
           }
        }
        return true;
    }
    /**
     * #Description 进行预订
     * @param hour: 开始使用的时间
     * @param useTime: 使用时长
     * @return boolean
     * @author sujinbin
     * #Date 2024/2/18
     */
    public boolean reserveProcess(Integer hour,Integer useTime){
        if (verifyCanReserve(hour,useTime)){
            updateDateList(hour,useTime);
            return true;
        }
        return false;
    }
    /**
     * #Description 取消预订，还原预订队列信息
     * @param hour:
     * @param useTime:
     * @return boolean
     * @author sujinbin
     * #Date 2024/2/20
     */
    public boolean cancelReserveProcess(Integer hour,Integer useTime){
        updateDateList(hour, useTime,0);
        return true;
    }
    public  void  updateDateList(Integer hour,Integer useTime){
        //进行预订信息的时间更新
        for (int i=hour;i<dateList.size()&&i<hour+useTime;i++){
            this.dateList.set(i,1);
        }
    }
    /**
     * #Description 更新设置对应值
     * @param hour: 使用时长
     * @param useTime: 使用时间
     * @param value: 设置的值
     * @return void
     * @author sujinbin
     * #Date 2024/2/20
     */
    public  void  updateDateList(Integer hour,Integer useTime,Integer value){
        //进行预订信息的时间更新
        for (int i=hour;i<dateList.size()&&i<hour+useTime;i++){
            this.dateList.set(i,value);
        }
    }
    /**
     * #Description 获取当前日期的可预订区间信息列表
     * @param :
     * @return java.util.List<com.atsu.tabletennisreservation.pojo.Period>
     * @author sujinbin
     * #Date 2024/2/28
     */
    public List<Period> getPeriodList(){
        List<Period> periodList=new ArrayList<>();
        Integer start=0;//首索引
        Integer end=0;//尾索引
        boolean site=false;//是否找到了期间,用于设置首索引的标志
        //将日期队列转换为区间对象
        for (int i=0;i < dateList.size();i++){
             if (!site&&dateList.get(i)==1){
                 site=true;
                 end=i;
                 //构造对象
                 Period period=new Period();
                 period.setStartTime(start);
                 period.setEndTime(end);
                 period.setDateStr(this.dateStr);
                 period.setTableCode(this.tableCode);
                 period.setUseTime(end-start);
                 periodList.add(period);
             }
             if (site&&dateList.get(i)==0){
                 start=i;
                 site=false;//重置，继续查找下一个期间
             }
        }
        //如果遍历完成还没找到下一个期间，那么说明到0点
        if (!site){
            Period period=new Period();
            period.setStartTime(start);
            period.setEndTime(0);
            period.setUseTime(24-start);
            period.setDateStr(this.dateStr);
            period.setTableCode(this.tableCode);
            periodList.add(period);
        }
        return periodList;
    }

    public ReserveDate() {
        init();
    }

    public BallTable getBallTable() {
        return ballTable;
    }
    public static Set<String> getRedisKeySet(){
        return redisKeySet;
    }

    public void setBallTable(BallTable ballTable) {
        this.ballTable = ballTable;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public List<Integer> getDateList() {
        return dateList;
    }

    public void setDateList(List<Integer> dateList) {
        this.dateList = dateList;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public static Set<String> getMatchInfoRedisKeySet() {
        return matchInfoRedisKeySet;
    }

    @Override
    public String toString() {
        return "ReserveDate{" +
                "ballTable=" + ballTable +
                ", tableCode='" + tableCode + '\'' +
                ", dateStr='" + dateStr + '\'' +
                ", dateList=" + dateList +
                '}';
    }
}
