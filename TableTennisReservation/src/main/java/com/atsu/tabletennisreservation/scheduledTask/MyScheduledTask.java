package com.atsu.tabletennisreservation.scheduledTask;

import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.ReserveDate;
import com.atsu.tabletennisreservation.redis.dao.RedisDao;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

//定时任务
@Component
public class MyScheduledTask {
    private static Logger logger=Logger.getLogger(MyScheduledTask.class.getName());
    //获取redisDao
    @Resource
    private RedisDao redisDao;
    //定时清除本地内存中保存的已失效的未支付订单key
    @Scheduled(fixedRate = 1 * 60 * 1000) // 每 20 分钟执行一次
    public void executeTask() {
        logger.info("=======清除本地内存中保存的已失效的未支付订单key定时任务开始=========");
        Set<String> redisKeySet = ReserveDate.getRedisKeySet();
        Set<String> nowKeySet=new HashSet<>();
        //查询数据
        String[] keys =new String[redisKeySet.size()];
        keys = redisKeySet.toArray(keys);
        List<Reserve> reserveList = redisDao.getReserveList(Arrays.asList(keys));
        for (int i=0;i<reserveList.size();i++){
            nowKeySet.add(reserveList.get(i).getOutTradeNo());
        }
        //获取差集
        Set<String> difference = new HashSet<>(redisKeySet);
        difference.removeAll(nowKeySet); // 获取set1相对于set2的差集
        //删除这些失效的key
        for (String deleteKey : difference) {
            redisKeySet.remove(deleteKey);
        }
        logger.info("清理完成失效的key,共"+difference.size()+"个");
    }
}