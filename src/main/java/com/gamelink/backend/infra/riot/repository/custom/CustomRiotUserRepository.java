package com.gamelink.backend.infra.riot.repository.custom;

import com.gamelink.backend.infra.riot.model.entity.RiotUser;

import java.util.Optional;
import java.util.UUID;

public interface CustomRiotUserRepository {
    Optional<RiotUser> findOneByUserSubId(UUID userSubId);

    Optional<RiotUser> findOneByGameNameAndTagLine(String gameName, String tagLine);
}
