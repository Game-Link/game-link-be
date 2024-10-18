package com.gamelink.backend.infra.riot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.infra.riot.exception.UriErrorException;
import com.gamelink.backend.infra.riot.model.dto.*;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiotOpenApiServiceImpl implements RiotOpenApiService {

    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private final AWSObjectStorageService s3service;

    @Value("${riot.api-key}")
    private String apiKey;

    @Value("${riot.asia.request-url}")
    private String asiaRequestUrl;

    @Value("${riot.kr.request-url}")
    private String krRequestUrl;

    @Value("${riot.dragon.version}")
    private String dataVersion;

    @Value("${riot.kr.profile-url}")
    private String profileUrl;

    @Override
    public AccountDto getAccountDto(String gameName, String tagLine) {
        String encodedGameName = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        String encodedTagLine = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);

        URI uri;
        try {
            uri = new URI(asiaRequestUrl + "/riot/account/v1/accounts/by-riot-id/"
                    + encodedGameName + "/" + encodedTagLine);
        } catch (URISyntaxException e) {
            throw new UriErrorException();
        }

        return webClient.get()
                .uri(uri)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }

    @Override
    public SummonerDto getSummonerDto(String puuid) {
        return webClient.get()
                .uri(krRequestUrl + "/lol/summoner/v4/summoners/by-puuid/" + puuid)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToMono(SummonerDto.class)
                .block();
    }

    @Override
    public Set<LeagueEntryDto> getLeagueInfo(RiotUser riotUser) {
        return webClient.get()
                .uri(krRequestUrl + "/lol/league/v4/entries/by-summoner/" + riotUser.getSummonerId())
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToFlux(LeagueEntryDto.class)
                .collect(Collectors.toSet())
                .block();
    }

    @Override
    public String getProfileUrl(int profileIconId) {
        return String.format(profileUrl, dataVersion, profileIconId);
    }

    @Override
    public String getRankImageUrl(RankQueue rankQueue) {
        if (rankQueue.getTier() == null) {
            return null;
        }
        return s3service.getImageUrl(rankQueue.getTier().toLowerCase() + ".png");
    }

    @Override
    public List<MatchDto> getMatchInfoList(String puuid, int start) {
        List<MatchDto> matchInfoList = new ArrayList<>();

        List<String> matchIds = webClient.get()
                .uri(asiaRequestUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=5")
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToFlux(String.class)
                .collect(Collectors.toList())
                .block();

        if (matchIds != null && matchIds.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] ids = matchIds.get(0).replaceAll("[\\[\\]\"]", "").split(",");
            for (String matchId : ids) {
                log.info("matchId : {}", matchId);
                matchInfoList.add(getMatchInfo(matchId));
            }
        }
        return matchInfoList;
    }

    @Override
    public List<String> getMatchIdList(String puuid, int start, int count) {
        List<String> matchIds = webClient.get()
                .uri(asiaRequestUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToFlux(String.class)
                .collect(Collectors.toList())
                .block();
        String[] ids = matchIds.get(0).replaceAll("[\\[\\]\"]", "").split(",");
        return Arrays.asList(ids);
    }

    @Override
    public List<String> getRankMatchIdList(String puuid, int start, int count) {
        List<String> rankMatchIds = webClient.get()
                .uri(asiaRequestUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?type=ranked&start=" + start + "&count=" + count)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToFlux(String.class)
                .collect(Collectors.toList())
                .block();
        String[] rankIds = rankMatchIds.get(0).replaceAll("[\\[\\]\"]", "").split(",");
        return Arrays.asList(rankIds);
    }

    @Override
    public MatchDto getMatchInfo(String matchId) {
        return webClient.get()
                .uri(asiaRequestUrl + "/lol/match/v5/matches/" + matchId)
                .header("X-Riot-Token", apiKey)
                .retrieve()
                .bodyToMono(MatchDto.class)
                .block();
    }
}
