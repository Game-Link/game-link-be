package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.infra.riot.model.dto.response.PersistMatchDataDto;
import com.gamelink.backend.infra.riot.model.dto.response.CacheMatchDataDto;

import java.util.List;
import java.util.UUID;

public interface MatchIdCacheRepository {

    void putMatchId(List<PersistMatchDataDto> matches, UUID riotUserSubId, boolean isNew);

    boolean existMatchId(UUID riotUserSubId);

    List<CacheMatchDataDto> getMatchId(UUID riotUserSubId, int start);

    CacheMatchDataDto getLastMatchId(UUID riotUserSubId);
}
