package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.dto.RoleMenuDto;
import com.atsu.tabletennisreservation.pojo.Role;
import com.atsu.tabletennisreservation.service.RoleMenuService;
import com.atsu.tabletennisreservation.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @Resource
    private RoleMenuService roleMenuService;
    //返回所有角色数据
    @GetMapping("/")
    @ResponseBody
    public Result getRoles(){
        List<Role> roleList = roleService.getRoleList(new Role());
        return  Result.success(roleList);
    }
    //保存角色菜单数据
    @PostMapping("/saveRoleMenu")
    @ResponseBody
    public Result saveRoleMenu(@RequestBody RoleMenuDto roleMenuDto){
        System.out.println(roleMenuDto);
        int i = roleMenuService.saveRoleMenu(roleMenuDto);
        System.out.println(i);
        return Result.success("ok");
    }
    //返回当前登录用户的角色菜单dto：普通用户登录使用
    @GetMapping("/user/roleMenuDto")
    @ResponseBody
    public Result getThisUserRoleMenuDto(){
        RoleMenuDto thisUserRoleMenuDto = roleMenuService.getThisUserRoleMenuDto();
        return Result.success(thisUserRoleMenuDto);
    }
    //根据用户id返回对应的菜单权限（菜单管理使用）
    @GetMapping("/roleMenuDto/{roleId}")
    @ResponseBody
    public Result getRoleMenuDto(@PathVariable("roleId") String roleId ){
        RoleMenuDto thisUserRoleMenuDto = roleMenuService.getRoleMenuDto(roleId);
        return Result.success(thisUserRoleMenuDto);
    }
}
