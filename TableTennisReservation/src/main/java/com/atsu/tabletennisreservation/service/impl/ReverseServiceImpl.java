package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.configuration.BaseDataLoader;
import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.dto.ReverseDto;
import com.atsu.tabletennisreservation.enums.ReserveStatus;
import com.atsu.tabletennisreservation.exception.ReserveException;
import com.atsu.tabletennisreservation.mapper.ReserveMapper;
import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.*;
import com.atsu.tabletennisreservation.thread.CandidateReserveThread;
import com.atsu.tabletennisreservation.utils.MessageUtil;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ReverseServiceImpl implements ReverseService {
    public static final Integer TIME_OUT_SECOND=900;//redis保存的过期时间(秒)
    @Resource
    private CommonService commonService;
    @Resource
    private   ReserveMapper reserveMapper;
    @Resource
    private CandidateReserveService candidateReserveService;
    @Resource
    private MessageService messageService;
    @Resource
    private MatchInfoService matchInfoService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    @Resource
    private ThreadPoolTaskExecutor threadPool;//线程池
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    public int saveReserve(Reserve reserve) {
        //补全信息
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        String guid = commonService.getId();
        reserve.setCreateDate(nowDateStr);
        reserve.setUpdateDate(nowDateStr);
        if (StringTool.isNull(reserve.getGuid())){
            reserve.setGuid(guid);
        }
        //为空时才设置，防止与候补单冲突
        if (StringTool.isNull(reserve.getUserId())){
            reserve.setUserId(user.getGuid());
        }
        if (StringTool.isNull(reserve.getUserName())){
            reserve.setUserName(user.getUserName());
        }
        if (StringTool.isNull(reserve.getUpdateUser())){
            reserve.setUpdateUser(user.getGuid());
        }
        if (StringTool.isNull(reserve.getCreateUser())){
            reserve.setCreateUser(user.getGuid());
        }
        //状态,已付款状态
        reserve.setReserveStatus(ReserveStatus.RESERVE_SUCCESS.getCode());
        //先更新预订时间队列
        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        String date = reserve.getStartDate().substring(0, 10);
        String key=reserve.getTableCode()+"_"+date;
        ReserveDate reserveDate = reserveDateListMap.get(key);
        boolean site=false;
        if (reserveDate!=null){
            //更新信息
            int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer useTime = reserve.getUseTime();
            site = reserveDate.reserveProcess(hour, useTime);
            if (!site){
                return 0;
            }
        }
        else {
            //如果不存在当前时间的队列，则新建一个
            ReserveDate newReserveDate=new ReserveDate();
            newReserveDate.init(reserve);
            //存入map中
            reserveDateListMap.put(reserve.getReserveDateKey(),newReserveDate);
            site=true;
        }
        int i = reserveMapper.saveReserve(reserve);//保存;
        if (i>0){
            //保存消息
            messageService.saveSystemMessage(MessageUtil.getMessageText("ydcg",reserve),reserve.getUserId());
        }
        return i;
    }

    @Override
    public int saveReserve(ReverseDto reverseDto) {
        //转化为Reverse

        return 0;
    }

    @Override
    public List<Reserve> getReserveListById(List<String> ids) {
        return reserveMapper.getReserveListById(ids);
    }

    @Override
    public List<Reserve> getReserveList(Reserve reserve) {
        return reserveMapper.getReserveList(reserve);
    }

    @Override
    public List<Reserve> getReserveWrapperList(Reserve reserve) {
        List<Reserve> reserveList = reserveMapper.getReserveList(reserve);
        //封装成包装类
        for (int i=0;i<reserveList.size();i++){
            Reserve reserveData= reserveList.get(i);
            //初始化附加属性
            ReserveWrapper reserveWrapper=new ReserveWrapper(reserveData);
            try {
                //复制基本属性
                BeanUtils.copyProperties(reserveWrapper, reserveData);
                reserveList.set(i,reserveWrapper);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        return reserveList;
    }

    @Override
    public Reserve getReserve(Reserve reserve) {
        return reserveMapper.getReserve(reserve);
    }

    @Override
    public int deleteReserveById(String guid) {
        return reserveMapper.deleteReserveById(guid);
    }

    @Override
    public List<String> getReserveDateInfo(String dateStr) {
        //获取当前日期数据
        List<Reserve> list = reserveMapper.getReserveByDate(dateStr);
        //构造
        return null;
    }

    @Override
    public List<Reserve> getReserveByDateList(List dateList) {
        return reserveMapper.getReserveByDateList(dateList);
    }

    //取消订单
    @Override
    public boolean cancelReserveById(String guid) {
        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        //1、获取详细的订单信息
        Reserve reserve = getReserveById(guid);
        ReserveDate reserveDate = reserveDateListMap.get(reserve.getReserveDateKey());
        //2、判断订单是否允许取消

        //3、回退预订日期队列信息
        if (reserveDate!=null){
            int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer useTime = reserve.getUseTime();
            reserveDate.cancelReserveProcess(hour,useTime);
        }
        //4、删除预订单信息(或者设置标志位)
        int i = reserveMapper.deleteReserveById(guid);
        //候补操作(全量)
        if (reserveDate!=null){
            candidateReserveService.processCandidateReserve(reserve);
        }
        return i>0;
    }

    @Override
    public Reserve getReserveById(String guid) {
        Reserve reserve=new Reserve();
        reserve.setGuid(guid);
        List<Reserve> reserveList = reserveMapper.getReserveList(reserve);
        if (reserveList!=null&&reserveList.size()>0){
            return reserveList.get(0);
        }
        return null;
    }

    @Override
    public PageResult<Reserve> getReserveMatchtListPage(int pageNo, int pageSize,String code,String startDate) {
        //分页器
        PageHelper.startPage(pageNo,pageSize);
        Reserve reserve = new Reserve();
        //设置预订状态(待匹配状态)
        reserve.setReserveStatus(ReserveStatus.RESERVE_MATCHING.getCode());
        //判空
        if (!StringTool.isNull(code)){
            reserve.setTableCode(code);//球桌编号
        }
        if (!StringTool.isNull(startDate)){
            reserve.setProcessStartDate(startDate);//根据年月日搜索
        }
        List<Reserve> reserveList = reserveMapper.getReserveList(reserve);
        PageInfo<Reserve> pageInfo=new PageInfo<>(reserveList);
        //构造dto
        PageResult<Reserve> pageResult = new PageResult<Reserve>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<Reserve> getThisUserReserveMatchtListPage(int pageNo, int pageSize, String code, String startDate) {
        //分页器
        PageHelper.startPage(pageNo,pageSize);
        Reserve reserve = new Reserve();
        //设置预订状态(待匹配状态)
        reserve.setReserveStatus(ReserveStatus.RESERVE_MATCHING.getCode());
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        //判空
        if (!StringTool.isNull(code)){
            reserve.setTableCode(code);//球桌编号
        }
        if (!StringTool.isNull(startDate)){
            reserve.setProcessStartDate(startDate);//根据年月日搜索
        }
        List<Reserve> reserveList = reserveMapper.getReserveList(reserve);
        PageInfo<Reserve> pageInfo=new PageInfo<>(reserveList);
        //构造dto
        PageResult<Reserve> pageResult = new PageResult<Reserve>(pageInfo);
        return pageResult;
    }

    @Override
    public boolean createMatching(List orderIdList) {
        //批量更新状态
        int i = reserveMapper.updateBatchStatus(ReserveStatus.RESERVE_MATCHING.getCode(), orderIdList);
        return i>0;
    }
    //获取前台展示的预订时间明细
    @Override
    public ReserveDate getTableReserveDate(String tableCode, String process_date) {
        ReserveDate re = new ReserveDate();
        //获取队列key
        String key = Reserve.getReserveDateKey(tableCode, process_date);
        //获取预订队列map
        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        ReserveDate reserveDate = reserveDateListMap.get(key);
        if (reserveDate!=null){
            re= reserveDate;
        }
        //获取redis中的锁定日期信息
        //1、获取redis中的锁定预订单
        ReserveDate redisReserveDate = null;
        Map<String, ReserveDate> redisReserveDateListMap=null;
        Set<String> redisKeySet = ReserveDate.getRedisKeySet();
        if (redisKeySet.size()>0){
            String[] keys=new String[redisKeySet.size()];
            keys = redisKeySet.toArray(keys);
            List<Reserve> reserveList = redisDao.getReserveList(Arrays.asList(keys));
            //生成预订时间列表映射
            redisReserveDateListMap = BaseDataLoader.createReserveDateListMap(reserveList);
            redisReserveDate = redisReserveDateListMap.get(key);
        }
        if (redisReserveDate!=null){
            //如果redis中存在该日期该球桌的锁定数据，则合并,合并到redis中查询的内容，这样不影响本地
            List<Integer> dateList = redisReserveDate.getDateList();
            List<Integer> localDateList = re.getDateList();
            if (dateList!=null&&dateList.size()>0){
                for (int i=0;i<localDateList.size();i++){
                    if (localDateList.get(i)!=0){
                        dateList.set(i,localDateList.get(i));
                    }
                }
                re=redisReserveDate;//合并后的内容作为结果
            }
        }
        //如果process_date正好是当天，那么获取当前系统的小时数，小于等于当前小时的信息都不允许预订
        String nowDateStr = commonService.getNowDateStr();
        String dateStr = nowDateStr.substring(0, 10);
        String hourStr = nowDateStr.substring(11, 13);
        if (dateStr.equals(process_date)){
            int index=Integer.parseInt(hourStr);
            List<Integer> dateList = re.getDateList();
            for (int i=0;i<=index;i++){
               dateList.set(i,1);
            }
        }
        //初始化re
        re.setTableCode(tableCode);
        re.setDateStr(process_date);
        return re;
    }
    //处理预订请求
    @Override
    public boolean processReserve(Reserve reserve) throws Exception {
        //补全信息
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        String guid = commonService.getId();
        reserve.setGuid(guid);
        reserve.setCreateDate(nowDateStr);
        reserve.setUpdateDate(nowDateStr);
        //为空时才设置，防止与候补单冲突
        if (reserve.getUserId()==null||reserve.getUserId().length()==0){
            reserve.setUserId(user.getGuid());
        }
        if (reserve.getUserName()==null||reserve.getUserName().length()==0){
            reserve.setUserName(user.getUserName());
        }
        reserve.setUpdateUser(user.getGuid());
        reserve.setCreateUser(user.getGuid());
        //状态,为待付款状态
        reserve.setReserveStatus(ReserveStatus.RESERVE_DUE.getCode());
        //校验是否允许预订
        //系统参数控制是否进行校验
        if (systemParamsProperty.isVerifyReserve()){
            boolean site = verifyCanReserve(reserve);
            if (!site)
                return false;
        }
        //如果都通过了预订校验，将预订单存入redis中
        //生成商户订单号
        String outTradeNo = commonService.createOutTradeNo();
        reserve.setOutTradeNo(outTradeNo);
        //系统参数：如果不开启支付功能，则直接保存入库
        if (!systemParamsProperty.isUseZfb()){
            saveReserve(reserve);
            return true;
        }
        String redisKey =outTradeNo;//使用商户订单号作为redisKey，用来唯一标记本次预订单
        //保存
        redisDao.addReserve(redisKey,reserve,TIME_OUT_SECOND);//过期时间，15分钟
        //加入keySet
        ReserveDate.getRedisKeySet().add(redisKey);
        logger.info("用户["+user.getUserName()+"]预订成功，保存到redis中，使用时间:"+reserve.getStartDate()
                +"    时长（小时）："+reserve.getUseTime());
        return true;
    }

    @Override
    public boolean processPaySucessSaveReserve(Reserve reserve) {
        //设置为已付款状态
        reserve.setReserveStatus(ReserveStatus.RESERVE_SUCCESS.getCode());
        //保存到数据库
        int i = reserveMapper.saveReserve(reserve);

        return false;
    }

    @Override
    public int saveReserveBatch(List<Reserve> reserveList) {
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        //补充公共信息
        for (int i=0;i<reserveList.size();i++){
            Reserve reserve = reserveList.get(i);
            if (StringTool.isNull(reserve.getGuid())){
                reserve.setGuid(commonService.getId());
            }
            if (StringTool.isNull(reserve.getCreateUser())){
                reserve.setCreateUser(user.getGuid());
            }
            if (StringTool.isNull(reserve.getUpdateUser())){
                reserve.setUpdateUser(user.getGuid());
            }
            if (StringTool.isNull(reserve.getCreateDate())){
                reserve.setCreateDate(nowDateStr);
            }
            if (StringTool.isNull(reserve.getUpdateDate())){
                reserve.setUpdateDate(nowDateStr);
            }
            if (StringTool.isNull(reserve.getUserId())){
                reserve.setUserId(user.getGuid());
            }
            if (StringTool.isNull(reserve.getUserName())){
                reserve.setUserId(user.getUserName());
            }
        }
        return reserveMapper.saveReserveBatch(reserveList);
    }

    @Override
    public List<Reserve> getReserveListByMatchInfoId(List<String> matchInfoIdList) {
        return reserveMapper.getReserveListByMatchInfoId(matchInfoIdList);
    }

    @Override
    public int deleteMatchingReserveBatch(List<String> matchidList) {
        return reserveMapper.deleteMatchingReserveBatch(matchidList);
    }
    //分页查询订单数据
    @Override
    public PageResult<Reserve> getReservePage(int pageNo, int pageSize, Reserve condition) {
        PageHelper.startPage(pageNo,pageSize);//开启分页
        List<Reserve> reserveList = getReserveList(condition);
        PageInfo<Reserve> pageInfo=new PageInfo<>(reserveList);
        //转化成分页结果
        PageResult<Reserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }
    //获取当前用户的分页信息
    @Override
    public PageResult<Reserve> getThisUserReservePage(int pageNo, int pageSize, Reserve condition) {
        User user = commonService.getThisUserId();
        condition.setUserId(user.getGuid());//设置当前用户
        PageResult<Reserve> reservePage = this.getReservePage(pageNo, pageSize, condition);
        return reservePage;
    }

    @Override
    public List<Reserve> getProgressReserve(Reserve reserve) {
        return reserveMapper.getProgressReserve(reserve);
    }

    @Override
    public PageResult<Reserve> getProgressReservePage(int pageNo, int pageSize, Reserve condition) {
        PageHelper.startPage(pageNo,pageSize);//开启分页
        List<Reserve> list = reserveMapper.getProgressReserve(condition);
        PageInfo<Reserve> pageInfo=new PageInfo<>(list);
        PageResult<Reserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<Reserve> getWaitConfirmReservePage(int pageNo, int pageSize, Reserve reserve) {
        PageHelper.startPage(pageNo,pageSize);
        List<Reserve> list = reserveMapper.getWaitConfirmReserve(reserve);
        PageInfo<Reserve> pageInfo=new PageInfo<>(list);
        PageResult<Reserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<Reserve> getExpiredReservePage(int pageNo, int pageSize, Reserve reserve) {
        PageHelper.startPage(pageNo,pageSize);
        List<Reserve> list = reserveMapper.getExpiredReserve(reserve);
        PageInfo<Reserve> pageInfo=new PageInfo<>(list);
        PageResult<Reserve> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }
    //处理用户确认预订单
    @Override
    public boolean processConfirmReserve(String guid) throws Exception {
        //1、校验订单是预订时间是否大于或者等于当前系统日期
        boolean site=false;
        Reserve reserve = getReserveById(guid);
        String time = commonService.getNowDateStr().substring(0, 10);
        if (verifyConfirmReserve(reserve)){
            //2、修改订单状态
            List<String> idList=new ArrayList<>();
            idList.add(guid);
            int i = reserveMapper.updateBatchStatus(ReserveStatus.RESERVE_USED.getCode(), idList);
            site=i>0;
        }
        return site;
    }
    //处理取消订单业务（退款成功以后调用）
    @Override
    public boolean processCancelReserve(String guid) throws Exception {
        //先获取主单
        Reserve reserve = getReserveById(guid);

        Map<String, ReserveDate> reserveDateListMap = commonService.getReserveDateListMap();
        ReserveDate reserveDate = reserveDateListMap.get(reserve.getReserveDateKey());
        //2、判断订单是否允许取消(如果该预订单还存在匹配信息，则不允许取消)
        MatchInfo info = matchInfoService.getMatchInfoByReserveId(guid);
        if (info!=null){
            throw new Exception("该订单存在匹配单，请删除匹配单以后再取消！");
        }
        //3、回退预订日期队列信息
        //20240417:只对主单用户的预订单进行回退预订时间信息（避免球友匹配用户主动取消订单时，同时把主单用户的时间给取消了）
        if (reserveDate!=null&&StringTool.isNull(reserve.getMatchUserMatchInfoId())){
            int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer useTime = reserve.getUseTime();
            reserveDate.cancelReserveProcess(hour,useTime);
        }
        //1、设置订单状态为【订单取消】
        List<String> ids = new ArrayList<>();
        ids.add(guid);
        reserveMapper.updateBatchStatus(ReserveStatus.RESERVE_CANCEL.getCode(),ids);
        //2、如果是匹配的预订单，则需要重置匹配单信息，和将订单对匹配的引用置空
        if (!StringTool.isNull(reserve.getMatchUserMatchInfoId())){
            //获取匹配单信息
            MatchInfo matchInfo = matchInfoService.getMatchInfoById(reserve.getMatchUserMatchInfoId());
            //重置匹配单信息
            boolean site = matchInfoService.resetMatchInfo(reserve.getMatchUserMatchInfoId());
            if (!site){
                throw new Exception("重置匹配单信息失败！");
            }
            //置空预订单的匹配引用
            int i = reserveMapper.setReserveMatchingNull(guid);
            if (i<=0){
                throw new Exception("置空预订单引用匹配失败！");
            }
            //额外推送信息给主单用户
            messageService.saveSystemMessage("您的拼友"+reserve.getUserName()+"取消了订单，将恢复您的匹配拼桌球友，请知悉",matchInfo.getBillUserId());

        }
        //候补操作，即当存在日期队列时才会触发(后续可以使用mq来异步，不然太耗费时间了)
//        if (reserveDate!=null){
//            candidateReserveService.processCandidateReserve(reserve);
//        }
        //优化后的候补操作
        if (reserveDate!=null){
            CandidateReserveThread candidateReserveThread = new CandidateReserveThread();
            candidateReserveThread.setCancelReserve(reserve);
            candidateReserveThread.setCandidateReserveService(candidateReserveService);
            candidateReserveThread.setUser(commonService.getThisUserId());
            threadPool.execute(candidateReserveThread);//执行线程
        }
        return true;
    }
    //处理删除订单业务
    @Override
    public boolean processDeleteReserve(String guid) throws Exception {
        //校验订单是否可以删除(只允许删除已确认的订单和已确认的订单)
        Reserve reserve = getReserveById(guid);
        //只允许删除【已确认】或者【已取消】的订单
        if (ReserveStatus.RESERVE_USED.getCode()==reserve.getReserveStatus()||
        ReserveStatus.RESERVE_CANCEL.getCode()==reserve.getReserveStatus()){
            //删除预订单（物理删除）
            reserveMapper.deleteReserveById(guid);
            //删除对应的匹配单
            MatchInfo matchInfo=new MatchInfo();
            matchInfo.setBillReserveId(guid);
            matchInfoService.deleteMatchInfo(matchInfo);
        }
        else {
            throw  new Exception("该预订单未确认，请确认后再删除!");
        }
        return true;
    }

    @Override
    public int updateBatchStatus(Integer status, List idList) {
        return reserveMapper.updateBatchStatus(status, idList);
    }

    @Override
    public List<Reserve> getCanMatchReserve(Reserve reserve) {
        //增加当前用户权限
        User user = commonService.getThisUserId();
        reserve.setUserId(user.getGuid());
        return reserveMapper.getCanMatchReserve(reserve);
    }

    @Override
    public int setReserveMatchingNull(String guid) {
        return reserveMapper.setReserveMatchingNull(guid);
    }

    @Override
    public List<Reserve> getValidReserveByDateList(List<String> dateList) {
        return reserveMapper.getValidReserveByDateList(dateList);
    }
    //校验订单是否允许确认
    @Override
    public boolean verifyConfirmReserve(Reserve reserve) throws Exception {
        //获取订单的开始日期
        String startDate = reserve.getStartDate().substring(0,10);
        String nowDateStr = commonService.getNowDateStr().substring(0,10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date now = sdf.parse(nowDateStr);
        if (now.getTime()>=start.getTime()){
            return true;
        }
        else {
            throw new Exception("不能确认比当前日期早的订单");
        }
    }
    //取消待支付的预订单
    @Override
    public boolean processCancelUserWaitPayOrder(String redisKey) {
        //清除redis缓存
        redisDao.cleanReserveInfo(redisKey);
        //清除本地全局映射
        Set<String> redisKeySet = ReserveDate.getRedisKeySet();
        redisKeySet.remove(redisKey);
        return true;
    }

    @Override
    public boolean verifyCancelReserve(Reserve reserve) throws Exception {
        //1、有匹配单的不允许取消
        MatchInfo info = matchInfoService.getMatchInfoByReserveId(reserve.getGuid());
        if (info!=null){
            throw new Exception("该订单存在匹配单，请删除匹配单以后再取消！");
        }
        //判断该预订单是否为匹配单用户的，增加校验
        if (!StringTool.isNull(reserve.getMatchUserMatchInfoId())){
            //获取当前系统时间
            String nowDateStr = commonService.getNowDateStr();
            String startDate = reserve.getStartDate();
            if (nowDateStr.substring(0,10).equals(startDate.substring(0,10))){
                throw new Exception("球友匹配的订单，预订日期当日不允许取消订单!");
            }
        }
        return true;
    }

    //校验是否允许预订
    private boolean verifyCanReserve(Reserve reserve) throws Exception {
        boolean site=false;
        //前置校验
        //如果process_date正好是当天，那么获取当前系统的小时数，小于等于当前小时的信息都不允许预订
        String nowDateStr = commonService.getNowDateStr();
        String dateStr = nowDateStr.substring(0, 10);
        String hourStr = nowDateStr.substring(11, 13);
        if (reserve.getStartDate().substring(0, 10).equals(dateStr)){
            Integer reserveHour=Integer.parseInt(reserve.getStartDate().substring(11,13));
            Integer nowHour=Integer.parseInt(hourStr);
            if (nowHour>=reserveHour){
                throw new ReserveException("3","预订时间不能小于当前系统时间!禁止预订");
            }
        }
        //1、获取redis中的锁定预订单
        Map<String, ReserveDate> reserveDateListMap=null;
        Set<String> redisKeySet = ReserveDate.getRedisKeySet();
        if (redisKeySet.size()>0){
            String[] keys=new String[redisKeySet.size()];
            keys = redisKeySet.toArray(keys);
            List<Reserve> reserveList = redisDao.getReserveList(Arrays.asList(keys));
            //生成预订时间列表映射
            reserveDateListMap = BaseDataLoader.createReserveDateListMap(reserveList);
        }
        //校验redis中是否被锁定
        String key = reserve.getReserveDateKey();
        int hour=Integer.parseInt(reserve.getStartDate().substring(11,13));
        Integer useTime = reserve.getUseTime();
        if (reserveDateListMap!=null){
            ReserveDate reserveDate = reserveDateListMap.get(key);
            //存在当前预订日期时间集合时，才进行校验
            if (reserveDate!=null){
                boolean b = reserveDate.verifyCanReserve(hour, useTime);
                if (!b)
                    throw new ReserveException("5","该时间段已经被其他用户锁定，稍后若用户未付款，您可重新预订!");
            }
        }
        //校验本地内存的预订时间列表
        Map<String, ReserveDate> localReserveDateListMap = commonService.getReserveDateListMap();
        ReserveDate reserveDate = localReserveDateListMap.get(key);
        //如果为空，说明当天没有任何预订，直接返回true
        if (reserveDate!=null){
            boolean b = reserveDate.verifyCanReserve(hour, useTime);
            if (!b)
                throw new ReserveException("1","该时间段已经被预订!");
        }
        //执行到这里说明已经通过校验
        site=true;
        return site;
    }

}
