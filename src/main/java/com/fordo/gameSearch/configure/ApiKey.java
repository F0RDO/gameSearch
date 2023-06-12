package com.fordo.gameSearch.configure;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@ToString
public class ApiKey {
    @Value("${api.lol.key}")
    private String riotApiKey;
}
