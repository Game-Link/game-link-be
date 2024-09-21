package com.gamelink.backend.infra.riot.repository.custom;

import com.gamelink.backend.infra.riot.model.entity.queuetype.TeamRank;

import java.util.Optional;
import java.util.UUID;

public interface CustomTeamRankRepository {
    Optional<TeamRank> findOneByRiotUserSubId(UUID subId);
}
