package com.fordo.gameSearch.lolSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TeamDto {
    private List<BanDto> bans;
    private ObjectivesDto objectives;
    private String teamId;
    private String win;
}
