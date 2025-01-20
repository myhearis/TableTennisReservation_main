package com.atsu.tabletennisreservation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.atsu.tabletennisreservation.dto.MailDTO;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.propertyBean.PayPropertyBean;
import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.utils.ZfbCommonUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//支付宝
@Controller
@RequestMapping("/pay")
public class ZfbController {
    private static Logger logger=Logger.getLogger(ZfbController.class.getName());
    @Resource
    private PayService payService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private MatchInfoService matchInfoService;
    @Resource
    private ReverseService reverseService;
    @Resource
    private CommonService commonService;
    @Resource
    private CandidateReserveService candidateReserveService;
    @Resource
    private PayPropertyBean payPropertyBean;
    @Resource
    private UserService userService;
    @Resource
    private MailService mailService;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    public static final String CHARSET="UTF-8";
    //处理场馆预订支付请求(此时订单已经被预处理过了，已经锁定在redis中)
    @GetMapping("/processPayReserve/{redisKey}")
    public void processPayReserve(HttpServletResponse httpServletResponse, @PathVariable("redisKey") String redisKey, Model model) throws AlipayApiException, IOException, JSONException {

        //获取支付宝网关
        String URL=ZfbCommonUtils.getZFBinfoValue("open_api_domain");
        //获取支付宝应用id:app_id
        String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
        //获取应用私钥
        String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
        //获取支付宝公钥
        String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
        //异步通知地址
        String NOTIFY_URL=ZfbCommonUtils.getZFBinfoValue("notify_url");
        //同步返回地址
        String RETURN_URL=ZfbCommonUtils.getZFBinfoValue("return_url");
        /** 初始化阿里支付客户端对象 **/
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(NOTIFY_URL);//通知的url
        request.setReturnUrl(RETURN_URL);//支付完成以后，返回的url
        //从redis中获取预订单
        List<String> keys=new ArrayList<>();
        keys.add(redisKey);
        List<Reserve> reserveList = redisDao.getReserveList(keys);
        if (reserveList!=null&&reserveList.size()==1){
            Reserve reserve=reserveList.get(0);
            JSONObject bizContent = payService.createPayOrderData(reserve);
            request.setBizContent(bizContent.toString());
            //将请求传入阿里支付客户端对象，并执行，返回一个response
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            //输出相应体的内容
            System.out.println(response.getBody());
            if(response.isSuccess()){
                System.out.println("调用成功");
            } else {
                System.out.println("调用失败");
            }
            httpServletResponse.setContentType( "text/html;charset="  + CHARSET);
            httpServletResponse.getWriter().write(response.getBody()); //直接将完整的表单html输出到页面
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        }
        else {
            //redis 已过期

        }

    }
    //处理球友匹配支付请求
    @GetMapping("/processApplyMatchingPayReserve/{guid}")
    public void processApplyMatchingPayReserve(HttpServletResponse httpServletResponse, @PathVariable("guid") String matchInfoGuid, Model model) throws AlipayApiException, IOException, JSONException {
        //获取支付宝网关
        String URL=ZfbCommonUtils.getZFBinfoValue("open_api_domain");
        //获取支付宝应用id:app_id
        String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
        //获取应用私钥
        String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
        //获取支付宝公钥
        String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
        //异步通知地址
        String NOTIFY_URL=ZfbCommonUtils.getZFBinfoValue("match_notify_url");
        //同步返回地址
        String RETURN_URL=ZfbCommonUtils.getZFBinfoValue("match_return_url");
        /** 初始化阿里支付客户端对象 **/
        AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(NOTIFY_URL);//支付成功后，异步通知的url
        request.setReturnUrl(RETURN_URL);//支付完成以后，返回的url
        //根据匹配单信息，获取到预订单信息
        MatchInfo matchInfo = matchInfoService.getMatchInfoById(matchInfoGuid);
        //获取预订单,校验订单是否存在
        Reserve reserve=null;
        if (matchInfo!=null){
            reserve=reverseService.getReserveById(matchInfo.getBillReserveId());
        }
        //如果预订单不为空
        try {
            if (reserve!=null){
                //先初始化匹配单中当前用户操作的数据
                User user = commonService.getThisUserId();
                //初始化商户订单号和用户信息，最后使用交易号作为是否交易的依据
                matchInfo.setMatchUserId(user.getGuid());//补充匹配用户信息
                matchInfo.setMatchUserName(user.getUserName());
                String outTradeNo = payService.getOutTradeNo(user.getGuid(), matchInfo.getTableId());
                matchInfo.setMatchUserOutTradeNo(outTradeNo);//获取新生成的商户订单号
                logger.info("预处理匹配：生成商户订单号 ["+matchInfo.getMatchUserOutTradeNo()+"]");
                //将当前匹配单存入redis中,该方法内部会进行线程互斥，不通过会抛异常
                matchInfoService.processMatching(matchInfoGuid,outTradeNo);
                //如果不开启支付宝，直接生成匹配信息
                if (!systemParamsProperty.isUseZfb()){
                    //补充数据
//                    matchInfo.setMatchUserTradeNo(trade_no);
                    //设置状态
                    matchInfo.setStatus(1);//待单主确认状态
                    //入库
                    matchInfoService.updateMatchInfoById(matchInfo);
                    //移除redis key
//                    ReserveDate.getMatchInfoRedisKeySet().remove(out_trade_no);
                    //给主单用户推送邮件消息
                    userService.getUserById(matchInfo.getBillUserId());
                    if (!StringTool.isNull(user.getEmail())){
                        mailService.send(user.getEmail(),"球友匹配申请","您收到了来自用户["+matchInfo.getMatchUserName()+"]的球友匹配申请，请到系统“球友匹配-我的发布”中查收进行下一步操作");
                    }
                    logger.info("从redis中获取匹配单数据并入库完成!");
                }
                //构造支付单
                JSONObject bizContent = payService.createMatchPayOrderData(matchInfo,outTradeNo);
                request.setBizContent(bizContent.toString());
                //将请求传入阿里支付客户端对象，并执行，返回一个response
                AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
                //输出相应体的内容
                System.out.println(response.getBody());
                if(response.isSuccess()){
                    System.out.println("调用成功");
                } else {
                    System.out.println("调用失败");
                }
                httpServletResponse.setContentType( "text/html;charset="  + CHARSET);
                httpServletResponse.getWriter().write(response.getBody()); //直接将完整的表单html输出到页面
                httpServletResponse.getWriter().flush();
                httpServletResponse.getWriter().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setContentType( "text/html;charset="  + CHARSET);
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(e.getMessage());
            httpServletResponse.getWriter().flush();//刷新缓冲区
            httpServletResponse.getWriter().close();//关闭输出流
        }
        finally {

        }
    }
    //球友匹配支付宝支付成功回调(响应页面)
    @GetMapping("/processMatchPaySuccessReverse/")
    public String processMatchPaySuccessReverse(@RequestParam Map<String,String> params){
        //获取商户订单号
        logger.info("支付宝支付成功回调方法!");
        //跳转回球友匹配界面
        return  "redirect:/playerMatching/";
    }
    //球友匹配支付宝支付成功回调（异步通知）
    @PostMapping("/processMatchPaySuccessNotify/")
    @ResponseBody
    public Result processMatchPaySuccessNotify(@RequestParam Map<String,String> paramsMap) throws JSONException, AlipayApiException {
        //获取支付宝网关
        String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
        //获取支付宝应用id:app_id
        String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
        //获取应用私钥
        String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
        //获取支付宝公钥
        String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
        String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
        String CHARSET=ZfbCommonUtils.getZFBinfoValue("charset");
        logger.info("支付宝支付成功异步通知!");
        boolean signVerified=false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramsMap, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE) ;//调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure\
            logger.info("验签成功，支付结果异步通知中");
            logger.info(paramsMap.toString());
            //获取支付状态
            String trade_status = paramsMap.get("trade_status");
            if ("TRADE_SUCCESS".equals(trade_status)){
                //获取商户订单号
                String out_trade_no = paramsMap.get("out_trade_no");
                String trade_no = paramsMap.get("trade_no");
                String total_amount = paramsMap.get("total_amount");
                //将信息入库
                //从redis中获取信息
                List<String> keyList=new ArrayList<>();
                keyList.add(out_trade_no);
                List<MatchInfo> matchInfoList = redisDao.getMatchInfoList(keyList);
                if (matchInfoList!=null&&matchInfoList.size()==1){
                    //获取到了数据
                    MatchInfo matchInfo = matchInfoList.get(0);
                    //补充数据
                    matchInfo.setMatchUserTradeNo(trade_no);
                    //设置状态
                    matchInfo.setStatus(1);//待单主确认状态
                    //入库
                    matchInfoService.updateMatchInfoById(matchInfo);
                    //移除redis key
                    ReserveDate.getMatchInfoRedisKeySet().remove(out_trade_no);
                    //给主单用户推送邮件消息
                    User user = userService.getUserById(matchInfo.getBillUserId());
                    if (!StringTool.isNull(user.getEmail())){
                        mailService.send(user.getEmail(),"球友匹配申请","您收到了来自用户["+matchInfo.getMatchUserName()+"]的球友匹配申请，请到系统“球友匹配-我的发布”中查收进行下一步操作");
                    }
                    logger.info("从redis中获取匹配单数据并入库完成!");
                }
                else {
                    //redis已过期，退款
                    logger.info("球友匹配：用户未在规定时间内付款，将进行退款处理.......");
                    /** 初始化阿里支付客户端对象 **/
                    AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    //获取退款数据对象，这里直接全额退款
                    //生成退款号
                    String id = commonService.getId();
                    JSONObject refundPayData = payService.createRefundPayData(trade_no, new BigDecimal(total_amount), id);
                    request.setBizContent(refundPayData.toString());
                    AlipayTradeRefundResponse response = alipayClient.execute(request);
                    if(response.isSuccess()){
                        logger.info("退款成功，退款金额:"+total_amount);
                    } else {
                        logger.info("退款调用失败!");
                    }
                }
            }
            //如果不是支付成功，则清楚原有匹配数据信息
            else {
                logger.info("交易失败");
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
        }
        return  Result.success("回调");
    }
    //候补，支付成功后的异步通知
    @PostMapping("/processCandidatePaySuccessNotify/")
    @ResponseBody
    public Result processCandidatePaySuccessNotify(@RequestParam Map<String,String> paramsMap) throws JSONException, AlipayApiException {
        //获取支付宝网关
        String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
        //获取支付宝应用id:app_id
        String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
        //获取应用私钥
        String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
        //获取支付宝公钥
        String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
        String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
        String CHARSET=ZfbCommonUtils.getZFBinfoValue("charset");
        logger.info("支付宝支付成功异步通知!");
        boolean signVerified=false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(paramsMap, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE) ;//调用SDK验证签名
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure\
            logger.info("候补单生成成功，支付结果异步通知中");
            logger.info(paramsMap.toString());
            //获取支付状态
            String trade_status = paramsMap.get("trade_status");
            if ("TRADE_SUCCESS".equals(trade_status)){
                //获取商户订单号
                String out_trade_no = paramsMap.get("out_trade_no");
                String trade_no = paramsMap.get("trade_no");
                String total_amount = paramsMap.get("total_amount");
                //从redis中获取信息
                CandidateReserve candidateReserve = redisDao.getCandidateReserve(out_trade_no);
                if (candidateReserve!=null){
                    //补充信息
                    candidateReserve.setTradeNo(trade_no);
                    //将信息入库
                    int i = candidateReserveService.saveCandidateReserve(candidateReserve);
                    if (i>0){
                        logger.info("候补单入库成功!");
                        logger.info(candidateReserve.toString());
                        //推送邮件
                        String targetEmail = userService.getUserEmail(candidateReserve.getUserId());
                        if (!StringTool.isNull(targetEmail)){
                            mailService.send(targetEmail,"候补单已生成","您的候补单已生成，若该球桌在您的预订期间内有空缺，则会自动帮您预订!");
                        }
                    }
                    else {

                    }
                }
                else {
                    //redis已过期，退款
                    logger.info("候补：用户未在规定时间内付款，将进行退款处理.......");
                    /** 初始化阿里支付客户端对象 **/
                    AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
                    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                    //获取退款数据对象，这里直接全额退款
                    //生成退款号
                    String id = commonService.getId();
                    JSONObject refundPayData = payService.createRefundPayData(trade_no, new BigDecimal(total_amount), id);
                    request.setBizContent(refundPayData.toString());
                    AlipayTradeRefundResponse response = alipayClient.execute(request);
                    if(response.isSuccess()){
                        logger.info("退款成功，退款金额:"+total_amount);
                        //redis获取不到候补单，没有用户id无法确定邮箱信息和用户消息，不做操作
                        //mailService.send("1602820115@qq.com","候补生成失败","用户未在规定时间内付款，已进行退款处理，退款金额"+total_amount+"元");
                    } else {
                        logger.info("退款调用失败!");
                    }
                }
            }
            //如果不是支付成功，则提示用户原有匹配数据信息
            else {
                logger.info("交易失败");
            }
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
        }
        return Result.success("候补支付成功回调");
    }

}
