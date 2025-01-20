package com.atsu.tabletennisreservation.redis.dao;

import com.atsu.tabletennisreservation.pojo.CandidateReserve;
import com.atsu.tabletennisreservation.pojo.MatchInfo;
import com.atsu.tabletennisreservation.pojo.Reserve;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

public interface RedisDao {
    //将预订保存到redis中，并设置过期时间(如果为空则为永不过期)
    boolean addReserve(String key,Reserve reserve,Integer seconds);
    //查询在redis中所有的Reserve
    List<Reserve> getReserveList(List<String> keyList);
    //将匹配单保存到redis中，并设置过期时间
    boolean addMatchInfo(String key, MatchInfo matchInfo, Integer seconds);
    //查询在redis中的所有MatchInfo
    List<MatchInfo> getMatchInfoList(List<String> keyList);
    //将候补单保存到redis，并设置过期时间
    boolean addCandidateReserve(String key,CandidateReserve candidateReserve,Integer seconds);
    //根据key，从redis中获取候补单信息
    CandidateReserve getCandidateReserve(String key);
    //根据用户id，获取用户的待支付预订单信息
    List<Reserve> getUserReserveList(String userId);
    //获取当前用户的待支付预订单信息
    List<Reserve> getThisUserReserveList();
    //将预订单的key存入用户的待支付订单set中
    boolean addReserveKeyToUserReserveKeySet(String reserveKey);
    //从用户的待支付订单set移除指定的预订key
    boolean removeReserveKeyToUserReserveKeySet(String reserveKey);
    //清除缓存中对应key的预订单信息
    boolean cleanReserveInfo(String key);
}
