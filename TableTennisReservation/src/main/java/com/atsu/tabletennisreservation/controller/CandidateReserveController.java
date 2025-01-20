package com.atsu.tabletennisreservation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.dto.ReverseDto;
import com.atsu.tabletennisreservation.pojo.BallTable;
import com.atsu.tabletennisreservation.pojo.CandidateReserve;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.utils.ZfbCommonUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

@Controller
@RequestMapping("/candidateReserve")
public class CandidateReserveController {
    @Resource
    private CandidateReserveService candidateReserveService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private BallTableService ballTableService;
    @Resource
    private PayService payService;
    @Resource
    private CommonService commonService;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    @Resource
    private UserService userService;
    @Resource
    private MailService mailService;
    private Logger logger=Logger.getLogger(this.getClass().getName());
    public static final String CHARSET="UTF-8";
    //候补单主页
    @GetMapping("/candidateReserveManager")
    public String getcandidateReserveManager(){
        return "manager/candidateReserveManager";
    }
    //获取所有预订数据
    @GetMapping("/{pageNo}")
    @ResponseBody
    public Result getCandidateReserveList(CandidateReserve candidateReserve){
        return null;
    }
    //获取当前用户的预订数据
    @GetMapping("/user/{pageNo}")
    @ResponseBody
    public Result getThisUserCandidateReserveList(@PathVariable("pageNo") Integer pageNo,@RequestParam("status") Integer status){
        CandidateReserve candidateReserve=new CandidateReserve();
        if (status!=null){
            candidateReserve.setStatus(status);
        }
        PageResult<CandidateReserve> pageResult = candidateReserveService.getUserCandidateReserveListPage(pageNo, 10, candidateReserve);
        return Result.success(pageResult);
    }

    //处理保存候补订单（前置处理）
    @PostMapping("/")
    @ResponseBody
    public Result saveCandidateReserve(@RequestBody ReverseDto reverseDto){
        Result re=null;
        try {
            //转化为预订单
            CandidateReserve candidateReserve = reverseDto.parseCandidateReserve();
            //校验是否允许候补
            candidateReserveService.verifyCandidate(candidateReserve);
            //获取球桌价格
            BallTable ballTable = ballTableService.getBallTableById(candidateReserve.getTableId());
            //总价
            BigDecimal payAmt=new BigDecimal(candidateReserve.getUseTime());
            payAmt = payAmt.multiply(ballTable.getPrice());
            //初始化商户交易号
            User user = commonService.getThisUserId();
            String outTradeNo = payService.getOutTradeNo(user.getGuid(), candidateReserve.getTableId());
            candidateReserve.setOutTradeNo(outTradeNo);
            candidateReserve.setPayAmt(payAmt);
            //保存到redis中，并设置过期时间
            boolean site = redisDao.addCandidateReserve(outTradeNo, candidateReserve, 900);
            //如果没开启支付宝支付，则直接入库
            if (!systemParamsProperty.isUseZfb()){
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
            }
            if (site){
                re=Result.success(outTradeNo);
            }
            else {
                re=Result.failed("加入缓存失败");
            }
        }
        catch (Exception e){
           re = Result.failed("",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //取消候补订单
    @DeleteMapping("/{guid}")
    @ResponseBody
    public Result cancelCandidateReserve(@PathVariable("guid") String guid){
        Result re=null;
        try {
            //先查询要取消候补的订单信息，用于退款操作
            CandidateReserve cancelCandidateReserve = candidateReserveService.getCandidateReserveById(guid);
            AlipayClient alipayClient = commonService.createAlipayClient();
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            //生成退款号
            String id = commonService.getId();
            JSONObject refundPayData = payService.createRefundPayData(cancelCandidateReserve.getTradeNo(), cancelCandidateReserve.getPayAmt(), id);
            request.setBizContent(refundPayData.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if(!systemParamsProperty.isUseZfb()||response.isSuccess()){
                logger.info("退款成功，退款金额:"+cancelCandidateReserve.getPayAmt());
                //删除候补单数据
                CandidateReserve condition=new CandidateReserve();
                condition.setGuid(guid);
                int i = candidateReserveService.deleteCandidateReserve(condition);
                if (i>0){
                    re=Result.success("成功","取消候补单成功");
                }
                else {
                    re=Result.failed("失败","删除候补单失败！请联系管理员");
                }
            }
            else {
                logger.info("退款调用失败!");
                throw new Exception("支付宝退款接口调用失败!请稍后再操作");
            }

        }
        catch (Exception e){
            re = Result.failed("失败",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //处理候补单支付请求
    @GetMapping("/processCreateCandidateReservePay/{redisKey}")
    public void processCreateCandidateReserve(HttpServletResponse httpServletResponse, @PathVariable("redisKey") String redisKey) throws JSONException {
        try {
            //从redis中获取预订单
            CandidateReserve candidateReserve = redisDao.getCandidateReserve(redisKey);
            logger.info("处理候补单支付请求");
            logger.info(candidateReserve.toString());
            //预订过期
            if (candidateReserve==null){
                httpServletResponse.setContentType( "text/html;charset="  + CHARSET);
                httpServletResponse.getWriter().write("未在指定时间内支付，请重新进行候补操作!"); //直接将完整的表单html输出到页面
                httpServletResponse.getWriter().flush();
                httpServletResponse.getWriter().close();
                return;
            }
            //调用支付宝客户端
            AlipayClient alipayClient = commonService.createAlipayClient();
            //设置候补异步通知url和同步重定向页面
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            //异步通知地址
            String NOTIFY_URL= ZfbCommonUtils.getZFBinfoValue("candidate_notify_url");
            //同步返回地址
            String RETURN_URL=ZfbCommonUtils.getZFBinfoValue("candidate_return_url");
            request.setNotifyUrl(NOTIFY_URL);//支付成功后，异步通知的url
            request.setReturnUrl(RETURN_URL);//支付完成以后，返回的url
            //生成订单数据
            JSONObject bizContent = payService.createCommonPayOrderData(candidateReserve.getPayAmt(), "候补预订", candidateReserve.getOutTradeNo());
            request.setBizContent(bizContent.toString());
            //将请求传入阿里支付客户端对象，并执行，返回一个response
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            //输出相应体的内容
            System.out.println(response.getBody());
            if(response.isSuccess()){
                logger.info("支付调用成功");
            } else {
                logger.info("支付调用失败");
            }
            httpServletResponse.setContentType( "text/html;charset="  + CHARSET);
            httpServletResponse.getWriter().write(response.getBody()); //直接将完整的表单html输出到页面
            httpServletResponse.getWriter().flush();
            httpServletResponse.getWriter().close();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

}
