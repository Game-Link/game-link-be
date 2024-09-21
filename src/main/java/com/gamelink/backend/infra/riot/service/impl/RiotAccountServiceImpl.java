package com.gamelink.backend.infra.riot.service.impl;

import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.infra.riot.exception.*;
import com.gamelink.backend.infra.riot.model.dto.response.*;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.model.entity.queuetype.SoloRank;
import com.gamelink.backend.infra.riot.model.entity.queuetype.TeamRank;
import com.gamelink.backend.infra.riot.repository.RiotUserRepository;
import com.gamelink.backend.infra.riot.repository.SoloRankRepository;
import com.gamelink.backend.infra.riot.repository.TeamRankRepository;
import com.gamelink.backend.infra.riot.service.RiotAccountService;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiotAccountServiceImpl implements RiotAccountService {

    private final RiotUserRepository riotUserRepository;
    private final SoloRankRepository soloRankRepository;
    private final TeamRankRepository teamRankRepository;
    private final UserRepository userRepository;

    private final RiotOpenApiService openApiService;

    @Override
    @Transactional
    public void registerRiotAccountInfo(String gameName, String tagLine, UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId)
                .orElseThrow(UserNotFoundException::new);

        AccountDto accountDto = openApiService.getAccountDto(gameName, tagLine);
        SummonerDto summonerDto = openApiService.getSummonerDto(accountDto.getPuuid());

        RiotUser riotUser = RiotUser.convertFromAccountAndSummonerDto(user, accountDto, summonerDto);

        openApiService.getLeagueInfo(riotUser).forEach(dto -> {
            switch (dto.getQueueType()) {
                case "RANKED_SOLO_5x5":
                    SoloRank soloRank = SoloRank.convertFromLeagueEntryDto(riotUser, dto);
                    soloRank.changeRiotUser(riotUser);
                    break;
                case "RANKED_FLEX_SR":
                    TeamRank teamRank = TeamRank.convertFromLeagueEntryDto(riotUser, dto);
                    teamRank.changeRiotUser(riotUser);
                    break;
                default:
                    break;
            }
        });
        riotUserRepository.save(riotUser);
    }

    @Override
    public ResponseSummonerInfoDto getRiotUserInfo(UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        if (!riotUser.getUser().getSubId().equals(userSubId)) {
            throw new RiotUserNotMatchException();
        }

        final ResponseSummonerSoloRankDto[] soloRankDtos = new ResponseSummonerSoloRankDto[1];
        final ResponseSummonerTeamRankDto[] teamRankDtos = new ResponseSummonerTeamRankDto[1];

        riotUser.getQueues().forEach(queue -> {
            if (queue instanceof SoloRank soloRank) {
                soloRankDtos[0] = new ResponseSummonerSoloRankDto(soloRank);
            } else if (queue instanceof TeamRank teamRank) {
                teamRankDtos[0] = new ResponseSummonerTeamRankDto(teamRank);
            }
        });

        return new ResponseSummonerInfoDto(
                riotUser,
                soloRankDtos[0],
                teamRankDtos[0]
        );
    }

    @Override
    @Transactional
    public void refreshRiotAccountInfo(UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);
        SoloRank soloRank = soloRankRepository.findOneByRiotUserSubId(riotUser.getSubId())
                .orElseThrow(SoloRankNotFoundException::new);
        TeamRank teamRank = teamRankRepository.findOneByRiotUserSubId(riotUser.getSubId())
                .orElseThrow(TeamRankNotFoundException::new);

        AccountDto accountDto = openApiService.getAccountDto(riotUser.getSummonerName(), riotUser.getSummonerTag());
        SummonerDto summonerDto = openApiService.getSummonerDto(accountDto.getPuuid());

        riotUser.changeInfo(accountDto, summonerDto);
        openApiService.getLeagueInfo(riotUser).forEach(dto -> {
            switch (dto.getQueueType()) {
                case "RANKED_SOLO_5x5":
                    soloRank.changeInfo(dto);
                    break;
                case "RANKED_FLEX_SR":
                    teamRank.changeInfo(dto);
                    break;
                default:
                    break;
            }
        });
    }
}
