package com.fordo.gameSearch.lolSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BanDto {
    private String championId;
    private String pickTurn;
}
