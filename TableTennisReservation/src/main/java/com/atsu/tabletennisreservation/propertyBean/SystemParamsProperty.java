package com.atsu.tabletennisreservation.propertyBean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//系统参数配置
@Component
@ConfigurationProperties(prefix = "systemservice")
public class SystemParamsProperty {
    //是否开启预订单校验
    private boolean isVerifyReserve;
    private String systemIpPort;//当前服务器ip+端口号
    private boolean useZfb;//是否开启支付宝支付

    public boolean isVerifyReserve() {
        return isVerifyReserve;
    }

    public void setIsVerifyReserve(boolean verifyReserve) {
        isVerifyReserve = verifyReserve;
    }

    public String getSystemIpPort() {
        return systemIpPort;
    }

    public void setSystemIpPort(String systemIpPort) {
        this.systemIpPort = systemIpPort;
    }

    public boolean isUseZfb() {
        return useZfb;
    }

    public void setUseZfb(boolean useZfb) {
        this.useZfb = useZfb;
    }
}
