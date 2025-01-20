package com.atsu.tabletennisreservation.redis.dao.impl;

import com.atsu.tabletennisreservation.pojo.*;
import com.atsu.tabletennisreservation.propertyBean.RedisPropertyBean;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import com.atsu.tabletennisreservation.redis.dao.listening.RedisKeyExpirationListener;
import com.atsu.tabletennisreservation.service.CommonService;
import com.atsu.tabletennisreservation.utils.StringTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class RedisDaoImpl implements RedisDao {
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisKeyExpirationListener redisKeyExpirationListener;
    @Resource
    private RedisPropertyBean redisPropertyBean;
    @Resource
    private CommonService commonService;
    private Gson gson=new Gson();

    @Override
    public boolean addReserve(String key,Reserve reserve, Integer seconds) {
        //将预订单转化为json字符串,key为
        String s = gson.toJson(reserve);
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key,s,Duration.ofSeconds(seconds));
        //存入用户待支付订单集合
        addReserveKeyToUserReserveKeySet(key);
        return true;
    }

    @Override
    public List<Reserve> getReserveList(List<String> keyList) {
        List<String> list = redisTemplate.opsForValue().multiGet(keyList);
        List<Reserve> reserveList=new ArrayList<>();
        //转化成实体
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i=0;i<list.size();i++){
            Reserve reserve=null;
            String str=list.get(i);
            try {
                //不为空串时才解析，防止报错
                if (!StringTool.isNull(str)){
                    reserve = objectMapper.readValue(str, Reserve.class);
                }
                //为空串
                else {

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (reserve!=null){
                reserveList.add(reserve);
            }
        }
        return reserveList;
    }

    @Override
    public boolean addMatchInfo(String key, MatchInfo matchInfo, Integer seconds) {
        //将预订单转化为json字符串,key为
        String s = gson.toJson(matchInfo);
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key,s,Duration.ofSeconds(seconds));
        return true;
    }

    @Override
    public List<MatchInfo> getMatchInfoList(List<String> keyList) {
        List<String> list = redisTemplate.opsForValue().multiGet(keyList);
        List<MatchInfo> reserveList=new ArrayList<>();
        //转化成实体
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i=0;i<list.size();i++){
            MatchInfo reserve=null;
            String str=list.get(i);
            try {
                //不为空串时才解析，防止报错
                if (!StringTool.isNull(str)){
                    reserve = objectMapper.readValue(str, MatchInfo.class);
                }
                //为空串
                else {

                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (reserve!=null){
                reserveList.add(reserve);
            }
        }
        return reserveList;
    }

    @Override
    public boolean addCandidateReserve(String key,CandidateReserve candidateReserve, Integer seconds) {
        String s = gson.toJson(candidateReserve);
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key,s,Duration.ofSeconds(seconds));
        return true;
    }

    @Override
    public CandidateReserve getCandidateReserve(String key) {
        String str = (String) redisTemplate.opsForValue().get(key);
        CandidateReserve candidateReserve=null;
        //转化成实体
        ObjectMapper objectMapper = new ObjectMapper();
        //不为空串时才解析，防止报错
        try {
            //不为空串时才解析
            if (!StringTool.isNull(str)){
                candidateReserve = objectMapper.readValue(str, CandidateReserve.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return candidateReserve;
    }
    //获取用户待支付订单信息
    @Override
    public List<Reserve> getUserReserveList(String userId) {
        //获取key
        String key = getThisUserReserveSetKey();
        Set<String> members = redisTemplate.opsForSet().members(key);
        //转化为list
        if (members!=null){
            List<String> keyList=new ArrayList<>(members);
            //批量查询待支付订单
            List<Reserve> reserveList = getReserveList(keyList);
            return reserveList;
        }
        return null;
    }

    @Override
    public List<Reserve> getThisUserReserveList() {
        User user = commonService.getThisUserId();
        return getUserReserveList(user.getGuid());
    }

    @Override
    public boolean addReserveKeyToUserReserveKeySet(String reserveKey) {
        //获取key
        String key = getThisUserReserveSetKey();
        //保存信息
        redisTemplate.opsForSet().add(key, reserveKey);
        return true;
    }

    @Override
    public boolean removeReserveKeyToUserReserveKeySet(String reserveKey) {
        String key = getThisUserReserveSetKey();
        redisTemplate.opsForSet().remove(key, reserveKey);
        return true;
    }

    @Override
    public boolean cleanReserveInfo(String key) {
        //清除用户数据
        removeReserveKeyToUserReserveKeySet(key);
        //清除直接缓存
        redisTemplate.delete(key);
        return true;
    }

    //获取当前用户的待支付集合的key
    private String getThisUserReserveSetKey(){
        StringBuffer key=new StringBuffer();
        String userReserveKeySetPrefix = redisPropertyBean.getUserReserveKeySetPrefix();
        User user = commonService.getThisUserId();
        key.append(userReserveKeySetPrefix);
        key.append(user.getGuid());
        return key.toString();
    }
}
