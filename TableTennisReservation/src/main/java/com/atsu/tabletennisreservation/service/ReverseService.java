package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.dto.ReverseDto;
import com.atsu.tabletennisreservation.pojo.Period;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.pojo.ReserveDate;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

public interface ReverseService {
    //保存订单
    int saveReserve( Reserve reserve );
    int saveReserve(ReverseDto reverseDto);
    //根据id批量查询数据
    List<Reserve> getReserveListById( List<String> ids);
    //查询预订单，根据Reserve中的条件
    List<Reserve> getReserveList( Reserve reserve);
    //ReserveWrapper
    List<Reserve> getReserveWrapperList( Reserve reserve);
    //单个查询
    Reserve getReserve( Reserve reserve);
    //删除预订单
    int deleteReserveById(String guid);
    //根据当前日期，获取时间列表(废弃)
    List<String> getReserveDateInfo(String dateStr);
    //根据多个日期查询
    List<Reserve> getReserveByDateList( List dateList);
    //取消订单
    boolean cancelReserveById(String guid);
    //根据guid获取订单
    Reserve getReserveById( String guid);
    //分页获取匹配订单信息
    PageResult<Reserve> getReserveMatchtListPage(int pageNo, int pageSize,String code,String startDate);
    //分页获取当前用户匹配订单信息
    PageResult<Reserve> getThisUserReserveMatchtListPage(int pageNo, int pageSize,String code,String startDate);
    /**
     * #Description 构造发布匹配信息
     * @param orderIdList:
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean createMatching(List orderIdList);
    /**
     * #Description 获取当前球桌的可预订期间，跟redis中的锁定数据相结合
     * @param tableCode:
     * @param process_date:
     * @return com.atsu.tabletennisreservation.pojo.ReserveDate
     * @author sujinbin
     * #Date 2024/3/31
     */
    ReserveDate getTableReserveDate(String tableCode, String process_date);
    /**
     * #Description 处理预订请求(前置处理)
     * @param reserve:
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean processReserve(Reserve reserve) throws Exception;
    /**
     * #Description 处理支付成功，保存预订单入库
     * @param reserve: 操作的预订单
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean processPaySucessSaveReserve(Reserve reserve);
    //批量保存
    int saveReserveBatch(List<Reserve> reserveList);
    //根据匹配单id，批量查询数据
    List<Reserve> getReserveListByMatchInfoId( List<String> matchInfoIdList);
    //根据匹配单id，逻辑删除预订单数据
    int deleteMatchingReserveBatch(List<String> matchidList);
    //根据条件查询分页信息
    PageResult<Reserve> getReservePage(int pageNo, int pageSize,Reserve condition);
    //获取当前用户的预订单分页信息
    PageResult<Reserve> getThisUserReservePage(int pageNo, int pageSize,Reserve condition);
    //查询进行中的预订单
    List<Reserve> getProgressReserve( Reserve reserve);
    /**
     * #Description 查询进行中的订单，并分页
     * @param pageNo:
     * @param pageSize:
     * @param condition:
     * @return com.atsu.tabletennisreservation.dto.PageResult<com.atsu.tabletennisreservation.pojo.Reserve>
     * @author sujinbin
     * #Date 2024/3/31
     */
    PageResult<Reserve> getProgressReservePage(int pageNo, int pageSize,Reserve condition);
    /**
     * #Description 查询待确认的订单，并进行分页
     * @param pageNo:
     * @param pageSize:
     * @param reserve:
     * @return com.atsu.tabletennisreservation.dto.PageResult<com.atsu.tabletennisreservation.pojo.Reserve>
     * @author sujinbin
     * #Date 2024/3/31
     */
    PageResult<Reserve> getWaitConfirmReservePage(int pageNo, int pageSize,@Param("reserve") Reserve reserve);
    /**
     * #Description 查询已过期的订单,并进行分页
     * @param pageNo:
     * @param pageSize:
     * @param reserve:
     * @return com.atsu.tabletennisreservation.dto.PageResult<com.atsu.tabletennisreservation.pojo.Reserve>
     * @author sujinbin
     * #Date 2024/3/31
     */
    PageResult<Reserve> getExpiredReservePage(int pageNo, int pageSize,@Param("reserve") Reserve reserve);
    /**
     * #Description 处理用户确认订单业务
     * @param guid: 预订单主键
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean processConfirmReserve(String guid) throws Exception;
    /**
     * #Description 处理用户取消订单业务
     * @param guid:
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean processCancelReserve(String guid) throws Exception;
    /**
     * #Description 处理用户删除订单业务
     * @param guid:
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean processDeleteReserve(String guid) throws Exception;
    /**
     * #Description 批量更新预订单状态
     * @param status:
     * @param idList:
     * @return int
     * @author sujinbin
     * #Date 2024/3/31
     */
    int updateBatchStatus( Integer status,@Param("idList") List idList );
    /**
     * #Description 查询可发布匹配单的预订单
     * @param reserve:
     * @return java.util.List<com.atsu.tabletennisreservation.pojo.Reserve>
     * @author sujinbin
     * #Date 2024/3/31
     */
    List<Reserve> getCanMatchReserve( Reserve reserve);
    /**
     * #Description 将预订单的引用匹配单信息置空
     * @param guid:
     * @return int
     * @author sujinbin
     * #Date 2024/3/31
     */
    int setReserveMatchingNull( String guid);
    /**
     * #Description 查询传入的日期集合内有效的预订单（用于初始化基础预订球桌日期集合数据）
     * @param dateList:
     * @return java.util.List<com.atsu.tabletennisreservation.pojo.Reserve>
     * @author sujinbin
     * #Date 2024/3/31
     */
    List<Reserve> getValidReserveByDateList(List<String> dateList);
    /**
     * #Description 校验用户确认订单:如果通过校验，返回真,否则抛异常
     * @param reserve:  预订单对象
     * @return boolean
     * @author sujinbin
     * #Date 2024/3/31
     */
    boolean verifyConfirmReserve(Reserve reserve) throws Exception;
    /**
     * #Description 取消待支付的预订单
     * @param redisKey: 预订单在redis中的key
     * @return boolean
     * @author sujinbin
     * #Date 2024/4/2
     */
    boolean processCancelUserWaitPayOrder(String redisKey);
    /**
     * #Description 校验是否允许取消预订单
     * @param reserve: 预订单
     * @return boolean
     * @author sujinbin
     * #Date 2024/4/17
     */
    boolean verifyCancelReserve(Reserve reserve) throws Exception;
}
