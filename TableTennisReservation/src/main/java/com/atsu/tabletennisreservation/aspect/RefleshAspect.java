package com.atsu.tabletennisreservation.aspect;

import com.atsu.tabletennisreservation.configuration.RoleDataLoader;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
//对更新菜单和角色的操作进行代理，更新时重新加载内存中的角色和菜单数据
@Aspect
@Component
public class RefleshAspect {
    @Resource
    private RoleDataLoader roleDataLoader;
    @After("execution(* com.atsu.tabletennisreservation.service.MenuService.saveMenu(..))" +
            "|| execution(* com.atsu.tabletennisreservation.service.MenuService.updateMenuById(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.MenuService.deleteMenu(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleService.saveRole(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleService.deleteRole(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleService.updateRoleById(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleMenuService.saveRoleMenu(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleMenuService.saveRoleMenuBatch(..))"+
            "|| execution(* com.atsu.tabletennisreservation.service.RoleMenuService.deleteRoleMenu(..))"
    )
    public void afterServiceMethodExecution() {
        // 在 Service 层方法执行后执行的逻辑
        //刷新角色菜单映射数据
        roleDataLoader.refresh();
    }
}
