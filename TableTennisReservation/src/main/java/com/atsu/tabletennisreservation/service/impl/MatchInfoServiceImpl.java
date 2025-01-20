package com.atsu.tabletennisreservation.service.impl;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.enums.ReserveStatus;
import com.atsu.tabletennisreservation.mapper.MatchInfoMapper;
import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.ReserveDate;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.service.MatchInfoService;
import com.atsu.tabletennisreservation.service.ReverseService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class MatchInfoServiceImpl implements MatchInfoService {
    private static Logger logger=Logger.getLogger(MatchInfoServiceImpl.class.getName());
    @Resource
    private MatchInfoMapper matchInfoMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private ReverseService reverseService;
    public static final Integer EXPIRED_TIME=900;//redis key的过期时间
    @Override
    public int saveMatchInfo(MatchInfo matchInfo) {
        //补充信息
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        matchInfo.setGuid(commonService.getId());
        matchInfo.setStatus(0);//匹配单状态 0 正在匹配  1 预匹配，等待主单用户同意 2 匹配成功
        matchInfo.setCreateUser(user.getGuid());
        matchInfo.setUpdateUser(user.getGuid());
        matchInfo.setCreateDate(nowDateStr);
        matchInfo.setUpdateDate(nowDateStr);
        return matchInfoMapper.saveMatchInfo(matchInfo);
    }
    //批量保存
    @Override
    public int saveMatchInfoBatch(List<MatchInfo> matchInfoList) {
        //批量补充信息
        User user = commonService.getThisUserId();
        String nowDateStr = commonService.getNowDateStr();
        for (int i=0;i<matchInfoList.size();i++){
            MatchInfo matchInfo = matchInfoList.get(i);
            matchInfo.setGuid(commonService.getId());
            matchInfo.setStatus(0);//匹配单状态 0 正在匹配  1 预匹配，等待主单用户同意 2 匹配成功
            matchInfo.setCreateUser(user.getGuid());
            matchInfo.setUpdateUser(user.getGuid());
            matchInfo.setCreateDate(nowDateStr);
            matchInfo.setUpdateDate(nowDateStr);
        }
        return matchInfoMapper.saveMatchInfoBatch(matchInfoList);
    }

    @Override
    public List<MatchInfo> getMatchInfoList(MatchInfo matchInfo) {
        return matchInfoMapper.getMatchInfoList(matchInfo);
    }

    @Override
    public PageResult<MatchInfo> getMatchInfoListPage(MatchInfo condition,Integer pageNo, Integer pageSize) {
        User user = commonService.getThisUserId();
        //分页器
        PageHelper.startPage(pageNo,pageSize);
        List<MatchInfo> list = matchInfoMapper.getMatchInfoList(condition);
        PageInfo<MatchInfo> pageInfo=new PageInfo<>(list);
        //生成分页信息
        PageResult<MatchInfo> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<MatchInfo> getThisUserMatchInfoListPage(Integer pageNo, Integer pageSize) {
        User user = commonService.getThisUserId();
        //分页器
        PageHelper.startPage(pageNo,pageSize);
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setBillUserId(user.getGuid());
        List<MatchInfo> list = matchInfoMapper.getMatchInfoList(matchInfo);
        PageInfo<MatchInfo> pageInfo=new PageInfo<>(list);
        //生成分页信息
        PageResult<MatchInfo> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public PageResult<MatchInfo> getThisUserMatchInfoListPage(MatchInfo condition, Integer pageNo, Integer pageSize) {
        User user = commonService.getThisUserId();
        //分页器
        PageHelper.startPage(pageNo,pageSize);
        condition.setBillUserId(user.getGuid());//加上当前用户id
        List<MatchInfo> list = matchInfoMapper.getMatchInfoList(condition);//添加额外条件
        PageInfo<MatchInfo> pageInfo=new PageInfo<>(list);
        //生成分页信息
        PageResult<MatchInfo> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    @Override
    public MatchInfo getThisUserMatchInfoById(String guid) {
        User user = commonService.getThisUserId();
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setBillUserId(user.getGuid());
        matchInfo.setGuid(guid);
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(matchInfo);
        if (matchInfoList!=null&&matchInfoList.size()==1){
            return matchInfoList.get(0);
        }
        return null;
    }

    @Override
    public MatchInfo getMatchInfoById(String guid) {
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setGuid(guid);
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(matchInfo);
        if (matchInfoList!=null&&matchInfoList.size()==1){
            return matchInfoList.get(0);
        }
        return null;
    }

    @Override
    public int updateMatchInfoById(MatchInfo matchInfo) {
        //补充信息
        User user = commonService.getThisUserId();
        //用户不为空时进行构造
        if (user!=null){
            matchInfo.setUpdateUser(user.getGuid());
        }
        String nowDateStr = commonService.getNowDateStr();
        matchInfo.setUpdateDate(nowDateStr);
        return matchInfoMapper.updateMatchInfoById(matchInfo);
    }

    @Override
    public int deleteMatchInfo(MatchInfo matchInfo) {
        return matchInfoMapper.deleteMatchInfo(matchInfo);
    }

    @Override
    public int saveMatchInfoByReserveList(List<Reserve> reserveList) {
        //将预订单数据转化为匹配单
        List<MatchInfo> matchInfoList=new ArrayList<>();
        for (int i=0;i<reserveList.size();i++){
            Reserve reserve = reserveList.get(i);
            //初始化业务数据信息
            MatchInfo matchInfo=new MatchInfo();
            matchInfo.setBillReserveId(reserve.getGuid());//主单预订单id
            matchInfo.setBillUserId(reserve.getUserId());//主单用户id
            matchInfo.setBillUserName(reserve.getUserName());//主单用户名
            matchInfo.setBillUserOutTradeNo(reserve.getOutTradeNo());//主单用户的商户订单号
            matchInfo.setBillUserTradeNo(reserve.getTradeNo());//主单用户交易号
            matchInfo.setTableId(reserve.getTableId());//球桌id
            matchInfo.setTableCode(reserve.getTableCode());//球桌code
            matchInfo.setStartDate(reserve.getStartDate());//开始时间
            matchInfo.setUseTime(reserve.getUseTime());//使用时长
            //计算双方应付金额
            //设置金额
            BigDecimal payAmt = reserve.getPayAmt();
            // 将 payAmt 除以 2 保留两位小数
            BigDecimal matchUserPayAmt = payAmt.divide(new BigDecimal("2"), 2, RoundingMode.CEILING);//匹配用户需支付金额
            BigDecimal billUserPayAmt = payAmt.subtract(matchUserPayAmt);//主单用户需支付金额
            matchInfo.setBillUserNeedPayAmt(billUserPayAmt);
            matchInfo.setMatchUserNeedPayAmt(matchUserPayAmt);
            matchInfoList.add(matchInfo);
        }
        //再批量生成信息(这里会自动填充基础数据)
        int i = saveMatchInfoBatch(matchInfoList);
        return i;
    }

    @Override
    public boolean processMatching(String guid,String outTradeNo) throws Exception {
        //根据id，获取匹配单
        MatchInfo matchInfo = getMatchInfoById(guid);
        //如果校验未通过，直接抛异常
        if (!verifyMatching(matchInfo)){
            logger.info("该匹配单已被其他用户锁定!");
            throw new Exception("该匹配单已被其他用户锁定!");
        }
        //这里需要进行线程互斥
        synchronized (Object.class){
            //再次校验该预订单是否被锁定
            boolean site = verifyMatching(matchInfo);
            if (!site){
                logger.info("该匹配单已被其他用户锁定!");
                throw new Exception("该匹配单已被其他用户锁定!");
            }
            //补充数据到匹配单中
            User user = commonService.getThisUserId();
            matchInfo.setMatchUserId(user.getGuid());
            matchInfo.setMatchUserName(user.getUserName());
            matchInfo.setMatchUserOutTradeNo(outTradeNo);
            //存入redis
            Set<String> matchInfoRedisKeySet = ReserveDate.getMatchInfoRedisKeySet();
            //存入全局redis key集合
            matchInfoRedisKeySet.add(outTradeNo);
            redisDao.addMatchInfo(outTradeNo,matchInfo,EXPIRED_TIME);
            logger.info("匹配单存入redis完成!待用户付款");
            logger.info(matchInfo.toString());
            return true;
        }
    }

    @Override
    public List<MatchInfo> getThisUserBillMatchInfoList(MatchInfo condition) {
        User user = commonService.getThisUserId();
        condition.setBillUserId(user.getGuid());
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(condition);
        return matchInfoList;
    }

    @Override
    public List<MatchInfo> getThisUserApplicationMatchInfoList(MatchInfo condition) {
        User user = commonService.getThisUserId();
        condition.setMatchUserId(user.getGuid());
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(condition);
        return matchInfoList;
    }

    @Override
    public int updateMatchInfoByIdBatch(MatchInfo matchInfo, List<String> ids) {
        return matchInfoMapper.updateMatchInfoByIdBatch(matchInfo,ids);
    }
    //处理主单用户确认信息
    @Override
    public boolean processBillUserConfirm(List<String> ids) throws Exception {
        //批量校验是否重复生成
        List<Reserve> re = reverseService.getReserveListByMatchInfoId(ids);
        if (re!=null&&re.size()>0){
            throw new Exception("已生成过该订单,不能重复生成");
        }
        //1、设置匹配单状态为 已确认
        User user = commonService.getThisUserId();
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setStatus(2);//匹配成功状态
        matchInfoMapper.updateMatchInfoByIdBatch(matchInfo,ids);
        //2、反向生成匹配用户的订单信息
        //根据id查询信息
        List<MatchInfo> list = matchInfoMapper.getMatchInfoListByIds(ids);
        List<Reserve> reserveList = new ArrayList<>();
         //根据匹配单，批量生成匹配用户的订单
        if (list!=null&&list.size()>0){
            for (int i=0;i<list.size();i++){
                MatchInfo info = list.get(i);
                Reserve reserve=new Reserve();
                reserve.setUserId(info.getMatchUserId());//用户id
                reserve.setUserName(info.getMatchUserName());//用户名
                reserve.setOutTradeNo(info.getMatchUserOutTradeNo());//商户订单号
                reserve.setTableId(info.getTableId());//球桌id
                reserve.setTradeNo(info.getMatchUserTradeNo());//交易号
                reserve.setPayAmt(info.getMatchUserNeedPayAmt());//交易金额
                reserve.setStartDate(info.getStartDate());//开始日期
                reserve.setUseTime(info.getUseTime());//使用时长
                reserve.setTableCode(info.getTableCode());//球桌编号
                reserve.setReserveStatus(3);//预订状态 已付款，待使用
                reserve.setMatchUserMatchInfoId(info.getGuid());
                reserveList.add(reserve);
            }
            //批量保存
            int i = reverseService.saveReserveBatch(reserveList);
            return true;
        }
        return false;
    }

    @Override
    public boolean processBillUserReject(List<String> ids) throws Exception {
        //1、去除匹配单中匹配用户的数据
        int i = matchInfoMapper.setMatchingUserNull(ids);
        //2、删除用户对应的预订单数据(逻辑删除)
        int i1 = reverseService.deleteMatchingReserveBatch(ids);
        return i>0;
    }

    @Override
    public List<MatchInfo> getMatchInfoListByIds(List<String> ids) {
        return matchInfoMapper.getMatchInfoListByIds(ids);
    }
    //处理删除匹配单事件
    @Override
    public boolean processDeleteMatchInfo(String guid) throws Exception {
        //1、校验匹配单是否存在匹配用户
        MatchInfo matchInfo = getMatchInfoById(guid);
        //如果匹配单存在交易信息，说明存在匹配用户
        if (!StringTool.isNull(matchInfo.getMatchUserTradeNo())){
            throw  new Exception("匹配单已有用户匹配，不允许删除！如果要删除请联系匹配用户取消订单或您取消该对应订单");
        }
        //2、删除匹配单数据
        int i = deleteMatchInfo(matchInfo);
        //重置预订单状态为【待使用】
        List<String> ids = new ArrayList<>();
        ids.add(matchInfo.getBillReserveId());
        reverseService.updateBatchStatus(ReserveStatus.RESERVE_SUCCESS.getCode(),ids);
        return i>0;
    }

    @Override
    public boolean isExistTradeMatchInfo(String reserveId) {
        boolean site=false;
        //查询主单用户id
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setBillReserveId(reserveId);
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(matchInfo);
        if (matchInfoList!=null&&matchInfoList.size()==1){
            MatchInfo info = matchInfoList.get(0);
            //判断是否存在匹配单交易
            if (!StringTool.isNull(info.getMatchUserTradeNo())){
                site=true;
            }
        }
        return site;
    }
    //重置匹配单的匹配信息
    @Override
    public boolean resetMatchInfo(String guid) {
        List<String> ids=new ArrayList<>();
        ids.add(guid);
        int i = matchInfoMapper.setMatchingUserNull(ids);
        return i>0;
    }

    @Override
    public MatchInfo getMatchInfoByReserveId(String reserveId) {
        //构造条件
        MatchInfo matchInfo=new MatchInfo();
        matchInfo.setBillReserveId(reserveId);
        List<MatchInfo> matchInfoList = matchInfoMapper.getMatchInfoList(matchInfo);
        if (matchInfoList!=null&&matchInfoList.size()==1){
            return matchInfoList.get(0);
        }
        return null;
    }

    @Override
    public List<MatchInfo> getUserSelectMatchInfoList(MatchInfo matchInfo) {
        return matchInfoMapper.getUserSelectMatchInfoList(matchInfo);
    }

    @Override
    public PageResult<MatchInfo> getUserSelectMatchInfoListPage(MatchInfo matchInfo, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<MatchInfo> list = matchInfoMapper.getUserSelectMatchInfoList(matchInfo);
        PageInfo<MatchInfo> pageInfo=new PageInfo<>(list);
        //生成分页
        PageResult<MatchInfo> pageResult=new PageResult<>(pageInfo);
        return pageResult;
    }

    private boolean verifyMatching(MatchInfo matchInfo){
        //校验该预订单是否被锁定
        Set<String> redisKeySet = ReserveDate.getMatchInfoRedisKeySet();
        String[] keys=new String[redisKeySet.size()];
        keys = redisKeySet.toArray(keys);
        List<MatchInfo> matchInfoList = redisDao.getMatchInfoList(Arrays.asList(keys));
        //判断是否存在有该匹配单
        if (matchInfoList!=null&&matchInfoList.size()>0){
            for(int i=0;i<matchInfoList.size();i++){
                //如果存在于redis中，则为锁定状态
                if (matchInfoList.get(i).getGuid().equals(matchInfo.getGuid())){
                    return false;
                }
            }
        }
        //如果不存在数据，则直接允许匹配
        else {
            //校验该匹配单是否已经存在了交易信息,已存在交易信息的不允许匹配
            if (StringTool.isNull(matchInfo.getMatchUserTradeNo())){
                return true;
            }
            else
                return false;
        }
        return true;
    }



}
