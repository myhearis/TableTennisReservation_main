package com.atsu.tabletennisreservation.redis.dao;

import com.atsu.tabletennisreservation.utils.DateUtil;

//根据不同内容生成对应需求的key
public class KeyProcess {
    //生成用户期间锁定的key  时间_用户hash_球桌code
    public static String getUserRedisLockPeriodKey(String userId,String tableCode){
        StringBuffer key=new StringBuffer();
        DateUtil dateUtil=new DateUtil();
        String nowPayTimeStr = dateUtil.getNowPayTimeStr();
        key.append(nowPayTimeStr).append("_");
        key.append(userId.hashCode()).append("_");
        key.append(tableCode);
        return key.toString();
    }
}
