package com.atsu.tabletennisreservation.mapper;

import com.atsu.tabletennisreservation.pojo.MatchInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchInfoMapper {
    //保存
    int saveMatchInfo(@Param("matchInfo") MatchInfo matchInfo);
    //批量保存
    int saveMatchInfoBatch(@Param("matchInfoList") List<MatchInfo> matchInfoList);
    //查询
    List<MatchInfo> getMatchInfoList(@Param("matchInfo") MatchInfo matchInfo);
    //根据id批量查询信息
    List<MatchInfo> getMatchInfoListByIds(@Param("ids") List<String> ids);
    //更新
    int updateMatchInfoById(@Param("matchInfo") MatchInfo matchInfo);
    //批量根据id更新信息
    int updateMatchInfoByIdBatch(@Param("matchInfo") MatchInfo matchInfo, @Param("ids") List<String> ids);
    //删除
    int deleteMatchInfo(@Param("matchInfo") MatchInfo matchInfo);
    //将匹配用户的信息置空
    int setMatchingUserNull(@Param("ids") List<String> ids);
    //查询用于展示给全部用户的球友匹配单信息,不包括当前的主单用户
    List<MatchInfo> getUserSelectMatchInfoList(@Param("matchInfo") MatchInfo matchInfo);
}
