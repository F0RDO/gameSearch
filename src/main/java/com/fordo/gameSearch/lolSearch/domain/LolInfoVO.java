package com.fordo.gameSearch.lolSearch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LolInfoVO {
    // 국가 관련 Dto
    private String gameName;
    private String cid;
    private String countryCode;
    private String countryName;

    // 검색 조건
    private String userName;
    private String country;

    /** riot-api 에서 받아온 값 **/

    // 소환사 정보
    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private String profileIconId;
    private String revisionDate;
    private String summonerLevel;

    // 소환사 티어 정보
    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private String summonerName;
    private String leaguePoints;
    private String wins;
    private String losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
}
