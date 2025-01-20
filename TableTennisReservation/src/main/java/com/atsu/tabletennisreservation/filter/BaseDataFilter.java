package com.atsu.tabletennisreservation.filter;

import com.atsu.tabletennisreservation.propertyBean.SystemParamsProperty;
import com.atsu.tabletennisreservation.propertyBean.UploadFilePropertyBean;
import com.atsu.tabletennisreservation.utils.StringTool;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

//基础数据过滤器:用于基础数据绑定
@WebFilter(urlPatterns = {"/*"})
public class BaseDataFilter implements Filter {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private UploadFilePropertyBean uploadFilePropertyBean;
    @Resource
    private SystemParamsProperty systemParamsProperty;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //将基础数据初始化到工程域
        ServletContext servletContext = request.getServletContext();
        //判断当前工程上下文对象中是否存在baseUrl
        Object baseUrl = servletContext.getAttribute("baseUrl");
        Object img_url = servletContext.getAttribute("img_url");
        if (baseUrl==null){
            String url=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            //如果配置了系统ip+端口，则使用系统配置的
            String systemIpPort = systemParamsProperty.getSystemIpPort();
            if (!StringTool.isNull(systemIpPort)){
                url="http://"+systemIpPort+"/";
            }
            baseUrl=url;
            //将当前的项目路径写入工程域中
            servletContext.setAttribute("baseUrl",url);
            logger.info("加载baseUrl="+url);
        }
        if (img_url==null){
            String url = (String) baseUrl;
            url=url+uploadFilePropertyBean.getPrePath();
            img_url=url;
            servletContext.setAttribute("img_url",url);
            logger.info("加载img_url="+url);
        }
        //绑定基础数据到请求域中，用于渲染前端
        request.setAttribute("sys_base_url",baseUrl);
        //绑定基础数据到请求域中，用于渲染前端
        request.setAttribute("sys_img_url",img_url);
        //绑定当前系统的端口和ip，用于webSocket连接
        request.setAttribute("sys_ip_port",systemParamsProperty.getSystemIpPort());
        filterChain.doFilter(request,response);
    }
}
