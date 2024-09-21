package com.gamelink.backend.infra.riot.service;

import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerInfoDto;

import java.util.UUID;

public interface RiotAccountService {
    ResponseSummonerInfoDto getRiotUserInfo(UUID userSubId);

    void registerRiotAccountInfo(String gameName, String tagLine, UUID userSubId);

    void refreshRiotAccountInfo(UUID userSubId);
}
