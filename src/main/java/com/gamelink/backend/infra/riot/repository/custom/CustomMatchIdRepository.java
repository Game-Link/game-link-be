package com.gamelink.backend.infra.riot.repository.custom;

import com.gamelink.backend.infra.riot.model.entity.MatchId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomMatchIdRepository {
    List<MatchId> findAllByRiotUserSubId(UUID subId);

    int countByRiotUserSubId(UUID riotUserSubId);
}
