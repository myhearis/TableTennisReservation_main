package com.atsu.tabletennisreservation.configuration;

import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.ReserveDate;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.ReverseService;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Logger;

//预订基础数据加载
@Component
public class BaseDataLoader implements CommandLineRunner {
    private Logger logger =Logger.getLogger(this.getClass().getName());
    @Resource
    private ApplicationContext applicationContext;//容器对象
    @Resource
    private ReverseService reverseService;
    @Resource
    private CommonService commonService;
    private List<ReserveDate> reserveDateList=new ArrayList<>();//基础数据
    private Map<String,ReserveDate> reserveDateListMap=new Hashtable<>();//日期映射基础数据
    @Override
    public void run(String... args) throws Exception {
        logger.info("----------------------------------初始化基础数据----------------------------------");
        //获取最近7天的有效预订信息
        List<String> dateList = commonService.createFutureDataStrList(7);//获取最近七天的日期字符串列表
//        List<Reserve> reserveByDateList = reverseService.getReserveByDateList(dateList);
        List<Reserve> reserveByDateList = reverseService.getValidReserveByDateList(dateList);
        //生成日期预订数组
        for (int i=0;i<reserveByDateList.size();i++){
            //创建预订日期信息
            ReserveDate reserveDate=new ReserveDate();
            Reserve reserve = reserveByDateList.get(i);
            reserveDate.init(reserve);//初始化
            reserveDateList.add(reserveDate);
            //初始化预订队列和日期的映射关系
            //key:球桌编号_日期
            //value:预订队列
            reserveDateListMap.put(reserve.getReserveDateKey(),reserveDate);
        }
        //开始根据数据补充预订日期列表数据(此时map中都有全部的日期数据了)
        for (int i=0;i<reserveByDateList.size();i++){
            Reserve reserve = reserveByDateList.get(i);
            ReserveDate reserveDate = reserveDateListMap.get(reserve.getReserveDateKey());
            int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer useTime = reserve.getUseTime();
            reserveDate.updateDateList(hour,useTime,1);
        }
        logger.info("----------------------------------初始化预订基础数据完成----------------------------------");
    }
    //根据预订单信息，构造日期映射基础数据
    public static Map<String,ReserveDate> createReserveDateListMap( List<Reserve> reserveList){
        Map<String,ReserveDate> reserveDateListMap=new Hashtable<>();//日期映射基础数据
        //生成日期预订数组
        for (int i=0;i<reserveList.size();i++){
            //创建预订日期信息
            ReserveDate reserveDate=new ReserveDate();
            Reserve reserve = reserveList.get(i);
            reserveDate.init(reserve);//初始化
            reserveDateListMap.put(reserve.getReserveDateKey(),reserveDate);
        }
        //开始模拟预订操作-根据数据补充预订日期列表数据(此时map中都有全部的日期数据了)
        for (int i=0;i<reserveList.size();i++){
            Reserve reserve = reserveList.get(i);
            ReserveDate reserveDate = reserveDateListMap.get(reserve.getReserveDateKey());
            int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer useTime = reserve.getUseTime();
            reserveDate.updateDateList(hour,useTime,1);
        }
        return reserveDateListMap;
    }
    public List<ReserveDate> getReserveDateList() {
        return reserveDateList;
    }

    public void setReserveDateList(List<ReserveDate> reserveDateList) {
        this.reserveDateList = reserveDateList;
    }

    public Map<String, ReserveDate> getReserveDateListMap() {
        return reserveDateListMap;
    }

    public void setReserveDateListMap(Map<String, ReserveDate> reserveDateListMap) {
        this.reserveDateListMap = reserveDateListMap;
    }
}
