package com.fordo.gameSearch.lolSearch.domain.spell;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpellInfo {
    private String id;
    private String name;
    private String description;
    private String tooltip;
    private String maxrank;
    private String costBurn;
    private String key;
    private String summonerLevel;
    private String costType;
    private SpellImageVO image;
    private String  resource;
}
