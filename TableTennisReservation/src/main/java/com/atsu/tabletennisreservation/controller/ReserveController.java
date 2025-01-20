package com.atsu.tabletennisreservation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.atsu.tabletennisreservation.dto.MailDTO;
import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.dto.ReverseDto;
import com.atsu.tabletennisreservation.exception.ReserveException;
import com.atsu.tabletennisreservation.mapper.BallTableMapper;
import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.propertyBean.PayPropertyBean;
import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.utils.ZfbCommonUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class ReserveController {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private BallTableMapper ballTableMapper;
    @Resource
    private BallTableService ballTableService;
    @Resource
    private CommonService commonService;
    @Resource
    private ReverseService reverseService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private PayService payService;
    @Resource
    private MatchInfoService matchInfoService;
    @Resource
    private MessageService messageService;
    @Resource
    private MailService mailService;
    @Resource
    private UserService userService;
    @Resource
    private PayPropertyBean payPropertyBean;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    @GetMapping("/dashboard")
    public String dashboard(Map map,@RequestParam(value = "pageNo",required = false) Integer pageNo){
        if (pageNo==null){
            pageNo=1;
        }
        //查询信息
        List<BallTable> ballTableList = ballTableMapper.getBallTableList(1);
        PageResult<BallTable> ballTableListPage = ballTableService.getBallTableListPage(pageNo, 3);
        map.put("result",ballTableListPage);
        map.put("dataList",ballTableList);
        return "dashboard/dashboard";
    }
    //预订界面
    @GetMapping("/reverse/{ballTableId}")
    public String reverse(@PathVariable("ballTableId") String ballTableId,Map model){
        //根据球桌id信息加载数据
        //获取球桌信息
        BallTable ballTableById = ballTableService.getBallTableById(ballTableId);
        model.put("ballTable",ballTableById);
        //获取球桌预订信息
        //生成未来一周的时间展示信息
        List<String> futureDataStrList = commonService.createFutureDataStrList(7);
        model.put("dateList",futureDataStrList);
        //当天允许选择的开始时间(24小时制)
        List<String> startDateTimeList=new ArrayList<>();
        startDateTimeList.add("9");
        startDateTimeList.add("10");
        startDateTimeList.add("11");
        startDateTimeList.add("12");
        startDateTimeList.add("13");
        startDateTimeList.add("14");
        startDateTimeList.add("15");
        startDateTimeList.add("16");
        startDateTimeList.add("17");
        startDateTimeList.add("18");
        startDateTimeList.add("19");
        startDateTimeList.add("20");
        model.put("startDateTimeList",startDateTimeList);
        //设置停止营业时间
        model.put("closeTime","21");
        //设置图片路径
        return "dashboard/reverse";
    }
    //预处理预订信息，先缓存到redis中锁定订单,并响应redisKey给前端
    @PostMapping("/reverse/")
    @ResponseBody
    public Result saveReverse(@RequestBody ReverseDto reverseDto, Model model, HttpServletRequest request){
        Result re=null;
        User user = commonService.getThisUserId();
        System.out.println(reverseDto);
        Reserve reserve = reverseDto.parseReserve();
        try {
            boolean site = reverseService.processReserve(reserve);
            if (site){
                //响应保存的redisKey,这里是使用商户订单号作为key的
                re=Result.success(reserve.getOutTradeNo());
                commonService.sendMsg(user.getGuid(),"球桌锁定成功!请十五分钟内付款");
            }
            else {
                re=Result.failed("失败");
            }
        } catch (Exception e) {
            //转化异常
            if (e instanceof ReserveException){
                ReserveException reserveException= (ReserveException) e;
                re=Result.failed(reserveException.getCode(), reserveException.getMsg());
            }
            else {
                re=Result.failed("",e.getMessage());
            }
            commonService.sendMsg(user.getGuid(),e.getMessage());

        }finally {
            return re;
        }
    }
    //支付宝支付成功回调
    @GetMapping("/processPaySuccessReverse/")
    @ResponseBody
    public Result processPaySuccessReverse( Model model){
        //获取商户订单号
        model.getAttribute("");
        logger.info("支付宝支付成功回调方法!");
        logger.info(model.toString());
        return  Result.success("回调");
    }
    //支付宝支付成功异步通知
    @PostMapping("/processPaySuccessNotify/")
    @ResponseBody
    public Result processPaySuccessNotify(@RequestParam Map<String,String> paramsMap) throws JSONException, AlipayApiException {
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
                logger.info("支付成功，开始生成预订单到数据库!");
                //获取商户订单号
                String out_trade_no = paramsMap.get("out_trade_no");
                String trade_no = paramsMap.get("trade_no");
                String total_amount = paramsMap.get("total_amount");
                //根据订单号，去redis中获取订单数据
                List<String> redisKeyList=new ArrayList<>();
                redisKeyList.add(out_trade_no);
                List<Reserve> reserveList = redisDao.getReserveList(redisKeyList);
                //如果获取到了数据，将内容入库处理
                if (reserveList!=null&&reserveList.size()==1){
                    Reserve reserve = reserveList.get(0);
                    //补全商户订单号和交易号
                    reserve.setOutTradeNo(out_trade_no);
                    reserve.setTradeNo(trade_no);//交易号
                    int i = reverseService.saveReserve(reserve);
                    if (i>0){
                        //清除redis中锁定的key
                        redisDao.cleanReserveInfo(out_trade_no);
                        logger.info("预订单入库成功!");
                        logger.info(reserve.toString());
                        MailDTO mailDTO=new MailDTO();
                        mailDTO.setTitle("预订成功");
                        //推送邮件（如果用户存在邮箱）
                        String targetEmail = userService.getUserEmail(reserve.getUserId());
                        if (!StringTool.isNull(targetEmail)){
                            mailDTO.setTargetMail(targetEmail);
                            mailDTO.setContent("您已成功预订球桌，使用时间为"+reserve.getStartDate()+"时长(小时)："+reserve.getUseTime()+"交易金额【"+total_amount+"】元");
                            mailService.send(mailDTO);
                        }
                    }
                    else {
                        logger.info("预订单入库失败!");
                    }
                }
                //如果没获取到：说明redis过期了，进行退款操作
                else {
                    logger.info("锁定预订时间过期，将进行退款处理.......");
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

        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
        }
        return  Result.success("回调");
    }
    //查询当前用户预订信息
    @GetMapping("/reverse")
    @ResponseBody
    public Result getThisUserReverseInfo(Integer status){
        Reserve reserve=new Reserve();
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        if (status!=null){
            reserve.setReserveStatus(status);
        }
        List<Reserve> reserveList = reverseService.getReserveWrapperList(reserve);
        //将内容转化为包装类
        return Result.success(reserveList);
    }
    //查询当前用户预订信息(分页)
    @GetMapping("/reversePage/{pageNo}")
    @ResponseBody
    public Result getThisUserReverseInfoPage(@PathVariable("pageNo") Integer pageNo,@RequestParam Map<String,String> map){
        String status = map.get("status");
        Reserve reserve=new Reserve();
        if (!StringTool.isNull(status)){
            reserve.setReserveStatus(Integer.parseInt(status));
        }
        PageResult<Reserve> pageResult = reverseService.getThisUserReservePage(pageNo, 10, reserve);
        //将内容转化为包装类
        return Result.success(pageResult);
    }
    //获取所有的订单信息(废弃)
    @GetMapping("/reverse/all")
    @ResponseBody
    public Result getAllUserReverseInfo(Integer status){
        Reserve reserve=new Reserve();
        if (status!=null){
            reserve.setReserveStatus(status);
        }
        List<Reserve> reserveList = reverseService.getReserveWrapperList(reserve);
        //将内容转化为包装类
        return Result.success(reserveList);
    }
    //删除订单订单(直接删除数据库记录)
    @DeleteMapping("/reverse/{guid}")
    @ResponseBody
    public Result cancelReverseInfo(@PathVariable("guid") String guid){
        Result re=null;
        try {
            boolean site = reverseService.processDeleteReserve(guid);
            if (site){
                re=Result.success("ok");
            }
            else {
                re=Result.failed("失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("操作失败",e.getMessage());
        }
        finally {
            return re;
        }

    }
    //用于渲染前端可视化，展示当前日期的球桌可预订时间
    @GetMapping("/reverse/reserveDate/{tableCode}/{processDate}")
    @ResponseBody
    public Result getTableReserveDate(@PathVariable("tableCode") String tableCode,@PathVariable("processDate") String processDate){
        ReserveDate reserveDate = reverseService.getTableReserveDate(tableCode, processDate);
        return Result.success(reserveDate);
    }
    //获取当前用户进行中的订单数据分页
    @GetMapping("/reverse/getProgressReservePage/{pageNo}")
    @ResponseBody
    public Result getProgressReservePage(@PathVariable("pageNo") Integer pageNo,@RequestParam Map<String,String> map){
        //设置条件
        Reserve reserve=new Reserve();
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        //条件查询
        if (!StringTool.isNull(map.get("processStartDate"))){
            reserve.setProcessStartDate(map.get("processStartDate"));
        }
        PageResult<Reserve> pageResult = reverseService.getProgressReservePage(pageNo, 10, reserve);
        return Result.success(pageResult);
    }
    //查询待确认的订单信息
    @GetMapping("/reverse/getWaitConfirmReservePage/{pageNo}")
    @ResponseBody
    public Result getWaitConfirmReservePage(@PathVariable("pageNo") Integer pageNo,@RequestParam Map<String,String> map){
        //设置条件
        Reserve reserve=new Reserve();
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        //条件查询
        if (!StringTool.isNull(map.get("processStartDate"))){
            reserve.setProcessStartDate(map.get("processStartDate"));
        }
        PageResult<Reserve> pageResult = reverseService.getWaitConfirmReservePage(pageNo, 10, reserve);
        return Result.success(pageResult);
    }
    //查询过期订单数据
    @GetMapping("/reverse/getExpiredReservePage/{pageNo}")
    @ResponseBody
    public Result getExpiredReservePage(@PathVariable("pageNo") Integer pageNo,@RequestParam Map<String,String> map){
        //设置条件
        Reserve reserve=new Reserve();
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        //条件查询
        if (!StringTool.isNull(map.get("processStartDate"))){
            reserve.setProcessStartDate(map.get("processStartDate"));
        }
        PageResult<Reserve> pageResult = reverseService.getExpiredReservePage(pageNo, 10, reserve);
        return Result.success(pageResult);
    }
    //处理用户确认预订单
    @PutMapping("/reverse/processConfirmReserve/{guid}")
    @ResponseBody
    public Result processConfirmReserve(@PathVariable("guid") String guid){
        Result re=null;
        try {
            Reserve reserve = reverseService.getReserveById(guid);
            //校验订单是否允许确认,如果校验不通过会抛异常
            reverseService.verifyConfirmReserve(reserve);
            //1、如果是拼单用户的，先进行匹配单的退款操作
            MatchInfo matchInfo = null;
            MatchInfo condition=new MatchInfo();
            condition.setBillReserveId(guid);
            List<MatchInfo> matchInfoList = matchInfoService.getMatchInfoList(condition);
            if (matchInfoList!=null&&matchInfoList.size()==1){
                matchInfo=matchInfoList.get(0);
            }
            //存在匹配交易信息,给主单用户退款
            if (matchInfo!=null&&!StringTool.isNull(matchInfo.getMatchUserTradeNo())){
                //获取支付宝网关
                String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
                //获取支付宝应用id:app_id
                String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
                //获取应用私钥
                String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
                //获取支付宝公钥
                String ALIPAY_PUBLIC_KEY=ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
                //加签方式
                String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
                //支付宝客户端
                AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
                //构造支付宝请求
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                //编码
                String CHARSET=ZfbCommonUtils.getZFBinfoValue("charset");
                String out_trade_no = matchInfo.getBillUserOutTradeNo();
                String trade_no = matchInfo.getBillUserTradeNo();
                BigDecimal total_amount = matchInfo.getMatchUserNeedPayAmt();
                JSONObject refundPayData = payService.createRefundPayData(trade_no, total_amount, commonService.getId());
                request.setBizContent(refundPayData.toString());
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                if(response.isSuccess()){
                    logger.info("确认订单:退款匹配差价成功，退款金额:"+total_amount);
                    //退款完成以后，进行更改状态
                    boolean site = reverseService.processConfirmReserve(guid);
                    if (site){
                        re=Result.success("ok","确认订单完成");
                    }
                    else {
                        re=Result.failed("失败","无法更新数据库状态");
                    }
                }
                else {
                    logger.info("确认订单:退款匹配差价，调用失败!");
                    re=Result.failed("","确认订单:退款匹配差价，调用失败!请稍后再试");
                }
            }
            //不存在匹配单，直接修改状态
            else {
                boolean site = reverseService.processConfirmReserve(guid);
                if (site){
                    re=Result.success("ok","确认订单完成");
                }
                else {
                    re=Result.failed("失败","无法更新数据库状态");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("失败",e.getMessage());
        } finally {
            return re;
        }
    }
    //处理用户取消预订单
    @DeleteMapping("/reverse/processCancelOrder/{guid}")
    @ResponseBody
    public Result processCancelOrder(@PathVariable("guid") String guid){
        /*
        ● 退款给拼单用户
        ● 重置匹配单信息和状态(如果是拼单)
        ● 推送信息给主单用户
        */
        Result re=null;
        Reserve reserve = reverseService.getReserveById(guid);
        //判断该预订单是否为匹配单用户的，增加校验
//        if (!StringTool.isNull(reserve.getMatchUserMatchInfoId())){
//            //获取当前系统时间
//            String nowDateStr = commonService.getNowDateStr();
//            String startDate = reserve.getStartDate();
//            if (nowDateStr.substring(0,10).equals(startDate.substring(0,10))){
//                return  Result.failed("失败","球友匹配的订单，预订日期当日不允许取消订单!");
//            }
//        }

        try {
            //校验是否允许取消订单
            reverseService.verifyCancelReserve(reserve);
            AlipayClient alipayClient = commonService.createAlipayClient();
            //构造支付宝请求
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject refundPayData = payService.createRefundPayData(reserve.getTradeNo(), reserve.getPayAmt(), commonService.getId());
            request.setBizContent(refundPayData.toString());
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            boolean success = response.isSuccess();
//            success=true;//暂时去除退款操作
            if(!systemParamsProperty.isUseZfb()||success){
                logger.info("取消订单：退款成功！金额["+reserve.getPayAmt()+"]元");
                //业务处理取消预订
                boolean site = reverseService.processCancelReserve(guid);
                if (site){
                    //推送消息给当前用户
                    messageService.saveSystemMessage("您已成功取消订单，退款金额为["+reserve.getPayAmt()+"]",reserve.getUserId());
                    re=Result.success("操作成功!");
                }
                else {
                    re=Result.failed("失败","拼单用户取消订单：重置匹配单和预订单失败！");
                }
            }
            else {
                logger.info("取消订单：退款失败");
                re=Result.failed("失败","取消订单：退款失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("失败",e.getMessage());

        }
        finally {
            return re;
        }
    }
    //获取当前用户可发布匹配的预订单
    @GetMapping("/reverse/getUserCanMatchReserve")
    @ResponseBody
    public Result getUserCanMatchReserve(){
        List<Reserve> list = reverseService.getCanMatchReserve(new Reserve());
        return Result.success(list);
    }
    //获取当前用户待支付的预订单
    @GetMapping("/reverse/getUserWaitPayOrder/{pageNo}")
    @ResponseBody
    public Result getUserWaitPayOrder(@PathVariable("pageNo") Integer pageNo){
        List<Reserve> list = redisDao.getThisUserReserveList();
        //直接全量展示即可(一般数据量不会很大)
        PageResult<Reserve> pageResult=new PageResult<>();
        pageResult.setDataList(list);
        pageResult.setPageNo(1);
        pageResult.setNextPageNo(1);
        pageResult.setPrePageNo(1);
        pageResult.setPageSize(10);
        pageResult.setTotalPages(1);
        List<Integer> pageNoList=new ArrayList<>();
        pageNoList.add(1);
        pageResult.setPageNoList(pageNoList);
        return Result.success(pageResult);
    }
    //处理取消待支付订单
    @DeleteMapping("/reverse/processCancelUserWaitPayOrder/{redisKey}")
    @ResponseBody
    public Result processCancelUserWaitPayOrder(@PathVariable("redisKey") String redisKey){
       //清除redis的信息即可
        reverseService.processCancelUserWaitPayOrder(redisKey);
        return Result.success("ok");
    }
}
