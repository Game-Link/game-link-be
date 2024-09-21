package com.gamelink.backend.infra.riot.repository.custom;

import com.gamelink.backend.infra.riot.model.entity.queuetype.SoloRank;

import java.util.Optional;
import java.util.UUID;

public interface CustomSoloRankRepository {
    Optional<SoloRank> findOneByRiotUserSubId(UUID riotUserSubId);
}
