package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.infra.riot.model.dto.CacheChampionDto;
import com.gamelink.backend.infra.riot.model.dto.CacheKdaDto;
import com.gamelink.backend.infra.riot.model.dto.PersistChampionDto;
import com.gamelink.backend.infra.riot.model.dto.PersistKdaDto;
import com.gamelink.backend.infra.riot.model.dto.response.PersistMatchDataDto;
import com.gamelink.backend.infra.riot.model.dto.response.CacheMatchDataDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MatchIdCacheRepository {

    void putMatchId(List<PersistMatchDataDto> matches, UUID riotUserSubId, boolean isNew);

    void putRankMatchId(List<PersistMatchDataDto> matches, UUID riotUserSubId, boolean isNew, boolean isSolo);

    boolean existMatchId(UUID riotUserSubId);

    List<CacheMatchDataDto> getMatchId(UUID riotUserSubId, int start);

    CacheMatchDataDto getLastMatchId(UUID riotUserSubId);

    void putKdaAndChampionInfo(PersistKdaDto totalKdaDto, PersistKdaDto soloKdaDto, PersistKdaDto teamKdaDto, List<PersistChampionDto> totalChampionDto, List<PersistChampionDto> soloChampionDto, List<PersistChampionDto> teamChampionDto, UUID riotUserSubId);

    CacheKdaDto getTotalKda(UUID riotUserSubId);

    CacheKdaDto getSoloKda(UUID riotUserSubId);

    CacheKdaDto getTeamKda(UUID riotUserSubId);

    void deleteAndUpdateKda(PersistKdaDto totalKdaDto, PersistKdaDto soloKdaDto, PersistKdaDto teamKdaDto, UUID riotUserSubId);

    void deleteAndUpdateChampion(List<PersistChampionDto> totalChampionDto, List<PersistChampionDto> soloChampionDto, List<PersistChampionDto> teamChampionDto, UUID subId);

    List<CacheChampionDto> getTotalChampion(UUID riotUserSubId);

    List<CacheChampionDto> getSoloChampion(UUID riotUserSubId);

    List<CacheChampionDto> getTeamChampion(UUID riotUserSubId);

}
