package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//预订单相关
@Controller
@RequestMapping("/orders")
public class OrdersController {
    //预订信息管理
    @GetMapping("/manager")
    public String orders(){
        return "manager/ordersManager.html";
    }
    //响应json订单数据
    @GetMapping("/")
    public Result getOrdersInfo(){
        return null;
    }
}
