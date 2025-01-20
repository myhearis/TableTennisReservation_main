package com.atsu.tabletennisreservation.redis.dao.listening;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//用于监听redis的一些事件的操作
@Component
public class RedisKeyExpirationListener {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisKeyExpirationListener(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void listenToKeyExpiration(String key) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.pSubscribe((message, pattern) -> {
                String expiredKey = new String(message.getBody());
                System.out.println("Key expired: " + expiredKey);
                // 在这里可以添加处理过期事件的逻辑
                System.out.println("key="+key+" 的内容过期了!");
            }, ("__keyspace@0__:" + key).getBytes());
            return null;
        });
    }
}