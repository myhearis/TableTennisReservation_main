package com.atsu.tabletennisreservation.utils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//支付宝读取配置文件工具类
public class ZfbCommonUtils {
    //getZFBinfoValue()：用于获取zfbinfo.properties资源文件中的参数\
    public static String getZFBinfoValue(String key)  {
        Properties properties=new Properties();
        InputStream fileInputStream=null;
        String propertyValue=null;
        Resource resource=new ClassPathResource("zfbinfo.properties");
        // fileInputStream=resource.getInputStream();

        try {
            //创建配置文件输入流
            fileInputStream=resource.getInputStream();;
            //加载到配置对象中
            properties.load(fileInputStream);
            //获取value
            propertyValue = properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //关闭流
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return propertyValue;
    }
    //测试
    public static void main(String[] args) {
        String appid = getZFBinfoValue("appid");
        System.out.println(appid);
    }
}
