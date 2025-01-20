package com.atsu.tabletennisreservation.configuration;

import com.atsu.tabletennisreservation.propertyBean.UploadFilePropertyBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private UploadFilePropertyBean uploadFilePropertyBean;
    /**
     * 图片保存路径，自动从yml文件中获取数据
     *   示例： E:/uploadFile/
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 配置资源映射处理器
         * 意思是：如果访问的资源路径是以“/uploadFile/”开头的，
         * 就给我映射到本机的“E:/uploadFile/”这个文件夹内，去找你要的资源
         * 注意：E:/uploadFile/ 后面的 “/”一定要带上
         */
        String urlMapping=uploadFilePropertyBean.getUrlMapping();
        String filePath=uploadFilePropertyBean.getFilePath();
        registry.addResourceHandler(urlMapping)
                .addResourceLocations("file:"+filePath);
    }
}