package com.atsu.tabletennisreservation.controller;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.Result;
import com.atsu.tabletennisreservation.pojo.BallTable;
import com.atsu.tabletennisreservation.service.BallTableService;
import com.atsu.tabletennisreservation.utils.StringTool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/ballTable")
public class BallTableController {
    private Logger logger=Logger.getLogger(this.getClass().getName());
    @Resource
    private BallTableService ballTableService;
    @GetMapping("/manager")
    public String ballTable(){
        return "manager/ballTableManager";
    }
    //获取所有球桌数据
    @GetMapping("/ballTableList")
    @ResponseBody
    public Result getBallTableList(){
        //查询所有页面数据
        List<BallTable> ballTableList = ballTableService.getBallTableList(null);
        return  Result.success(ballTableList,"请求成功");
    }
    //获取所有球桌数据，用于管理数据
    @GetMapping("/getAdminBallTableListPage/{pageNo}")
    @ResponseBody
    public Result getAdminBallTableListPage(@PathVariable("pageNo") Integer pageNo){
        //查询所有页面数据
        PageResult<BallTable> pageResult = ballTableService.getAllBallTableListPage(new BallTable(),pageNo, 5);
        return  Result.success(pageResult,"请求成功");
    }
    //获取球桌数据
    @GetMapping("/ballTableList/{pageNo}")
    @ResponseBody
    public Result getBallTableListPage(@PathVariable("pageNo") Integer pageNo, HttpServletRequest request){
        String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
        String img_url = (String) request.getServletContext().getAttribute("img_url");
        //查询页面数据
        PageResult<BallTable> ballTableListPage = ballTableService.getBallTableListPage(StringTool.nvl(pageNo,1), 3);
        Result result = Result.success(ballTableListPage, "ok");
        result.setBaseUrl(baseUrl);
        result.setImgUrl(img_url);
        return  result;
    }
    //根据id查询球桌信息
    @GetMapping("/{guid}")
    @ResponseBody
    public Result getBallTableById(@PathVariable("guid") String guid, HttpServletRequest request){
        String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
        String img_url = (String) request.getServletContext().getAttribute("img_url");
        BallTable ballTableById = ballTableService.getBallTableById(guid);
        Result result = Result.success(ballTableById, "ok");
        result.setBaseUrl(baseUrl);
        result.setImgUrl(img_url);
        return result;
    }
    //根据id查询球桌信息
    @DeleteMapping("/{guid}")
    @ResponseBody
    public Result deleteBallTableById(@PathVariable("guid") String guid, HttpServletRequest request){
        String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
        String img_url = (String) request.getServletContext().getAttribute("img_url");
        int i = ballTableService.deleteBallTableById(guid);
        Result result=null;
        if (i>0){
            result = Result.success("删除成功", "ok");
        }
        else
            result = Result.success("删除失败", "ok");
        logger.info("删除guid="+guid+"的数据完成");
        result.setBaseUrl(baseUrl);
        result.setImgUrl(img_url);
        return result;
    }

    //修改球桌数据
    @PutMapping("/")
    @ResponseBody
    public Result updateBallTable(@RequestBody BallTable ballTable){
        Result re=null;
        try {
            int i = ballTableService.updateBallTableById(ballTable);
            if (i>0){
                re=Result.success("成功","成功");
            }
            else {
                re=Result.failed("失败","更新失败");
            }
            System.out.println(ballTable);
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("失败",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //添加球桌信息
    @PostMapping("/")
    @ResponseBody
    public Result addBallTable(@RequestBody BallTable ballTable){
        Result re=null;
        try {
            logger.info("处理添加球桌信息...");
            int i =ballTableService.addBallTable(ballTable);
            if (i>0){
                re=Result.success("成功","成功");
            }
            else {
                re=Result.failed("失败","新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re=Result.failed("失败",e.getMessage());
        }
        finally {
            return re;
        }
    }
    //获取可预订且开放的球桌数据，用于预订界面
    @GetMapping("/getCanReserveBallTableList/{pageNo}")
    @ResponseBody
    public Result getCanReserveBallTableList(@PathVariable("pageNo") Integer pageNo,HttpServletRequest request){
        String baseUrl = (String) request.getServletContext().getAttribute("baseUrl");
        String img_url = (String) request.getServletContext().getAttribute("img_url");
        PageResult<BallTable> listPage = ballTableService.getCanReserveBallTableListPage(pageNo, 3);
        Result result = Result.success(listPage, "ok");
        result.setBaseUrl(baseUrl);
        result.setImgUrl(img_url);
        return result;
    }

}
