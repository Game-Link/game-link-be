package com.gamelink.backend.infra.riot.service.impl;

import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.infra.riot.exception.*;
import com.gamelink.backend.infra.riot.model.RankType;
import com.gamelink.backend.infra.riot.model.dto.response.*;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.repository.RankQueueRepository;
import com.gamelink.backend.infra.riot.repository.RiotUserRepository;
import com.gamelink.backend.infra.riot.service.RiotAccountService;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiotAccountServiceImpl implements RiotAccountService {

    private final RiotUserRepository riotUserRepository;
    private final UserRepository userRepository;

    private final RiotOpenApiService openApiService;
    private final RankQueueRepository rankQueueRepository;

    @Override
    @Transactional
    public void registerRiotAccountInfo(String gameName, String tagLine, UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId)
                .orElseThrow(UserNotFoundException::new);

        AccountDto accountDto = openApiService.getAccountDto(gameName, tagLine);
        SummonerDto summonerDto = openApiService.getSummonerDto(accountDto.getPuuid());

        RiotUser riotUser = RiotUser.convertFromAccountAndSummonerDto(user, accountDto, summonerDto);
        if (riotUserRepository.findOneByGameNameAndTagLine(gameName, tagLine).isPresent()) {
            throw new RiotUserAlreadyExistException();
        }

        openApiService.getLeagueInfo(riotUser).forEach(dto -> {
            if (dto.getTier() == null || dto.getRank() == null) {
                return;
            }
            RankQueue rankQueue = RankQueue.convertFromLeagueEntryDto(riotUser, dto);
            rankQueue.changeRiotUser(riotUser);
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

        return new ResponseSummonerInfoDto(
                riotUser,
                riotUser.getQueues().stream().filter(RankQueue::isSoloRank).filter(RankQueue::isActive)
                        .findFirst().map(ResponseSummonerSoloRankDto::new).orElse(null),
                riotUser.getQueues().stream().filter(RankQueue::isTeamRank).filter(RankQueue::isActive)
                        .findFirst().map(ResponseSummonerTeamRankDto::new).orElse(null)
        );
    }

    @Override
    @Transactional
    public void refreshRiotAccountInfo(UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        AccountDto accountDto = openApiService.getAccountDto(riotUser.getSummonerName(), riotUser.getSummonerTag());
        SummonerDto summonerDto = openApiService.getSummonerDto(accountDto.getPuuid());

        riotUser.changeInfo(accountDto, summonerDto);

        Set<LeagueEntryDto> leagueInfo = openApiService.getLeagueInfo(riotUser);

        List<RankQueue> queues = riotUser.getQueues();

        leagueInfo.forEach(dto -> {
            for (RankQueue queue : queues) {
                if (dto.getQueueType().equals("RANKED_SOLO_5x5") && queue.getRankType() == RankType.SOLO_RANK) {
                    queue.changeInfo(dto);
                } else if (dto.getQueueType().equals("RANKED_FLEX_SR") && queue.getRankType() == RankType.TEAM_RANK) {
                    queue.changeInfo(dto);
                }
            }
        });
        riotUserRepository.save(riotUser);
    }

    @Override
    @Transactional
    public void changeRiotAccountInfo(String gameName, String tagLine, UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        if (gameName.isEmpty() && tagLine.isEmpty()) {
            throw new ParameterNotFoundException("최소 하나의 값은 입력해야 합니다.");
        }
        String newName = gameName.isEmpty() ? riotUser.getSummonerName() : gameName;
        String newTag = tagLine.isEmpty() ? riotUser.getSummonerTag() : tagLine;

        AccountDto accountDto = openApiService.getAccountDto(newName, newTag);
        SummonerDto summonerDto = openApiService.getSummonerDto(accountDto.getPuuid());

        riotUser.changeInfo(accountDto, summonerDto);
        rankQueueRepository.findAllByRiotUserSubId(riotUser.getSubId())
                .forEach(RankQueue::changeToInactive);

        openApiService.getLeagueInfo(riotUser).forEach(dto -> {
            if (dto.getTier() == null || dto.getRank() == null) {
                return;
            }
            RankQueue rankQueue = RankQueue.convertFromLeagueEntryDto(riotUser, dto);
            rankQueue.changeRiotUser(riotUser);
        });
        riotUserRepository.save(riotUser);
    }
}
