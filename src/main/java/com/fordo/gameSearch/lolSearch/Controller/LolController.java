package com.fordo.gameSearch.lolSearch.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fordo.gameSearch.configure.ApiKey;
import com.fordo.gameSearch.lolSearch.Service.LolService;
import com.fordo.gameSearch.lolSearch.domain.LolInfoVO;
import com.fordo.gameSearch.lolSearch.domain.MatchDto;
import com.fordo.gameSearch.lolSearch.domain.MatchInfoVO;
import com.fordo.gameSearch.lolSearch.domain.ParticipantDto;
import com.fordo.gameSearch.lolSearch.domain.spell.SpellVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.apache.ibatis.jdbc.Null;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class LolController {

    @Autowired
    private LolService lolService;

    @Autowired
    private ApiKey apiKey;

    /**
     * 롤 검색 페이지 이동
     * @return 롤 검색 페이지
     */
    @GetMapping(value = "lol/lolSearch.do")
    public String lolSearch(Model model){
        JSONArray jsonArray = findCountry();

        model.addAttribute("country", jsonArray);
        return "lol/lolSearch.html";
    }

    @GetMapping(value = "lol/search.do")
    public String moveSearchResult(LolInfoVO lolInfoVO, Model model){
        try {
            // 국가코드 정보 수집
            JSONArray jsonArray = findCountry();
            // 최근 버전 확인
            String version = findVersion();
            // 소환사 정보 수집
            String userName = lolInfoVO.getUserName() == null ? "" : lolInfoVO.getUserName();
            int countUserInfo = lolService.countUserInfo(userName);
            LolInfoVO userInfo = selectOrInsertUserInfo(userName, countUserInfo);
            // 소환사 티어 정보 수집(솔로랭크, 자유랭크)
            List<LolInfoVO> tierInfo = setTierInfoOfSizeOne(userInfo != null ? userInfo.getId() : "");
            // 소환사 최근 게임 리스트(10개씩)
            List<MatchDto> matchInfoList = null;
            if (userInfo != null && countUserInfo > 0) {
                System.out.println("있는 정보로 인해 수집");
                lolService.selectMatchInfoList(userName);
            }else {
                String[] matchIds = getMatchIdForApi(userInfo.getPuuid(), "0", "10");
                matchInfoList = getMatchInfoList(matchIds);
                matchInfoList = setSumKillsAndAvgAndKillPercent(matchInfoList);
                matchInfoList = convertMillisToTime(matchInfoList);
                matchInfoList = convertRestDay(matchInfoList);
                matchInfoList = setTeamIdAndEnemyNameAndWinOrLose(matchInfoList, userInfo.getName());
                matchInfoList = getSpellInfo(version,matchInfoList);
                int resultNum = lolService.insertMatchInfoList(matchInfoList);
            }

            // Request 영역에 담기
            model.addAttribute("result", userInfo);
            model.addAttribute("country", jsonArray);
            model.addAttribute("profileImg", "https://ddragon.leagueoflegends.com/cdn/"+version+"/img/profileicon/"+ Objects.requireNonNull(userInfo).getProfileIconId()+".png");
            model.addAttribute("version", version);
            model.addAttribute("tierInfo", tierInfo);
            model.addAttribute("matchInfo", matchInfoList);
        }catch (NullPointerException e){
            log.debug("찾는 소환사명의 정보는 없습니다."+e.getMessage());
            return "redirect:/lol/lolSearch.do";
        }catch (Exception e){
            log.debug("moveSearchResult Error : "+e.getMessage());
        }

        return "lol/lolSearchList.html";
    }

    @GetMapping(value = "lol/search.do1111")
    public String moveSearchResultBak(LolInfoVO lolInfoVO, Model model){
        try {
            // 국가코드 정보 수집
            JSONArray jsonArray = findCountry();
            // 소환사 정보 수집
            LolInfoVO userInfo = findUserInfo(lolInfoVO.getUserName()).block();
            // 최근 버전 확인
            String version = findVersion();
            // 소환사 티어 정보 수집(솔로랭크, 자유랭크)
            List<LolInfoVO> tierInfo = setTierInfoOfSizeOne(userInfo != null ? userInfo.getId() : "");
            // 소환사 최근 게임 리스트(10개씩)
            String[] matchIds = new String[0];
            if (userInfo != null) {
                matchIds = getMatchIdForApi(userInfo.getPuuid(), "0", "10");
            }
            List<MatchDto> matchInfoList = getMatchInfoList(matchIds);
            matchInfoList = setSumKillsAndAvgAndKillPercent(matchInfoList);
            matchInfoList = convertMillisToTime(matchInfoList);
            matchInfoList = convertRestDay(matchInfoList);
            matchInfoList = setTeamIdAndEnemyNameAndWinOrLose(matchInfoList, userInfo.getName());
            matchInfoList = getSpellInfo(version,matchInfoList);
            // Request 영역에 담기
            model.addAttribute("result", userInfo);
            model.addAttribute("country", jsonArray);
            model.addAttribute("profileImg", "https://ddragon.leagueoflegends.com/cdn/"+version+"/img/profileicon/"+ Objects.requireNonNull(userInfo).getProfileIconId()+".png");
            model.addAttribute("version", version);
            model.addAttribute("tierInfo", tierInfo);
            model.addAttribute("matchInfo", matchInfoList);
        }catch (NullPointerException e){
            log.debug("찾는 소환사명의 정보는 없습니다."+e.getMessage());
            return "redirect:/lol/lolSearch.do";
        }catch (Exception e){
            log.debug("moveSearchResult Error : "+e.getMessage());
        }

        return "lol/lolSearchList.html";
    }
    @RequestMapping("/lol/recentDetailInfo.do")
    public String recentDetailInfo(String matchId, String userName, String version, Model model){
        try {
            MatchDto matchDto = getMatchInfo(matchId).block();
            matchDto = convertMillisToTimeOnd(matchDto);
            String userTeamId = "";
            Boolean userWinOrLose = false;
            String enemyUserName = "";
            if (matchDto != null) {
                for(ParticipantDto participantDto : matchDto.getInfo().getParticipants()){
                    if(participantDto.getSummonerName().equals(userName)){
                        userTeamId = participantDto.getTeamId();
                        userWinOrLose = participantDto.getWin();
                    }else {
                        enemyUserName = participantDto.getSummonerName();
                    }
                }
            }
            model.addAttribute("version", version.replaceAll("\"", ""));
            model.addAttribute("userWinOrLose", userWinOrLose);
            model.addAttribute("enemyUserName", enemyUserName);
            model.addAttribute("userTeamId", userTeamId);
            model.addAttribute("userName", userName);
            model.addAttribute("matchInfo", matchDto);
        }catch (Exception e){
            log.debug("recentDetailInfo Error : "+e.getMessage());
        }
        return "lol/recentDetailInfo.html";
    }

    /**
     * 국가 코드 및 이름 DB에서 수집
     */
    private JSONArray findCountry(){
        JSONArray jsonArray = new JSONArray();
        try {
            List<LolInfoVO> countrys = lolService.findCountry();
            for (LolInfoVO country : countrys) {
                JSONObject json = new JSONObject();
                json.put("gameName", country.getGameName());
                json.put("cid", country.getCid());
                json.put("countryCode", country.getCountryCode());
                json.put("countryName", country.getCountryName());
                jsonArray.add(json);
            }
        }catch (Exception e){
            log.debug("findCountry Error : "+e.getMessage());
        }
        return jsonArray;
    }

    /**
     * 소환사 정보 검색
     * @param userName 소횐사명
     */
    private Mono<LolInfoVO> findUserInfo(String userName){
        Mono<LolInfoVO> mono = null;
        try {
            mono = WebClient.create()
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                        .scheme("https")
                                        .host("kr.api.riotgames.com")
                                        .path("/lol/summoner/v4/summoners/by-name/"+userName)
                                        .queryParam("api_key",apiKey.getRiotApiKey())
                                        .build())
                                .retrieve()
                                .bodyToMono(LolInfoVO.class)
                                .map(userInfo -> {
                                    LolInfoVO result = new LolInfoVO();
                                    result.setAccountId(userInfo.getAccountId());
                                    result.setProfileIconId(userInfo.getProfileIconId());
                                    result.setRevisionDate(userInfo.getRevisionDate());
                                    result.setName(userInfo.getName());
                                    result.setId(userInfo.getId());
                                    result.setPuuid(userInfo.getPuuid());
                                    result.setSummonerLevel(userInfo.getSummonerLevel());
                                    return result;
                                });
        }catch (Exception e){
            log.debug("findUserInfo Error : "+e.getMessage());
        }
        return mono;
    }

    /**
     * LOL 버전 확인
     * @return 최근 버전
     */
    private String findVersion(){
        String version = null;
        try {
            Mono<String> mono = WebClient.create()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("ddragon.leagueoflegends.com")
                            .path("/api/versions.json")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class);
            String versionList = mono.share().block();
            String[] versionSplit = new String[0];
            if (versionList != null) {
                versionSplit = versionList.split(",");
            }
            version = versionSplit[0].replaceAll("\"", "").replaceAll("\\[", "");
        }catch (Exception e){
            log.debug("findVersion Error : "+e.getMessage());
        }
        return version;
    }

    /**
     * 소환사 티어 정보 수집
     * @param userId ID값
     * @return 소환사 티어 정보
     */
    private List<LolInfoVO> findTierInfo(String userId){
        List<LolInfoVO> result = new ArrayList<>();
        try {
            result = WebClient.create()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("kr.api.riotgames.com")
                            .path("/lol/league/v4/entries/by-summoner/"+userId)
                            .queryParam("api_key",apiKey.getRiotApiKey())
                            .build())
                    .retrieve()
                    .bodyToFlux(LolInfoVO.class)
                    .toStream()
                    .collect(Collectors.toList());
        }catch(Exception e){
            log.debug("findTierInfo Error : "+e.getMessage());
        }

        return result;
    }

    /**
     * 솔로랭크와 자유랭크 리스트를
     * 0번째 : 솔로랭크, 1번째 : 자유랭크
     * 로 나오도록 설정함.
     * @return 티어 정보
     */
    public List<LolInfoVO> setTierInfoOfSizeOne(String id){
        List<LolInfoVO> tierInfo = findTierInfo(id);
        if(tierInfo.size() == 1){
            LolInfoVO tmpTierInfo = new LolInfoVO();
            if(tierInfo.get(0).getQueueType().equals("RANKED_FLEX_SR")){
                tierInfo.add(tierInfo.get(0));
                tierInfo.remove(0);
                tmpTierInfo.setQueueType("RANKED_SOLO_5x5");
                tmpTierInfo.setTier("Unranked");
                tmpTierInfo.setLeaguePoints("0");
                tmpTierInfo.setWins("0");
                tmpTierInfo.setLosses("0");
                tmpTierInfo.setRank("");
                tierInfo.add(0, tmpTierInfo);
            }else {
                tmpTierInfo.setQueueType("RANKED_FLEX_SR");
                tmpTierInfo.setTier("Unranked");
                tmpTierInfo.setLeaguePoints("0");
                tmpTierInfo.setWins("0");
                tmpTierInfo.setLosses("0");
                tmpTierInfo.setRank("");
                tierInfo.add(tmpTierInfo);
            }
        }
        return tierInfo;
    }

    /**
     *  API에서 인게임 ID(MatchID) 수집
     * @param puuid 유저 puuid
     * @param start 시작 번호
     * @param count 시작 번호로부터 가져올 matchId 개수
     * @return matchID 리스트
     */
    private String[] getMatchIdForApi(String puuid, String start, String count){
        String[] result = null;
        try {
            List<String> resultList = WebClient.create()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("asia.api.riotgames.com")
                            .path("/lol/match/v5/matches/by-puuid/"+puuid+"/ids")
                            .queryParam("api_key", apiKey.getRiotApiKey())
                            .queryParam("start", start)
                            .queryParam("count", count)
                            .build())
                    .retrieve()
                    .bodyToFlux(String.class)
                    .toStream()
                    .collect(Collectors.toList());
            if(resultList.size()>0){
                String matchIdList = resultList.get(0).replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","");
                result = matchIdList.split(",");
            }
        }catch (Exception e){
            log.debug("getMatchId Error"+e.getMessage());
        }

        return result;
    }


    /**
     * 매치 ID 리스트에 대한 각 매치 정보 수집
     * @param matchIds 매치 ID 리스트
     * @return 매치 정보 리스트
     */
    private List<MatchDto> getMatchInfoList(String[] matchIds){
        List<MatchDto> matchInfoList = new ArrayList<>();
        try {
            for(String matchInfo : matchIds){
                matchInfoList.add(getMatchInfo(matchInfo).block());
            }
        }catch (Exception e){
            log.debug("getMatchInfoList Error : "+e.getMessage());
        }
        return matchInfoList;
    }

    /**
     * 각 매치 정보 수집
     * @param matchId 매치 ID
     * @return 매치 정보
     */
    private Mono<MatchDto> getMatchInfo(String matchId){
        Mono<MatchDto> result = null;
        try {
                result = WebClient.create()
                        .get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("asia.api.riotgames.com")
                                .path("/lol/match/v5/matches/"+matchId)
                                .queryParam("api_key", apiKey.getRiotApiKey())
                                .build())
                        .retrieve()
                        .bodyToMono(MatchDto.class);
        } catch(Exception e){
            log.debug("getMatchInfo Error : "+e.getMessage());
            result = Mono.empty();
        }

        return result;
    }

    /**
     * 한 매치에 대한 총합 킬, 각 유저의 평균, 각 유저의 킬관여율 세팅
     */
    private List<MatchDto> setSumKillsAndAvgAndKillPercent(List<MatchDto> matchInfoList){
        try {
            for(MatchDto matchOne : matchInfoList){
                int sumKills = 0;
                int sumDeaths = 0;
                int sumAssists = 0;
                for(ParticipantDto dto : matchOne.getInfo().getParticipants()){
                    sumKills += Integer.parseInt(dto.getKills());
                    sumDeaths += Integer.parseInt(dto.getDeaths());
                    sumAssists += Integer.parseInt(dto.getAssists());

                    int userKill = Integer.parseInt(dto.getKills());
                    int userDeath = Integer.parseInt(dto.getDeaths());
                    int userAssist = Integer.parseInt(dto.getAssists());
                    double avg = 0.0;
                    if(userDeath == 0){
                        avg = userKill+userAssist;
                    }else {
                        avg = Math.round((userKill+userAssist) / userDeath)*100 / 100.0;
                    }
                    dto.setCalAvg(avg);
                }
                for(ParticipantDto dto : matchOne.getInfo().getParticipants()){
                    dto.setSumKills(sumKills);
                    dto.setSumDeaths(sumDeaths);
                    dto.setSumAssists(sumAssists);
                    int userKill = Integer.parseInt(dto.getKills());
                    int userAssist = Integer.parseInt(dto.getAssists());
                    int killPersent = (int)Math.round(((double)(userKill+userAssist)/ dto.getSumKills()) * 100);
                    dto.setKillPercent(killPersent);
                }
            }
        }catch (Exception e){
            log.debug("setSumKills Error : "+e.getMessage());
        }
        return matchInfoList;
    }

    /**
     * 모든 매치 정보들 밀리세컨드를 시간:분 형태로 변환
     */
    private List<MatchDto> convertMillisToTime(List<MatchDto> matchDtoList){
        try {
            for(MatchDto matchDto : matchDtoList){
                int gameDuration = Integer.parseInt(matchDto.getInfo().getGameDuration());
                int minutes = gameDuration / 60;
                int seconds = gameDuration % 60;
                String timeFormatted = String.format("%d:%02d", minutes, seconds); // 시간:분 형식으로 포맷팅
                matchDto.getInfo().setConvertTime(timeFormatted);
            }
        }catch (Exception e){
            log.debug("convertMillisTOTime Error : "+e.getMessage());
        }
        return matchDtoList;
    }

    /**
     * 한 매치에 대한 밀리세컨드를 시간:분 형태로 변환
     */
    private MatchDto convertMillisToTimeOnd(MatchDto matchDto){
        try {
                int gameDuration = Integer.parseInt(matchDto.getInfo().getGameDuration());
                int minutes = gameDuration / 60;
                int seconds = gameDuration % 60;
                String timeFormatted = String.format("%d:%02d", minutes, seconds); // 시간:분 형식으로 포맷팅
                matchDto.getInfo().setConvertTime(timeFormatted);
        }catch (Exception e){
            log.debug("convertMillisTOTime Error : "+e.getMessage());
        }
        return matchDto;
    }

    /**
     * 현재 시간 기준으로 게임 끝난 시간을 뺀 날짜의 차이
     */
    private List<MatchDto> convertRestDay(List<MatchDto> matchDtoList){
        try {
            for(MatchDto matchDto : matchDtoList){
                long gameEndStamp  = Long.parseLong(matchDto.getInfo().getGameEndTimestamp());
                long nowStamp = System.currentTimeMillis();
                long durationInMillis = nowStamp - gameEndStamp;
                long hours = durationInMillis / 1000 / 60 / 60;
                long days = hours / 24;
                if(days == 0){
                    matchDto.getInfo().setRestHours(String.valueOf(hours));
                }else {
                    matchDto.getInfo().setRestDays(String.valueOf(days));
                }
            }
        }catch (Exception e){
            log.debug("convertRestDay Error : "+e.getMessage());
        }
        return matchDtoList;
    }

    /**
     * User 기준 팀ID, 적이름, 승패여부 수집
     */
    private List<MatchDto> setTeamIdAndEnemyNameAndWinOrLose(List<MatchDto> matchDtoList, String userName){
        for(MatchDto matchDto : matchDtoList){
            matchDto.setUserName(userName);
            for(ParticipantDto participantDto : matchDto.getInfo().getParticipants()){
                if(participantDto.getSummonerName().equals(userName)){
                    matchDto.getInfo().setUserTeamId(participantDto.getTeamId());
                    matchDto.getInfo().setUserWinOrLose(participantDto.getWin());
                }else {
                    matchDto.getInfo().setEnemyUserName(participantDto.getSummonerName());
                }
            }
        }
        return matchDtoList;
    }

    /**
     * 스펠 정보 수집
     */
    public List<MatchDto> getSpellInfo(String version, List<MatchDto> matchInfoList) {

        SpellVO spellVO = null;
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
            log.debug("getMatchInfo Error : " + e.getMessage());
            spellVO = new SpellVO();
        }
        try {
            for(MatchDto matchDto : matchInfoList){
                for(ParticipantDto participantDto : matchDto.getInfo().getParticipants()){
                    int indexNum = 0;
                    for(String key : spellVO.getData().keySet()){
                        if(indexNum == Integer.parseInt(participantDto.getSummoner1Id())) participantDto.setSpell1(spellVO.getData().get(key).getImage().getFull());
                        if(indexNum == Integer.parseInt(participantDto.getSummoner2Id())) participantDto.setSpell2(spellVO.getData().get(key).getImage().getFull());
                        indexNum++;
                    }
                }
            }
        }catch (NullPointerException e){
            log.debug("getMatchInfo Error For NullPointerException : "+ e.getMessage());
        }catch (Exception e){
            log.debug("getMatchInfo Error2 : "+ e.getMessage());
        }
        return matchInfoList;
    }

    private LolInfoVO selectOrInsertUserInfo(String userName , int countUserInfo){
        LolInfoVO userInfo = null;
        try {
            if(countUserInfo > 0){
                userInfo = lolService.findUserInfo(userName);
            }else {
                userInfo = findUserInfo(userName).block();
                lolService.insertUserInfo(userInfo);
            }
        }catch (Exception e){
            log.debug("selectOrInsertUserInfo Error : "+e.getMessage());
        }

        return userInfo;
    }
}
