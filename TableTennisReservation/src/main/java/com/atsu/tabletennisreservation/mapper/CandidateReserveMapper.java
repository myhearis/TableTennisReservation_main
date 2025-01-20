package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.CandidateReserve;
import com.atsu.tabletennisreservation.pojo.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CandidateReserveMapper {
    //根据条件查询
    List<CandidateReserve> getCandidateReserveList(@Param("candidateReserve") CandidateReserve candidateReserve);
    //根据条件删除
    int deleteCandidateReserve(@Param("candidateReserve") CandidateReserve candidateReserve);
    //保存
    int saveCandidateReserve(@Param("candidateReserve") CandidateReserve candidateReserve);
    //更新
    int updateCandidateReserveById(@Param("candidateReserve") CandidateReserve candidateReserve);
    //根据期间查询符合的候补信息
    List<CandidateReserve>  getUseCandidateReserveList(@Param("period") Period period );
}
