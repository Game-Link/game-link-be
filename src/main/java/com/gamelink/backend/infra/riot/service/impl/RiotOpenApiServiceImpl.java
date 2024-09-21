package com.gamelink.backend.infra.riot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.infra.riot.exception.UriErrorException;
import com.gamelink.backend.infra.riot.model.dto.response.AccountDto;
import com.gamelink.backend.infra.riot.model.dto.response.LeagueEntryDto;
import com.gamelink.backend.infra.riot.model.dto.response.SummonerDto;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Value("${riot.api-key}")
    private String apiKey;

    @Value("${riot.asia.request-url}")
    private String asiaRequestUrl;

    @Value("${riot.kr.request-url}")
    private String krRequestUrl;

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
}
