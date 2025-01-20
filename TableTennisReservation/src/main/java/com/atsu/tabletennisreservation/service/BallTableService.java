package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.pojo.BallTable;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import javax.servlet.ServletContext;
import java.util.List;

public interface BallTableService {
    //查询球桌列表，status是否可用
    List<BallTable> getBallTableList( Integer status);

    //新增球桌
    int saveBallTable(@Param("ballTable") BallTable ballTable);

    //更新球桌
    int updateBallTableById(@Param("ballTable") BallTable ballTable) throws Exception;

    //返回分页信息
    PageResult<BallTable> getBallTableListPage(int pageNo,int pageSize);

    //根据id查询球桌信息
    BallTable getBallTableById(@Param("guid") String guid);

    //插入球桌信息
    int addBallTable( BallTable ballTable) throws Exception;
    //根据id删除球桌数据
    int deleteBallTableById( String guid);
    //初始化预订队列信息(废弃)
    void  initReverseData(ServletContext servletContext);
    /**
     * #Description 获取可预订的球桌集合
     * @param :
     * @return java.util.List<com.atsu.tabletennisreservation.pojo.BallTable>
     * @author sujinbin
     * #Date 2024/4/16
     */
    List<BallTable> getCanReserveBallTableList();
    /**
     * #Description 分页获取可预订的球桌信息
     * @param pageNo: 页码
     * @param pageSize: 分页大小
     * @return com.atsu.tabletennisreservation.dto.PageResult<com.atsu.tabletennisreservation.pojo.BallTable>
     * @author sujinbin
     * #Date 2024/4/16
     */
    PageResult<BallTable> getCanReserveBallTableListPage(int pageNo,int pageSize);
    //获取所有球桌信息的分页
    PageResult<BallTable> getAllBallTableListPage(BallTable condition,int pageNo,int pageSize);
}
