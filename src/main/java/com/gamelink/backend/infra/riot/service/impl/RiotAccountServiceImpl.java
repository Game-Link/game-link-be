package com.gamelink.backend.infra.riot.service.impl;

import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.infra.riot.exception.*;
import com.gamelink.backend.infra.riot.model.RankType;
import com.gamelink.backend.infra.riot.model.dto.*;
import com.gamelink.backend.infra.riot.model.dto.AccountDto;
import com.gamelink.backend.infra.riot.model.dto.response.*;
import com.gamelink.backend.infra.riot.model.entity.MatchId;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.repository.MatchIdCacheRepository;
import com.gamelink.backend.infra.riot.repository.MatchIdRepository;
import com.gamelink.backend.infra.riot.repository.RankQueueRepository;
import com.gamelink.backend.infra.riot.repository.RiotUserRepository;
import com.gamelink.backend.infra.riot.service.RiotAccountService;
import com.gamelink.backend.infra.riot.service.RiotOpenApiService;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RiotAccountServiceImpl implements RiotAccountService {

    private final RiotUserRepository riotUserRepository;
    private final MatchIdRepository matchIdRepository;
    private final MatchIdCacheRepository matchIdCacheRepository;
    private final UserRepository userRepository;

    private final AWSObjectStorageService s3service;
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

        // 최근 300개의 매치 Id를 조회하여 저장
        List<MatchId> matchIds = new ArrayList<>();
        int size = 100;
        for(int i = 0; i < 3; i++) {
            List<String> matchIdList = openApiService.getMatchIdList(riotUser.getPuuid(), i * size, size);
            matchIdList.forEach(matchId -> matchIds.add(new MatchId(riotUser, matchId)));
        }

        // 최근 20개의 매치 데이터를 조회하여 캐시에 저장
        List<PersistMatchDataDto> cacheDatas = new ArrayList<>();
        matchIds.subList(0, 20).forEach(
                matchId -> {
                    MatchDto matchDto = openApiService.getMatchInfo(matchId.getMatchId());
                    matchDto.getInfo().getParticipants().forEach(participantDto -> {
                        if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                            PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                            cacheDatas.add(persistMatchDataDto);
                        }
                    });
                }
        );
        Collections.reverse(cacheDatas);
        matchIdCacheRepository.putMatchId(cacheDatas, riotUser.getSubId(), true);
        matchIdRepository.saveAll(matchIds);
        riotUserRepository.save(riotUser);
    }

    @Override
    public ResponseSummonerInfoDto getRiotUserInfo(UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId)
                .orElseThrow(UserNotFoundException::new);
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        if (!riotUser.getUser().getSubId().equals(userSubId)) {
            throw new RiotUserNotMatchException();
        }

        String profileUrl = openApiService.getProfileUrl(riotUser.getProfileIconId());

        return new ResponseSummonerInfoDto(
                user,
                s3service,
                riotUser,
                riotUser.getQueues().stream().filter(RankQueue::isSoloRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerSoloRankDto(rq, rankImageUrl);
                        }).orElse(null),
                riotUser.getQueues().stream().filter(RankQueue::isTeamRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerTeamRankDto(rq, rankImageUrl);
                        }).orElse(null),
                profileUrl
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

    @Override
    @Transactional
    public List<CacheMatchDataDto> getRiotMatchInfo(UUID userSubId, int start) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);

        // 1. MatchId를 Mysql에서 전체 조회
        // 2. Redis에서 start를 이용하여 range로 5개를 조회
        // 3-1. Redis에서 5개이면 그대로 리턴
        // 3-2. Redis에서 5개 미만이면 RiotApi를 통해 부족한 수만큼 호출하여 Redis에 저장 후 5개 리턴

        List<String> matchIds = matchIdRepository.findAllByRiotUserSubId(riotUser.getSubId())
                .stream().sorted((m1, m2) -> {
                    String num1 = m1.getMatchId().replace("KR_", "");
                    String num2 = m2.getMatchId().replace("KR_", "");
                    return Long.compare(Long.parseLong(num2), Long.parseLong(num1));
                }).map(MatchId::getMatchId)
                .toList();
        List<CacheMatchDataDto> cacheDatas = matchIdCacheRepository.getMatchId(riotUser.getSubId(), start);
        List<PersistMatchDataDto> saveRightCacheDatas = new ArrayList<>();

        if (cacheDatas.size() == 5) {
            return cacheDatas;
        } else {
            List<String> matchIdList;
            if (cacheDatas.isEmpty()) {
                CacheMatchDataDto lastCacheMatchId = matchIdCacheRepository.getLastMatchId(riotUser.getSubId());
                int lastMatchIndex = matchIds.indexOf(lastCacheMatchId.getMatchId());
                matchIdList = matchIds.subList(lastMatchIndex + 1, lastMatchIndex + 6);
            } else {
                String lastCacheMatchId = cacheDatas.get(cacheDatas.size() - 1).getMatchId();
                int lastMatchIndex = matchIds.indexOf(lastCacheMatchId);
                matchIdList = matchIds.subList(lastMatchIndex + 1, lastMatchIndex + 6 - cacheDatas.size());
            }

            for(String matchId : matchIdList) {
                MatchDto matchDto = openApiService.getMatchInfo(matchId);
                matchDto.getInfo().getParticipants().forEach(participantDto -> {
                    if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                        PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                        saveRightCacheDatas.add(persistMatchDataDto);
                        cacheDatas.add(PersistMatchDataDto.convertToCacheMatchDataDto(persistMatchDataDto));
                    }
                });
            }
        }
        matchIdCacheRepository.putMatchId(saveRightCacheDatas, riotUser.getSubId(), false);

        return cacheDatas;
    }

    @Override
    @Transactional
    public void refreshRiotMatchInfo(UUID userSubId) {
        RiotUser riotUser = riotUserRepository.findOneByUserSubId(userSubId)
                .orElseThrow(RiotUserNotFoundException::new);
        List<MatchId> matchIds = matchIdRepository.findAllByRiotUserSubId(riotUser.getSubId());

        List<String> matchIdListFromRiot = openApiService.getMatchIdList(riotUser.getPuuid(), 0, 100);

        // 중복되지 않은 최신 매치 Id만 newIds에 추가
        List<String> newIds = new ArrayList<>();
        matchIdListFromRiot.forEach(matchId -> {
            if (matchIds.stream().noneMatch(id -> id.getMatchId().equals(matchId))) {
                newIds.add(matchId);
            }
        });

        // 매치 데이터를 조회하여 새로운 데이터만 캐시에 저장
        List<PersistMatchDataDto> myData = new ArrayList<>();
        List<MatchId> newMatchIds = new ArrayList<>();
        for(String matchId : newIds) {
            MatchDto matchDto = openApiService.getMatchInfo(matchId);
            matchDto.getInfo().getParticipants().forEach(participantDto -> {
                if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                    myData.add(new PersistMatchDataDto(matchDto, participantDto));
                    newMatchIds.add(new MatchId(riotUser, matchDto.getMetadata().getMatchId()));
                }
            });
        }
        matchIdRepository.saveAll(newMatchIds);
        Collections.reverse(myData);
        matchIdCacheRepository.putMatchId(myData, riotUser.getSubId(), true);
    }
}
