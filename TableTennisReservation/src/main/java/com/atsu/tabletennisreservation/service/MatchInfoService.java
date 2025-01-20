package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchInfoService {
    //保存
    int saveMatchInfo( MatchInfo matchInfo);
    //批量保存
    int saveMatchInfoBatch(List<MatchInfo> matchInfoList);
    //查询
    List<MatchInfo> getMatchInfoList( MatchInfo matchInfo);
    //所有用户的匹配单分页
    PageResult<MatchInfo> getMatchInfoListPage( MatchInfo condition,Integer pageNo, Integer pageSize);
    //当前用户的匹配单分页
    PageResult<MatchInfo> getThisUserMatchInfoListPage(Integer pageNo, Integer pageSize);
    PageResult<MatchInfo> getThisUserMatchInfoListPage(MatchInfo condition,Integer pageNo, Integer pageSize);
    //当前用户的数据根据id查询
    MatchInfo getThisUserMatchInfoById(String guid);
    //根据id查询
    MatchInfo getMatchInfoById(String guid);
    //更新
    int updateMatchInfoById(MatchInfo matchInfo);
    //删除
    int deleteMatchInfo(MatchInfo matchInfo);
    //根据预订单数据，批量生成匹配单
    int saveMatchInfoByReserveList(List<Reserve> reserveList);
    /**
     * #Description 预处理球友匹配请求,如果返回true，说明预处理完毕，如果返回false，说明预处理失败[一般是已经被锁定]
     * @param guid: 匹配单id
     * @param outTradeNo: 商户订单号
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/23
     */
    boolean processMatching(String guid,String outTradeNo) throws Exception;
    //获取主单用户（当前用户）的匹配单,不分页
    List<MatchInfo> getThisUserBillMatchInfoList(MatchInfo condition);
    //获取匹配用户（当前用户）申请的匹配单,不分页
    List<MatchInfo> getThisUserApplicationMatchInfoList(MatchInfo condition);
    //批量根据id更新信息
    int updateMatchInfoByIdBatch( MatchInfo matchInfo,List<String> ids);
    //处理主单用户确认
    boolean processBillUserConfirm(List<String> ids) throws Exception;
    //处理主单用户拒绝匹配
    boolean processBillUserReject(List<String> ids) throws Exception;
    //根据id批量查询信息
    List<MatchInfo> getMatchInfoListByIds( List<String> ids);
    //处理删除匹配单事件
    boolean processDeleteMatchInfo(String guid) throws Exception;
    //根据主单用户的匹配单id，判断是否存在已经交易的匹配单
    boolean isExistTradeMatchInfo(String reserveId);
    //根据匹配单id，重置匹配用户的信息和匹配单状态
    boolean resetMatchInfo(String guid);
    //根据预订单id，查询对应的匹配单
    MatchInfo getMatchInfoByReserveId(String reserveId);
    //查询用于展示给全部用户的球友匹配单信息,不包括当前的主单用户
    List<MatchInfo> getUserSelectMatchInfoList( MatchInfo matchInfo);
    PageResult<MatchInfo> getUserSelectMatchInfoListPage( MatchInfo matchInfo,Integer pageNo, Integer pageSize);
}
