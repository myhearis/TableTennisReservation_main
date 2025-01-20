package com.atsu.tabletennisreservation.propertyBean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//配置文件上传相关属性绑定
@ConfigurationProperties(prefix = "upload")
@Component
public class UploadFilePropertyBean {
    private String filePath;//文件真实路径
    private String urlMapping;//url映射路径
    private String prePath;

    public String getPrePath() {
        return prePath;
    }

    public void setPrePath(String prePath) {
        this.prePath = prePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(String urlMapping) {
        this.urlMapping = urlMapping;
    }
}
