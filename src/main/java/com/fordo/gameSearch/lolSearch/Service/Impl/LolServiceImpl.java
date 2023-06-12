package com.fordo.gameSearch.lolSearch.Service.Impl;

import com.fordo.gameSearch.lolSearch.Service.LolService;
import com.fordo.gameSearch.lolSearch.Service.Mapper.LolMapper;
import com.fordo.gameSearch.lolSearch.domain.LolInfoVO;
import com.fordo.gameSearch.lolSearch.domain.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LolServiceImpl implements LolService {

    @Autowired
    private LolMapper lolMapper;

    @Override
    public List<LolInfoVO> findCountry() {
        return lolMapper.findCountry();
    }

    @Override
    public int countUserInfo(String userName) {
        return lolMapper.countUserInfo(userName);
    }

    @Override
    public LolInfoVO findUserInfo(String userName) {
        return lolMapper.findUserInfo(userName);
    }

    @Override
    public int insertUserInfo(LolInfoVO lolInfoVO) {
        return lolMapper.insertUserInfo(lolInfoVO);
    }

    @Override
    public int insertMatchInfoList(List<MatchDto> matchInfoList) {
        return lolMapper.insertMatchInfoList(matchInfoList);
    }

    @Override
    public List<MatchDto> selectMatchInfoList(String userName) {
        List<MatchDto> matchList = lolMapper.selectMatchInfoList(userName);
        for(MatchDto dto : matchList){
            System.out.println("확인 : "+dto.getMetadata());
        }
        return matchList;
    }

}
