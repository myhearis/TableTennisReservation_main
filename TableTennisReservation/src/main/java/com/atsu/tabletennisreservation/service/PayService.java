package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.math.BigDecimal;

//支付相关的业务类
public interface PayService {
    String getOutTradeNo(String userId,String TableId);//生成一个商户订单号
    //根据预订单，生成支付json对象
    JSONObject createPayOrderData(Reserve reserve) throws JSONException;
    //根据信息，生成退款的json对象
    JSONObject createRefundPayData(Reserve reserve) throws JSONException;
    JSONObject createRefundPayData(String tradeNo, BigDecimal refundAmount,String outRequestNo) throws JSONException;//全额退款
    //根据匹配单信息，生成要支付的匹配金额数据
    JSONObject createMatchPayOrderData(MatchInfo matchInfo,String outTradeNo) throws JSONException;
    //创建支付订单数据（通用）
    JSONObject createCommonPayOrderData(BigDecimal payAmt,String subject,String outTradeNo) throws JSONException;
}
