package com.atsu.tabletennisreservation.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    //精确到秒数
    public static final String DEFAULT_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String PAY_TIME_DEFAULT_FORMAT="yyyyMMddHHmmssSSS";
    private String format=DEFAULT_FORMAT;
    public static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DEFAULT_FORMAT);
    public static final SimpleDateFormat payTimeSimpleDateFormat=new SimpleDateFormat(PAY_TIME_DEFAULT_FORMAT);

    //传入日期，格式化成字符串
    public String dateToStr(Date date){
        String format = simpleDateFormat.format(date);
        return format;
    }
    //传入字符串，解析为日期对象
    public Date parse(String dateStr){
        Date parse=null;
        try {

            parse = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("日期解析失败！检查格式是否正确",e);
        }
        return parse;

    }
    //生成未来n天的格式化后的日期列表,日期格式 yyyy-MM-dd
    public List<String> createFutureDataStrList(Integer dataCount){
        List<String> list=new ArrayList<>();
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 创建日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 打印未来n天的日期
        for (int i = 0; i < dataCount; i++) {
            LocalDate futureDate = today.plusDays(i);
            String format = futureDate.format(formatter);
            list.add(format);
        }
        return list;
    }
    //生成未来n天的格式化后的日期散列表,日期格式 yyyy-MM-dd
    public Set<String> createFutureDataStrSet(Integer dataCount){
        Set<String> set=new HashSet<>();
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 创建日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 打印未来n天的日期
        for (int i = 0; i < dataCount; i++) {
            LocalDate futureDate = today.plusDays(i);
            String format = futureDate.format(formatter);
            set.add(format);
        }
        return set;
    }
    //生成 年月日字符串 pay
    public String getNowPayTimeStr(){
        Date date=new Date();
        String format = payTimeSimpleDateFormat.format(date);
        return format;
    }
    public static void main(String[] args) {
        //获取当前时间
        DateUtil dateUtil=new DateUtil();
        String nowPayTimeStr = dateUtil.getNowPayTimeStr();
        System.out.println(nowPayTimeStr);
    }
}
