package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.MenuNode;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.Menu;
import com.atsu.tabletennisreservation.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private MenuService menuService;
    //菜单管理页
    @GetMapping("/")
    public String menuIndex(){
        return "menu/menu";
    }
    //角色菜单授权主页
    @GetMapping("/roleMenu")
    public String roleMenuIndex(){
        return "menu/role_menu";
    }
    @GetMapping("/nodeList")
    @ResponseBody
    public Result getMenuNodeList(){
        List<MenuNode> menuNodeList = menuService.getMenuNodeList();
        //加一个根节点
        MenuNode menuNode=new MenuNode();
        menuNode.setId("-1");
        menuNode.setChildren(menuNodeList);
        menuNode.setData("菜单信息");
        return Result.success(menuNode);
    }
    @RequestMapping("/menuNode")
    @ResponseBody
    public List<MenuNode> getMenuNodeList2(){
        List<MenuNode> menuNodeList = menuService.getMenuNodeList();
        //加一个根节点
        MenuNode menuNode=new MenuNode();
        menuNode.setId("000");
        menuNode.setChildren(menuNodeList);
        menuNode.setData("菜单信息");
        List<MenuNode> list=new ArrayList<>();
        list.add(menuNode);
        return list;
    }
    @GetMapping("/simple/{guid}")
    @ResponseBody
    //根据id获取菜单详细信息
    public Result getMenuInfo(@PathVariable("guid") String guid){
        Menu menuById = menuService.getMenuById(guid);
        return  Result.success(menuById);
    }
    @GetMapping("/extend/{guid}")
    @ResponseBody
    public Result getMenuCodeLike(@PathVariable(value = "guid",required = false) String guid){
        //如果点击虚拟根节点，返回全部信息
        if ("-1".equals(guid)){
            guid="";
        }
        List<Menu> list = menuService.getMenuCodeLike(guid);
        return  Result.success(list);
    }
    //更新菜单信息
    @PutMapping("/simple")
    @ResponseBody
    public Result updateMenuInfo(@RequestBody Menu menu){
        Result result=null;
        int i = menuService.updateMenuById(menu);
        if (i>0){
            result=Result.success("ok");
        }
        else {
            result=Result.failed("ok");
        }
        return result;
    }
    //保存菜单信息
    @PostMapping("/simple")
    @ResponseBody
    public Result saveMenuInfo(@RequestBody Menu menu){
        Result result=null;
        int i = menuService.saveMenu(menu);
        if (i>0){
            result=Result.success("ok");
        }
        else {
            result=Result.failed("ok");
        }
        return result;
    }
    @DeleteMapping("/simple")
    @ResponseBody
    public Result deleteMenuInfo(@RequestBody Menu menu){
        Result result=null;
        int i = menuService.deleteMenu(menu);
        if (i>0){
            result=Result.success("ok");
        }
        else {
            result=Result.failed("ok");
        }
        return result;
    }
    @GetMapping("/menuTree")
    @ResponseBody
    public Result getMenuTree(){
        List<Menu> menuTree = menuService.getMenuTree();
        return Result.success(menuTree);
    }
    //获取当前用户角色授权的菜单树信息
    @GetMapping("/user/menuTree")
    @ResponseBody
    public Result getThisUserMenuTree(){
        List<Menu> menuTree = menuService.getThisUserMenuTreeList();
        return Result.success(menuTree);
    }
}
