package com.atsu.tabletennisreservation.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.atsu.tabletennisreservation.utils.ZfbCommonUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//球友匹配
@Controller
@RequestMapping("/playerMatching")
public class PlayerMatchingController {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private BallTableService ballTableService;
    @Resource
    private CommonService commonService;
    @Resource
    private ReverseService reverseService;
    @Resource
    private MatchInfoService matchInfoService;
    @Resource
    private PayService payService;
    @Resource
    private MessageService messageService;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    @GetMapping("/")
    public String getIndex(Model model, HttpServletRequest request){
        //设置页面全局变量
        ServletContext servletContext = request.getServletContext();
        Object baseUrl = servletContext.getAttribute("baseUrl");
        Object img_url = servletContext.getAttribute("img_url");
        model.addAttribute("baseUrl",baseUrl);
        model.addAttribute("img_url",img_url);
        return "playerMatching/playerMatching";
    }
    //当前用户发布的匹配信息页
    @GetMapping("/user")
    public String userMatchInfo(Model model, HttpServletRequest request){
        //设置页面全局变量
        ServletContext servletContext = request.getServletContext();
        Object baseUrl = servletContext.getAttribute("baseUrl");
        Object img_url = servletContext.getAttribute("img_url");
        model.addAttribute("baseUrl",baseUrl);
        model.addAttribute("img_url",img_url);
        return "playerMatching/userMatchingInfo";
    }
    //获取所有用户的匹配单数据
    @GetMapping("/getMacthList/{pageNo}")
    @ResponseBody
    public Result getMacthList(@PathVariable("pageNo") Integer pageNo, @RequestParam Map<String,String> map){
        //获取条件
        String code = map.get("code");
        String startDate = map.get("startDate");
        String status= map.get("status");
        MatchInfo matchInfo=new MatchInfo();
        if (!StringTool.isNull(code)){
            matchInfo.setTableCode(code);
        }
        if (!StringTool.isNull(startDate)){
            matchInfo.setProcessStartDate(startDate);
        }
        if (!StringTool.isNull(status)){
            matchInfo.setStatus(Integer.parseInt(status));
        }
        //封装主单用户id
        User user = commonService.getThisUserId();
        matchInfo.setBillUserId(user.getGuid());
        PageResult<MatchInfo> matchInfoListPage = matchInfoService.getUserSelectMatchInfoListPage(matchInfo, pageNo, 3);
        return Result.success(matchInfoListPage,"ok");
    }
    @GetMapping("/getThisUserMatchInfo/{guid}")
    @ResponseBody
    public Result getThisUserMatchInfoById(@PathVariable("guid") String guid){
        Result re=null;
        try {
            MatchInfo matchInfo = matchInfoService.getThisUserMatchInfoById(guid);
            if (matchInfo!=null){
                re=Result.success(matchInfo,"ok");
            }
            else {
                re=Result.failed(matchInfo,"未查询到数据!");
            }
        }
        catch (Exception e){
            re=Result.failed("系统异常",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //获取当前用户的匹配单数据
    @GetMapping("/getThisUserMacthList/{pageNo}")
    @ResponseBody
    public Result getThisUserMacthList(@PathVariable("pageNo") Integer pageNo, @RequestParam Map<String,String> map){
        //获取条件
        String code = map.get("code");
        String processStartDate = map.get("processStartDate");
        String status= map.get("status");
        MatchInfo matchInfo=new MatchInfo();
        if (!StringTool.isNull(code)){
            matchInfo.setTableCode(code);
        }
        if (!StringTool.isNull(processStartDate)){
            matchInfo.setProcessStartDate(processStartDate);
        }
        if (!StringTool.isNull(status)){
            matchInfo.setStatus(Integer.parseInt(status));
        }
        PageResult<MatchInfo> matchInfoPageResult = matchInfoService.getThisUserMatchInfoListPage(matchInfo,pageNo, 3);
        return Result.success(matchInfoPageResult,"ok");
    }
    //获取当前(主单)用户的匹配单数据
    @GetMapping("/getThisUserBillMatchInfoList")
    @ResponseBody
    public Result getThisUserBillMatchInfoList( @RequestParam Map<String,String> map){
        //获取条件
        String code = map.get("code");
        String startDate = map.get("startDate");
        String status= map.get("status");
        MatchInfo matchInfo=new MatchInfo();
        if (!StringTool.isNull(code)){
            matchInfo.setTableCode(code);
        }
        if (!StringTool.isNull(startDate)){
            matchInfo.setStartDate(startDate);
        }
        if (!StringTool.isNull(status)){
            matchInfo.setStatus(Integer.parseInt(status));
        }
        List<MatchInfo> matchInfoList = matchInfoService.getThisUserBillMatchInfoList(matchInfo);
        return Result.success(matchInfoList,"ok");
    }
    //获取当前(匹配)用户的匹配单数据
    @GetMapping("/getThisUserApplicationMatchInfoList")
    @ResponseBody
    public Result getThisUserApplicationMatchInfoList(@RequestParam Map<String,String> map){
        String code = map.get("code");
        String startDate = map.get("startDate");
        String status= map.get("status");
        MatchInfo matchInfo=new MatchInfo();
        if (!StringTool.isNull(code)){
            matchInfo.setTableCode(code);
        }
        if (!StringTool.isNull(startDate)){
            matchInfo.setStartDate(startDate);
        }
        if (!StringTool.isNull(status)){
            matchInfo.setStatus(Integer.parseInt(status));
        }
        List<MatchInfo> matchInfoList = matchInfoService.getThisUserApplicationMatchInfoList(matchInfo);
        return Result.success(matchInfoList,"ok");
    }
    //处理发布匹配
    @PutMapping("/createMatching")
    @ResponseBody
    public Result createMatching(@RequestBody Map map){
        System.out.println(map);
        List<String> ids = (List<String>) map.get("ids");
        //根据id查询出对应的预订单
        List<Reserve> reserveList = reverseService.getReserveListById(ids);
        //根据信息生成匹配单
        int i = matchInfoService.saveMatchInfoByReserveList(reserveList);
        boolean flag = reverseService.createMatching(ids);//同时将标志位设置为球友匹配状态
        if (flag){
            return Result.success("ok");
        }
        else {
            return  Result.failed("");
        }
    }
    @PutMapping("/updateThisUserMatchInfo")
    @ResponseBody
    public Result updateThisUserMatchInfo(@RequestBody MatchInfo matchInfo){
        Result re=null;
        try {
            //修改数据
            logger.info(matchInfo.toString());
            int i = matchInfoService.updateMatchInfoById(matchInfo);
            if (i>0){
                re=Result.success(matchInfo,"ok");
            }
            else {
                re=Result.failed(matchInfo,"修改失败!");
            }
        }
        catch (Exception e){
            re=Result.failed("系统异常",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //主单用户确认匹配请求处理
    @PostMapping("/billUserConfirmMatch")
    @ResponseBody
    public Result billUserConfirmMatch(@RequestBody Map map){
        Result re=null;
        List<String> ids = (List<String>) map.get("ids");
        if (ids==null&&ids.size()==0){
            return Result.success("");
        }
        //1、修改匹配单状态
        //2、生成匹配用户的预订单信息
        try {
            boolean site = matchInfoService.processBillUserConfirm(ids);
            if (site){
                re=Result.success("确认成功!");
            }
            else {
                re=Result.failed("操作失败，逻辑错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("操作失败",e.getMessage());
        } finally {
            return re;
        }
    }
    //主单用户拒绝匹配请求处理
    @PostMapping("/billUserRejectMatch")
    @ResponseBody
    public Result billUserRejectMatch(@RequestBody Map map){
        Result re=null;
        boolean site=true;
        List<String> ids = (List<String>) map.get("ids");
        if (ids==null||ids.size()==0){
            return Result.failed("未选择传入匹配单id数据！");
        }
        //1、修改匹配单状态
        //2、生成匹配用户的预订单信息
        try {
            //给主单用户退款，退款金额为匹配用户支付的金额
            //获取支付宝网关
            String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
            //获取支付宝应用id:app_id
            String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
            //获取应用私钥
            String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
            //获取支付宝公钥
            String ALIPAY_PUBLIC_KEY = ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
            String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
            String CHARSET = ZfbCommonUtils.getZFBinfoValue("charset");
            /** 初始化阿里支付客户端对象 **/
            AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            //获取所有的匹配单对象
            List<MatchInfo> matchInfoList = matchInfoService.getMatchInfoListByIds(ids);
            //退款给匹配的用户
            for (int i=0;i<matchInfoList.size();i++){
                MatchInfo matchInfo = matchInfoList.get(i);
                //生成退款号
                String id = commonService.getId();
                String trade_no=matchInfo.getMatchUserTradeNo();
                BigDecimal total_amount=matchInfo.getMatchUserNeedPayAmt();//退款金额为匹配用户所支付的金额
                JSONObject refundPayData = payService.createRefundPayData(trade_no, total_amount, id);
                request.setBizContent(refundPayData.toString());
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                //退款成功的情况，或者未启用支付宝的情况
                if(!systemParamsProperty.isUseZfb()||response.isSuccess()){
                    logger.info("主单用户已拒绝匹配用户成功，匹配用户【"+matchInfo.getBillUserName()+"】退款成功，退款金额:"+total_amount);
                    //主单记录
                    messageService.saveSystemMessage("您已拒绝来自用户【"+matchInfo.getMatchUserName()+"】的匹配请求，差价["+total_amount+"]已退回匹配用户的支付宝账户!",matchInfo.getBillUserId());
                    //匹配用户记录
                    messageService.saveSystemMessage("您的匹配请求已被主单用户["+matchInfo.getBillUserName()+"]拒绝,金额已退回您的账户",matchInfo.getMatchUserId());
                } else {
                    site=false;
                    logger.info("主单用户拒绝匹配用户【"+matchInfo.getMatchUserName()+"】退款调用失败!");
                }
            }
            if (site){
                //清除匹配单的匹配用户信息
                matchInfoService.processBillUserReject(ids);
                re=Result.success("拒绝成功!");
            }
            else {
                re=Result.failed("操作失败","操作失败，存在用户退款失败！请联系管理员处理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("操作失败",e.getMessage());
        } finally {
            return re;
        }
    }
    //匹配用户取消匹配请求处理（流程跟主单用户拒绝一样，只是保存的信息不一样）
    @PostMapping("/matchUserCancelMatch")
    @ResponseBody
    public Result matchUserCancelMatch(@RequestBody Map map){
        Result re=null;
        boolean site=true;
        List<String> ids = (List<String>) map.get("ids");
        if (ids==null||ids.size()==0){
            return Result.failed("未选择传入匹配单id数据！");
        }
        //1、修改匹配单状态
        //2、生成匹配用户的预订单信息
        try {
            //给主单用户退款，退款金额为匹配用户支付的金额
            //获取支付宝网关
            String URL= ZfbCommonUtils.getZFBinfoValue("open_api_domain");
            //获取支付宝应用id:app_id
            String APP_ID= ZfbCommonUtils.getZFBinfoValue("appid");
            //获取应用私钥
            String APP_PRIVATE_KEY=ZfbCommonUtils.getZFBinfoValue("private_key");
            //获取支付宝公钥
            String ALIPAY_PUBLIC_KEY = ZfbCommonUtils.getZFBinfoValue("alipay_public_key");
            String SIGN_TYPE = ZfbCommonUtils.getZFBinfoValue("sign_type");
            String CHARSET = ZfbCommonUtils.getZFBinfoValue("charset");
            /** 初始化阿里支付客户端对象 **/
            AlipayClient alipayClient = new DefaultAlipayClient(URL,APP_ID,APP_PRIVATE_KEY,"json","UTF-8",ALIPAY_PUBLIC_KEY,"RSA2");
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            //获取所有的匹配单对象
            List<MatchInfo> matchInfoList = matchInfoService.getMatchInfoListByIds(ids);
            //退款给匹配的用户
            for (int i=0;i<matchInfoList.size();i++){
                MatchInfo matchInfo = matchInfoList.get(i);
                //生成退款号
                String id = commonService.getId();
                String trade_no=matchInfo.getMatchUserTradeNo();
                BigDecimal total_amount=matchInfo.getMatchUserNeedPayAmt();//退款金额为匹配用户所支付的金额
                JSONObject refundPayData = payService.createRefundPayData(trade_no, total_amount, id);
                request.setBizContent(refundPayData.toString());
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                //退款成功的情况
                if(!systemParamsProperty.isUseZfb()||response.isSuccess()){
                    logger.info("取消匹配单成功，匹配用户【"+matchInfo.getBillUserName()+"】退款成功，退款金额:"+total_amount);
                    //匹配用户记录
                    messageService.saveSystemMessage("您的匹配请求已撤回,金额【"+total_amount+"】已退回您的账户",matchInfo.getMatchUserId());
                } else {
                    site=false;
                    logger.info("取消匹配单退款调用失败!");
                }
            }
            if (site){
                //清除匹配单的匹配用户信息
                matchInfoService.processBillUserReject(ids);
                re=Result.success("取消成功!");
            }
            else {
                re=Result.failed("操作失败","操作失败，存在用户退款失败！请联系管理员处理");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("操作失败",e.getMessage());
        } finally {
            return re;
        }
    }
    //删除匹配单信息
    @DeleteMapping("/matchInfo/{guid}")
    @ResponseBody
    public Result processDeleteMatchInfo(@PathVariable("guid") String guid){
        Result re=null;
        try {
            boolean site = matchInfoService.processDeleteMatchInfo(guid);
            if (site){
                re=Result.success("ok","操作成功");
            }
            else {
                re=Result.failed("失败","操作失败，请联系系统管理员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            re=Result.failed("失败",e.getMessage());
        }
        finally {
            return re;
        }

    }
}
