package com.atsu.tabletennisreservation.configuration;

import com.atsu.tabletennisreservation.interceptor.LoginInterceptor;
import com.atsu.tabletennisreservation.interceptor.MenuInterceptor;
import com.atsu.tabletennisreservation.interceptor.UserContextInterceptor;
import com.atsu.tabletennisreservation.pojo.Menu;
import com.atsu.tabletennisreservation.service.MenuService;
import com.atsu.tabletennisreservation.service.RoleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private MenuInterceptor menuInterceptor;//需要注入ioc，才能自动注入依赖
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //排除的请求路径
        List<String> loginExcludePathList=new ArrayList<>();
        loginExcludePathList.add("/user/login");//排除登录请求
        loginExcludePathList.add("/user/register");//排除注册页面
        loginExcludePathList.add("/user/processRegister");//排除注册请求
        loginExcludePathList.add("/user/verifyCanRegister/**");//排除注册校验请求
        //排除静态资源请求
        loginExcludePathList.add("/public/**");
        loginExcludePathList.add("/plugins/**");
        loginExcludePathList.add("/js/**");
        loginExcludePathList.add("/dist/**");
        loginExcludePathList.add("/css/**");
        //排除处理登录请求
        loginExcludePathList.add("/user/processLogin");
        //排除支付成功回调，和支付成功的异步通知
        loginExcludePathList.add("/processPaySuccessReverse/**");
        loginExcludePathList.add("/processPaySuccessNotify/**");
        loginExcludePathList.add("/pay/processMatchPaySuccessNotify/**");
        loginExcludePathList.add("/pay/processMatchPaySuccessReverse/**");
        loginExcludePathList.add("/pay/processCandidatePaySuccessNotify/**");
        //排除主页，用于回调
        loginExcludePathList.add("/paySucess");
        //注册登录拦截器
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(loginExcludePathList);//过滤拦截路径
        //注册用户上下文线程局部变量初始化拦截器
        registry.addInterceptor(new UserContextInterceptor()).addPathPatterns("/**");
        //注册菜单拦截器，只拦截已配置的菜单路径信息
        registry.addInterceptor(menuInterceptor).addPathPatterns("/**");
    }
}
