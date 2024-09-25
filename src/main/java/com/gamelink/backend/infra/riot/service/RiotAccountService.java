package com.gamelink.backend.infra.riot.service;

import com.gamelink.backend.infra.riot.model.dto.MatchDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerInfoDto;

import java.util.List;
import java.util.UUID;

public interface RiotAccountService {
    ResponseSummonerInfoDto getRiotUserInfo(UUID userSubId);

    void registerRiotAccountInfo(String gameName, String tagLine, UUID userSubId);

    void refreshRiotAccountInfo(UUID userSubId);

    void changeRiotAccountInfo(String gameName, String tagLine, UUID userSubId);

    List<MatchDto> getRiotMatchInfo(UUID userSubId);
}
