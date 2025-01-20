package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.BallTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BallTableMapper {
    //获取当前可预订的球桌信息(根据开放时间和状态过滤)
    List<BallTable> getCanReserveBallTableList();
    //查询所有球桌列表
    List<BallTable> getAllBallTableList();
    //查询球桌列表，status是否可用
    List<BallTable> getBallTableList(@Param("status") int status);

    //新增球桌
    int saveBallTable(@Param("ballTable") BallTable ballTable);

    //更新球桌
    int updateBallTableById(@Param("ballTable") BallTable ballTable);

    //根据id查询球桌信息
    BallTable getBallTableById(@Param("guid") String guid);

    //插入球桌信息
    int addBallTable(@Param("ballTable") BallTable ballTable);

    //根据id删除球桌数据
    int deleteBallTableById(@Param("guid") String guid);
    //根据条件查询球桌数据
    List<BallTable> getBallTableListCondition(@Param("ballTable") BallTable ballTable);
}
