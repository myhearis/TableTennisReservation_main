package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.PayService;
import com.atsu.tabletennisreservation.utils.DateUtil;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.google.gson.Gson;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PayServiceImpl implements PayService {
    private DateUtil dateUtil=new DateUtil();
    @Resource
    private CommonService commonService;
    @Override
    public String getOutTradeNo(String userId,String tableId) {
        //生成订单号   当前时间+当前用户id哈希值+当前球桌id哈希值
        StringBuffer outTradeNo=new StringBuffer();
        outTradeNo.append(dateUtil.getNowPayTimeStr());
        outTradeNo.append(userId.hashCode());
        outTradeNo.append(tableId.hashCode());
        return  outTradeNo.toString();
    }

    @Override
    public JSONObject createPayOrderData(Reserve reserve) throws JSONException {
        /*  private String appId;
        private String tradeNo; // 支付宝交易凭证号
        private String outTradeNo; // 原支付请求的商户订单号
        private String outBizNo; // 商户业务ID，主要是退款通知中返回退款申请的流水号
        private String buyerId; // 买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字
        private String buyerLogonId; // 买家支付宝账号
        private String sellerId; // 卖家支付宝用户号
        private String sellerEmail; // 卖家支付宝账号
        private String tradeStatus; // 交易目前所处的状态，见交易状态说明
        private BigDecimal totalAmount; // 本次交易支付的订单金额
        private BigDecimal receiptAmount; // 商家在交易中实际收到的款项
        private BigDecimal buyerPayAmount; // 用户在交易中支付的金额
        private BigDecimal refundFee; // 退款通知中，返回总退款金额，单位为元，支持两位小数
        private String subject; // 商品的标题/交易标题/订单标题/订单关键字等
        private String body; // 该订单的备注、描述、明细等。对应请求时的body参数，原样通知回来
        private Date gmtCreate; // 该笔交易创建的时间。格式为yyyy-MM-dd HH:mm:ss
        private Date gmtPayment; // 该笔交易的买家付款时间。格式为yyyy-MM-dd HH:mm:ss
        private Date gmtRefund; // 该笔交易的退款时间。格式为yyyy-MM-dd HH:mm:ss.S
        private Date gmtClose; // 该笔交易结束时间。格式为yyyy-MM-dd HH:mm:ss
        private String fundBillList; // 支付成功的各个渠道金额信息,array
        private String passbackParams; // 公共回传参数，如果请求时传递了该参数，则返回给商户时会在异步通知时将该参数原样返回。
*/
        //创建一个json对象，填入必须的几个参数
        /*
         out_trade_no:商户订单号。由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复。
         total_amount:订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]。金额不能为0。
         subject:订单标题。注意：不可使用特殊字符，如 /，=，& 等。
         product_code:销售产品码，与支付宝签约的产品码名称。注：目前电脑支付场景下仅支持FAST_INSTANT_TRADE_PAY
         * */
        User user = commonService.getThisUserId();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", reserve.getOutTradeNo());//商户订单号,同时也是redisKey
        bizContent.put("total_amount", reserve.getPayAmt());
        bizContent.put("subject", "球桌预订");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        Gson gson=new Gson();
        //把当前预订单也保存进去，用于回调
        //// 商品明细信息，按需传入
        JSONArray goodsDetail = new JSONArray();
        JSONObject goods1 = new JSONObject();
        goods1.put("goods_id", "goodsNo1");
        goods1.put("goods_name", "子商品1");
        goods1.put("quantity", 1);
        goods1.put("price", 0.01);
        goodsDetail.put(0,goods1);
        bizContent.put("goods_detail", goodsDetail);
        bizContent.put("goods_detail",goodsDetail);
        //加入参数
        bizContent.put("passbackParams",gson.toJson(reserve));
        return bizContent;
    }

    @Override
    public JSONObject createRefundPayData(Reserve reserve) throws JSONException {
        JSONObject bizContent = new JSONObject();
        //生成退款请求号
        String out_request_no = commonService.getId();
        //交易号和商户订单号二选一
        if (!StringTool.isNull(reserve.getTradeNo())){
            bizContent.put("trade_no", reserve.getTradeNo());
        }
        else {
            bizContent.put("out_trade_no", reserve.getOutTradeNo());
        }
        //退款金额
        bizContent.put("refund_amount", reserve.getPayAmt());
        /*退款请求号。 标识一次退款请求，需要保证在交易号下唯一，如需部分退款，则此参数必传。
        注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，防止该笔交易重复退款。
        支付宝会保证同样的退款请求号多次请求只会退一次。*/
        bizContent.put("out_request_no", out_request_no);
        return bizContent;
    }
    //全额退款
    @Override
    public JSONObject createRefundPayData(String tradeNo, BigDecimal refundAmount,String outRequestNo) throws JSONException {
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no",tradeNo);
        bizContent.put("refund_amount", refundAmount);
        bizContent.put("out_request_no", outRequestNo);
        return bizContent;
    }

    @Override
    public JSONObject createMatchPayOrderData(MatchInfo matchInfo,String outTradeNo) throws JSONException {
        User user = commonService.getThisUserId();
        //设置金额
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no",outTradeNo);//商户订单号
        bizContent.put("total_amount", matchInfo.getMatchUserNeedPayAmt());
        bizContent.put("subject", "球桌匹配");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        return bizContent;
    }

    @Override
    public JSONObject createCommonPayOrderData(BigDecimal payAmt, String subject, String outTradeNo) throws JSONException {
        //设置金额
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no",outTradeNo);//商户订单号
        bizContent.put("total_amount",payAmt);
        bizContent.put("subject", subject);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        return bizContent;
    }

}
