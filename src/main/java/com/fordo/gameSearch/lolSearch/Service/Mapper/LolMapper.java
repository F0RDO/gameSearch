package com.fordo.gameSearch.lolSearch.Service.Mapper;

import com.fordo.gameSearch.lolSearch.domain.LolInfoVO;
import com.fordo.gameSearch.lolSearch.domain.MatchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LolMapper {
    List<LolInfoVO> findCountry();
    int countUserInfo(String userName);
    LolInfoVO findUserInfo(String userName);
    int insertUserInfo(LolInfoVO lolInfoVO);
    int insertMatchInfoList(List<MatchDto> matchInfoList);
    List<MatchDto> selectMatchInfoList(String userName);
}
