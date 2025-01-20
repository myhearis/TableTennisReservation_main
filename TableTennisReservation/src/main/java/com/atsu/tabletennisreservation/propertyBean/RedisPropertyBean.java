package com.atsu.tabletennisreservation.propertyBean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//redis相关配置信息
@Component
@ConfigurationProperties(prefix = "redispro")
public class RedisPropertyBean {
    //保存用户redis未支付订单的key
    private String userReserveKeySetPrefix;

    public String getUserReserveKeySetPrefix() {
        return userReserveKeySetPrefix;
    }

    public void setUserReserveKeySetPrefix(String userReserveKeySetPrefix) {
        this.userReserveKeySetPrefix = userReserveKeySetPrefix;
    }

}
