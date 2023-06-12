package com.fordo.gameSearch.lolSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ParticipantDto {
    private String gameId;

    // 임의적으로 사용하려고 만든 변수(매치 총합킬, 매치 총합데스, 매치 총합어시스트, 평균, 킬관여율)
    private int sumKills;
    private int sumDeaths;
    private int sumAssists;
    private double calAvg;
    private int killPercent;

    // 스펠 관련 변수
    private String spell1;
    private String spell2;

    private String assists;
    private String baronKills;
    private String bountyLevel;
    private String champExperience;
    private String champLevel;
    private String championId;
    private String championName;
    private String championTransform;
    private String consumablesPurchased;
    private String damageDealtToBuildings;
    private String damageDealtToObjectives;
    private String damageDealtToTurrets;
    private String damageSelfMitigated;
    private String deaths;
    private String detectorWardsPlaced;
    private String doubleKills;
    private String dragonKills;
    private Boolean firstBloodAssist;
    private Boolean firstBloodKill;
    private Boolean firstTowerAssist;
    private Boolean firstTowerKill;
    private Boolean gameEndedInEarlySurrender;
    private Boolean gameEndedInSurrender;
    private String goldEarned;
    private String goldSpent;
    private String individualPosition;
    private String inhibitorKills;
    private String inhibitorTakedowns;
    private String inhibitorsLost;
    private String item0;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;
    private String itemsPurchased;
    private String killingSprees;
    private String kills;
    private String lane;
    private String largestCriticalStrike;
    private String largestKillingSpree;
    private String largestMultiKill;
    private String longestTimeSpentLiving;
    private String magicDamageDealt;
    private String magicDamageDealtToChampions;
    private String magicDamageTaken;
    private String neutralMinionsKilled;
    private String nexusKills;
    private String nexusTakedowns;
    private String nexusLost;
    private String objectivesStolen;
    private String objectivesStolenAssists;
    private String participantId;
    private String pentaKills;

    private String physicalDamageDealt;
    private String physicalDamageDealtToChampions;
    private String physicalDamageTaken;
    private String profileIcon;
    private String puuid;
    private String quadraKills;
    private String riotIdName;
    private String riotIdTagline;
    private String role;
    private String sightWardsBoughtInGame;
    private String spell1Casts;
    private String spell2Casts;
    private String spell3Casts;
    private String spell4Casts;
    private String summoner1Casts;
    private String summoner1Id;
    private String summoner2Casts;
    private String summoner2Id;
    private String summonerId;
    private String summonerLevel;
    private String summonerName;
    private String teamEarlySurrendered;
    private String teamId;
    private String teamPosition;
    private String timeCCingOthers;
    private String timePlayed;
    private String totalDamageDealt;
    private String totalDamageDealtToChampions;
    private String totalDamageShieldedOnTeammates;
    private String totalDamageTaken;
    private String totalHeal;
    private String totalHealsOnTeammates;
    private String totalMinionsKilled;
    private String totalTimeCCDealt;
    private String totalTimeSpentDead;
    private String totalUnitsHealed;
    private String tripleKills;
    private String trueDamageDealt;
    private String trueDamageDealtToChampions;
    private String trueDamageTaken;
    private String turretKills;
    private String turretTakedowns;
    private String turretsLost;
    private String unrealKills;
    private String visionScore;
    private String visionWardsBoughtInGame;
    private String wardsKilled;
    private String wardsPlaced;
    private Boolean win;
    private String totalAllyJungleMinionsKilled;
}
