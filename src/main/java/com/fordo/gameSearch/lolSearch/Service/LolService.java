package com.fordo.gameSearch.lolSearch.Service;

import com.fordo.gameSearch.lolSearch.domain.LolInfoVO;
import com.fordo.gameSearch.lolSearch.domain.MatchDto;

import java.util.List;

public interface LolService {
    // 국가 코드 수집
    List<LolInfoVO> findCountry();

    // 유저 정보 찾기(카운팅)
    int countUserInfo(String userName);

    // 해당 유저 찾기
    LolInfoVO findUserInfo(String userName);

    // 새로운 유저 정보 삽입
    int insertUserInfo(LolInfoVO lolInfoVO);

    int insertMatchInfoList(List<MatchDto> matchInfoList);

    List<MatchDto> selectMatchInfoList(String userName);
}
