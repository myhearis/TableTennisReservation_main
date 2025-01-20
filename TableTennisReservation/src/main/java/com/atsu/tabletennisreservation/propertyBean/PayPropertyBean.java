package com.atsu.tabletennisreservation.propertyBean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//支付的相关配置
@ConfigurationProperties(prefix = "zfbpay")
@Component
public class PayPropertyBean {
    private boolean isOpenPay;//是否开启支付

    public boolean isOpenPay() {
        return isOpenPay;
    }

    public void setOpenPay(boolean openPay) {
        isOpenPay = openPay;
    }
}
