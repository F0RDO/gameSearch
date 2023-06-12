package com.fordo.gameSearch.lolSearch.domain.spell;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SpellVO {
    private String type;
    private String version;
    private Map<String,SpellInfo> data;
}
