package com.atsu.tabletennisreservation.interceptor;

import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

//登录拦截器,最先执行
@Order(1)
public class LoginInterceptor implements HandlerInterceptor {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否已经登陆
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
//        logger.info("登录拦截器，当前user="+user);
        if (user==null){
            //如果没登录，则重定向到登录界面
            String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
            response.sendRedirect(baseUrl+"/user/login");
            return false;
        }
        return true;
    }
}
