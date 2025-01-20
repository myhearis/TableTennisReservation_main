package com.atsu.tabletennisreservation.interceptor;

import com.atsu.tabletennisreservation.configuration.RoleDataLoader;
import com.atsu.tabletennisreservation.pojo.User;
import com.atsu.tabletennisreservation.service.RoleService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.logging.Logger;

//菜单拦截器，用于校验当前用户是否有该菜单的访问权限
@Component
public class MenuInterceptor implements HandlerInterceptor {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private RoleService roleService;
    @Resource
    private RoleDataLoader roleDataLoader;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        //判断是否为菜单路径,只拦截菜单路径
        Set<String> allMenuUrlSet = roleDataLoader.getAllMenuUrlSet();
        if (allMenuUrlSet==null)
            roleDataLoader.refresh();
        if (allMenuUrlSet.contains(requestURI)){
            logger.info("校验菜单:"+requestURI);
            //校验用户是否有访问的权限
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user!=null){
                String roleId = user.getRoleId();
                boolean site = roleService.verifyRoleMenu(roleId, requestURI);
                if (!site){
                    logger.info("拦截菜单权限路径:"+requestURI);
                    return false;
                }
                else {
                    return  true;
                }

            }
        }
        return true;
    }

    public MenuInterceptor(RoleService roleService,RoleDataLoader roleDataLoader) {
        this.roleService = roleService;
    }

    public RoleDataLoader getRoleDataLoader() {
        return roleDataLoader;
    }

    public void setRoleDataLoader(RoleDataLoader roleDataLoader) {
        this.roleDataLoader = roleDataLoader;
    }

    public MenuInterceptor() {
    }
}
