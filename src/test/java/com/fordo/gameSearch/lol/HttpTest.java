package com.fordo.gameSearch.lol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fordo.gameSearch.configure.ApiKey;
import com.fordo.gameSearch.lolSearch.domain.MatchDto;
import com.fordo.gameSearch.lolSearch.domain.ParticipantDto;
import com.fordo.gameSearch.lolSearch.domain.spell.SpellInfo;
import com.fordo.gameSearch.lolSearch.domain.spell.SpellVO;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@SpringBootTest
public class HttpTest {
    @Autowired
    ApiKey apiKey;

    @Test
    public void tt() throws Exception {
        SpellVO spellVO = null;
        String version = "13.10.1";
        try {
            spellVO = WebClient.create()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("ddragon.leagueoflegends.com")
                            .path("/cdn/" + version + "/data/en_US/summoner.json")
                            .queryParam("api_key", apiKey.getRiotApiKey())
                            .build())
                    .retrieve()
                    .bodyToMono(SpellVO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("getMatchInfo Error : " + e.getMessage());
            spellVO = new SpellVO();
        }
        System.out.println("spellVO : "+spellVO);
        System.out.println(spellVO.getData());
        String str1 = "3";
        String str2 = "7";
        String spell1 = "";
        String spell2 = "";
        try {
            int indexNum = 0;
            for(String key : spellVO.getData().keySet()){
                if(indexNum == Integer.parseInt(str1)) spell1 = spellVO.getData().get(key).getImage().getFull();
                indexNum++;
            }
            System.out.println(spell1);
        }catch (NullPointerException e){
            System.out.println("getMatchInfo Error For NullPointerException : "+ e.getMessage());
        }catch (Exception e){
            System.out.println("getMatchInfo Error2 : "+ e.getMessage());
        }
    }
}