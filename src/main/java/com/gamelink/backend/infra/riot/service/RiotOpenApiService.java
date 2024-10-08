package com.gamelink.backend.infra.riot.service;

import com.gamelink.backend.infra.riot.model.dto.AccountDto;
import com.gamelink.backend.infra.riot.model.dto.LeagueEntryDto;
import com.gamelink.backend.infra.riot.model.dto.MatchDto;
import com.gamelink.backend.infra.riot.model.dto.SummonerDto;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;

import java.util.List;
import java.util.Set;

public interface RiotOpenApiService {
    AccountDto getAccountDto(String gameName, String tagLine);

    SummonerDto getSummonerDto(String puuid);

    Set<LeagueEntryDto> getLeagueInfo(RiotUser riotUser);

    String getProfileUrl(int profileIconId);

    String getRankImageUrl(RankQueue rankQueue);

    List<MatchDto> getMatchInfoList(String puuid, int start);

    List<String> getMatchIdList(String puuid, int start, int count);

    MatchDto getMatchInfo(String matchId);
}
