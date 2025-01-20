package com.atsu.tabletennisreservation;

import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.atsu.tabletennisreservation.mapper")//mapper接口扫描
@ServletComponentScan(basePackages = "com.atsu.tabletennisreservation")
@EnableScheduling // 启用定时任务支持
@EnableConfigurationProperties(SystemParamsProperty.class)//优先加载系统参数配置绑定
public class PingPongApplication {
    public static void main(String[] args) {

        SpringApplication.run(PingPongApplication.class,args);
    }

}
