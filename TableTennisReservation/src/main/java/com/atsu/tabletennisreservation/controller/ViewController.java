package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Controller
public class ViewController {
    @Resource
    private RedisDao redisDao;
    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @GetMapping("/test/testPage")
    public String getMyTestPage(){
        return "test/testPage";
    }
    //测试redis
    @GetMapping("/redis")
    @ResponseBody
    public Result testRedis(){
        Reserve reserve=new Reserve();
        reserve.setPayAmt(new BigDecimal(19));
        reserve.setUserId("fdasrfdsa");
        reserve.setUserName("test");
        reserve.setStartDate("20240101");
        boolean b = redisDao.addReserve("fff",reserve, 100);
        return Result.success("ok");
    }
    //跳转支付成功页
    @GetMapping("/paySucess")
    public String paySuccess(){
        return "pay/paySuccess";
    }
}
