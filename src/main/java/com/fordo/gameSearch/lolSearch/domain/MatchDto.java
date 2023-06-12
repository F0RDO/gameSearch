package com.fordo.gameSearch.lolSearch.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchDto {
    private MetadataDto metadata;
    private MatchInfoVO info;
    private String userName;
}
