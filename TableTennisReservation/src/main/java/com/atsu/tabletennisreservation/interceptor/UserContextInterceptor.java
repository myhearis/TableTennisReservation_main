package com.atsu.tabletennisreservation.interceptor;

import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.utils.UserContext;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

//线程用户上下文局部变量绑定
@Order(3)//在登录拦截器之后执行
public class UserContextInterceptor implements HandlerInterceptor {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if (user!=null){
            User u= (User) user;
            UserContext.setThisUser(u);
        }
//        logger.info(request.getServletPath()+"绑定user完成! user="+user);
        return true;
    }
}
