package com.gamelink.backend.infra.riot.service;

import com.gamelink.backend.infra.riot.model.dto.response.AccountDto;
import com.gamelink.backend.infra.riot.model.dto.response.LeagueEntryDto;
import com.gamelink.backend.infra.riot.model.dto.response.SummonerDto;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;

import java.util.Set;

public interface RiotOpenApiService {
    AccountDto getAccountDto(String gameName, String tagLine);

    SummonerDto getSummonerDto(String puuid);

    Set<LeagueEntryDto> getLeagueInfo(RiotUser riotUser);
}
