package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.Reserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReserveMapper {
    //保存订单
    int saveReserve(@Param("reserve") Reserve reserve );
    //批量保存
    int saveReserveBatch(@Param("reserveList") List<Reserve> reserveList);
    //查询预订单，根据Reserve中的条件
    List<Reserve> getReserveList(@Param("reserve") Reserve reserve);
    //单个查询(主要根据guid)
    Reserve getReserve(@Param("reserve") Reserve reserve);
    //删除预订单
    int deleteReserveById(@Param("guid") String guid);
    //根据条件删除预订单
    int deleteReserve(@Param("reserve") Reserve reserve);
    //根据日期查询预订数据 年-月-日
    List<Reserve> getReserveByDate(@Param("dateStr") String dateStr);
    //根据多个日期查询
    List<Reserve> getReserveByDateList(@Param("dateList") List dateList);
    //批量更新预订单状态
    int updateBatchStatus(@Param("status") Integer status,@Param("idList") List idList );
    //根据id批量查询数据
    List<Reserve> getReserveListById(@Param("ids") List<String> ids);
    //根据匹配单id，批量查询数据
    List<Reserve> getReserveListByMatchInfoId(@Param("matchInfoIdList") List<String> matchInfoIdList);
    //根据匹配单id，逻辑删除预订单数据
    int deleteMatchingReserveBatch(@Param("matchidList") List<String> matchidList);
    //查询进行中的预订单
    List<Reserve> getProgressReserve(@Param("reserve") Reserve reserve);
    //查询待确认的预订单（即当天进行的订单）
    List<Reserve> getWaitConfirmReserve(@Param("reserve") Reserve reserve);
    //查询已过期的订单
    List<Reserve> getExpiredReserve(@Param("reserve") Reserve reserve);
    //查询可发布匹配单的预订单
    List<Reserve> getCanMatchReserve(@Param("reserve") Reserve reserve);
    //将预订单的引用匹配单信息置空
    int setReserveMatchingNull(@Param("guid") String guid);
    //查询全部日期内有效的预订单（用于初始化基础数据）
     List<Reserve> getValidReserveByDateList(@Param("dateList") List<String> dateList);
}
