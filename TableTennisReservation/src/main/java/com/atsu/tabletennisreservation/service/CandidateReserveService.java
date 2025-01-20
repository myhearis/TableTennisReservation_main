package com.atsu.tabletennisreservation.service;

import com.atsu.tabletennisreservation.dto.PageResult;
import com.atsu.tabletennisreservation.exception.SysException;
import com.atsu.tabletennisreservation.pojo.CandidateReserve;
import com.atsu.tabletennisreservation.pojo.Reserve;
import com.atsu.tabletennisreservation.utils.StringTool;

import java.util.List;

public interface CandidateReserveService {
    //根据条件查询
    List<CandidateReserve> getCandidateReserveList( CandidateReserve candidateReserve);
    //根据id查询候补单
    CandidateReserve getCandidateReserveById(String guid);
    //根据条件删除
    int deleteCandidateReserve( CandidateReserve candidateReserve);
    //保存
    int saveCandidateReserve( CandidateReserve candidateReserve) throws SysException;
    //更新
    int updateCandidateReserveById( CandidateReserve candidateReserve);
    //获取当前用户的候补数据-分页
    PageResult<CandidateReserve> getUserCandidateReserveListPage(int pageNo, int pageSize,CandidateReserve candidateReserve);
    //获取所有用户的候补数据-分页
    PageResult<CandidateReserve> getCandidateReserveListPage(int pageNo, int pageSize,CandidateReserve candidateReserve);
    //根据订单信息，将候补数据转变为预订单
    boolean processCandidateReserve(List<Reserve> reserveList);
    //处理取消候补订单
    boolean processCandidateReserve(Reserve cancelReserve);
    //校验是否允许候补
    boolean verifyCandidate(CandidateReserve candidateReserve) throws SysException;

}
