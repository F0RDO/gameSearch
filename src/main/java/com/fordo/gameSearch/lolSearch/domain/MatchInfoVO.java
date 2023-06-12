package com.fordo.gameSearch.lolSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchInfoVO {

    private String userTeamId;
    private Boolean userWinOrLose;
    private String enemyUserName;

    private String gameCreation;
    private String gameDuration;
    private String gameEndTimestamp;
    private String gameId;
    private String gameMode;
    private String gameName;
    private String gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private String mapId;
    private List<ParticipantDto> participants;
    private String platformId;
    private String queueId;
    private List<TeamDto> teams;
    private String tournamentCode;
    private String convertTime;
    private String restDays;
    private String restHours;

}
