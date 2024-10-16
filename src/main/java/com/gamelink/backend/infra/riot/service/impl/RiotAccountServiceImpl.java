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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
        LocalDateTime start = LocalDateTime.now();
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
        for (int i = 0; i < 3; i++) {
            List<String> matchIdList = openApiService.getMatchIdList(riotUser.getPuuid(), i * size, size);
            matchIdList.forEach(matchId -> matchIds.add(new MatchId(riotUser, matchId)));
        }

        // 최근 20개의 매치 데이터를 조회하여 캐시에 저장 및 전적 데이터 재가공
        // MultiValueMap : 챔피언 이름을 key로, value는 [게임 수, 승리 수, 패배 수, 킬 수, 데스 수, 어시스트 수]로 저장
        List<PersistMatchDataDto> cacheDatas = new ArrayList<>();
        int[] sumData = new int[7];
        Map<String, int[]> championData = new HashMap<>();
        matchIds.subList(0, 20).forEach(
                matchId -> {
                    MatchDto matchDto = openApiService.getMatchInfo(matchId.getMatchId());
                    matchDto.getInfo().getParticipants().forEach(participantDto -> {
                        if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                            PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                            cacheDatas.add(persistMatchDataDto);
                            addKdaData(sumData, persistMatchDataDto);
                            addChampionData(championData, persistMatchDataDto);
                        }
                    });
                }
        );

        // 최근 랭크 게임 20개의 매치 데이터를 조회하여 캐시에 저장
        List<PersistMatchDataDto> soloRankDatas = new ArrayList<>();
        List<PersistMatchDataDto> teamRankDatas = new ArrayList<>();
        int[] sumSoloData = new int[7];
        int[] sumTeamData = new int[7];
        Map<String, int[]> championSoloData = new HashMap<>();
        Map<String, int[]> championTeamData = new HashMap<>();
        List<String> rankMatchIdList = openApiService.getRankMatchIdList(riotUser.getPuuid(), 0, 20);
        rankMatchIdList.forEach(
                rankMatchId -> {
                    MatchDto matchDto = openApiService.getMatchInfo(rankMatchId);
                    matchDto.getInfo().getParticipants().forEach(participantDto -> {
                        if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                            if (matchDto.getInfo().getQueueId() == 420) {
                                PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                                addKdaData(sumSoloData, persistMatchDataDto);
                                addChampionData(championSoloData, persistMatchDataDto);
                                soloRankDatas.add(persistMatchDataDto);
                            } else if (matchDto.getInfo().getQueueId() == 440) {
                                PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                                addKdaData(sumTeamData, persistMatchDataDto);
                                addChampionData(championTeamData, persistMatchDataDto);
                                teamRankDatas.add(persistMatchDataDto);
                            }
                        }
                    });
                }
        );
        PersistKdaDto totalKdaDto = PersistKdaDto.of(sumData);
        PersistKdaDto soloKdaDto = PersistKdaDto.of(sumSoloData);
        PersistKdaDto teamKdaDto = PersistKdaDto.of(sumTeamData);

        List<PersistChampionDto> totalChampionDto = convertToChampionList(championData);
        List<PersistChampionDto> soloChampionDto = convertToChampionList(championSoloData);
        List<PersistChampionDto> teamChampionDto = convertToChampionList(championTeamData);

        Collections.reverse(cacheDatas);
        Collections.reverse(soloRankDatas);
        Collections.reverse(teamRankDatas);
        matchIdCacheRepository.putMatchId(cacheDatas, riotUser.getSubId(), true);
        matchIdCacheRepository.putRankMatchId(soloRankDatas, riotUser.getSubId(), true, true);
        matchIdCacheRepository.putRankMatchId(teamRankDatas, riotUser.getSubId(), true, false);
        matchIdCacheRepository.putKdaAndChampionInfo(totalKdaDto, soloKdaDto, teamKdaDto, totalChampionDto, soloChampionDto, teamChampionDto, riotUser.getSubId());
        matchIdRepository.saveAll(matchIds);
        riotUserRepository.save(riotUser);

        LocalDateTime end = LocalDateTime.now();
        log.info("총 소요시간: " + (end.getSecond() - start.getSecond()) + "초");
    }

    private static void addKdaData(int[] sumData, PersistMatchDataDto persistMatchDataDto) {
        // 0: 킬 수, 1: 데스 수, 2: 어시스트 수, 3: 게임 수, 4: 승리 수, 5: 패배 수, 6: CS 수
        sumData[0] += persistMatchDataDto.getKills();
        sumData[1] += persistMatchDataDto.getDeaths();
        sumData[2] += persistMatchDataDto.getAssists();
        sumData[3] += 1;
        if (persistMatchDataDto.isWin()) {
            sumData[4] += 1;
        } else {
            sumData[5] += 1;
        }
        sumData[6] += persistMatchDataDto.getTotalMinionsKilled();
    }

    private void addChampionData(Map<String,int[]> championData, PersistMatchDataDto matchDto) {
        String championName = matchDto.getChampionName();
        if (championData.get(championName) == null) {
            championData.put(championName, new int[]{matchDto.getKills(), matchDto.getDeaths(), matchDto.getAssists(), 1, matchDto.isWin() ? 1 : 0, matchDto.isWin() ? 0 : 1});
        } else {
            championData.put(championName, new int[] {
                    championData.get(championName)[0] + matchDto.getKills(),
                    championData.get(championName)[1] + matchDto.getDeaths(),
                    championData.get(championName)[2] + matchDto.getAssists(),
                    championData.get(championName)[3] + 1,
                    championData.get(championName)[4] + (matchDto.isWin() ? 1 : 0),
                    championData.get(championName)[5] + (matchDto.isWin() ? 0 : 1)
            });
        }
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
        CacheKdaDto totalKda = matchIdCacheRepository.getTotalKda(riotUser.getSubId());
        CacheKdaDto soloKda = matchIdCacheRepository.getSoloKda(riotUser.getSubId());
        CacheKdaDto teamKda = matchIdCacheRepository.getTeamKda(riotUser.getSubId());
        List<CacheChampionDto> totalChampion = matchIdCacheRepository.getTotalChampion(riotUser.getSubId());
        List<CacheChampionDto> soloChampion = matchIdCacheRepository.getSoloChampion(riotUser.getSubId());
        List<CacheChampionDto> teamChampion = matchIdCacheRepository.getTeamChampion(riotUser.getSubId());

        return new ResponseSummonerInfoDto(
                user,
                totalKda,
                totalChampion,
                s3service,
                riotUser,
                riotUser.getQueues().stream().filter(RankQueue::isSoloRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerSoloRankDto(rq, rankImageUrl, soloKda, soloChampion);
                        }).orElse(null),
                riotUser.getQueues().stream().filter(RankQueue::isTeamRank).filter(RankQueue::isActive)
                        .findFirst().map(rq -> {
                            String rankImageUrl = openApiService.getRankImageUrl(rq);
                            return new ResponseSummonerTeamRankDto(rq, rankImageUrl, teamKda, teamChampion);
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
        // RSO로 변경되면 삭제될 예정
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

        int[] totalKdaData;
        int[] soloKdaData;
        int[] teamKdaData;
        Map<String, int[]> totalChampion;
        Map<String, int[]> soloChampion;
        Map<String, int[]> teamChampion;

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
            CacheKdaDto totalKda = matchIdCacheRepository.getTotalKda(riotUser.getSubId());
            CacheKdaDto soloKda = matchIdCacheRepository.getSoloKda(riotUser.getSubId());
            CacheKdaDto teamKda = matchIdCacheRepository.getTeamKda(riotUser.getSubId());

            totalKdaData = new int[]{totalKda.getKills(), totalKda.getDeaths(), totalKda.getAssists(), totalKda.getMatchCount(), totalKda.getWins(), totalKda.getLosses()};
            soloKdaData = new int[]{soloKda.getKills(), soloKda.getDeaths(), soloKda.getAssists(), soloKda.getMatchCount(), soloKda.getWins(), soloKda.getLosses()};
            teamKdaData = new int[]{teamKda.getKills(), teamKda.getDeaths(), teamKda.getAssists(), teamKda.getMatchCount(), teamKda.getWins(), teamKda.getLosses()};

            totalChampion = convertToChampionMap(matchIdCacheRepository.getTotalChampion(riotUser.getSubId()));
            soloChampion = convertToChampionMap(matchIdCacheRepository.getSoloChampion(riotUser.getSubId()));
            teamChampion = convertToChampionMap(matchIdCacheRepository.getTeamChampion(riotUser.getSubId()));

            for(String matchId : matchIdList) {
                MatchDto matchDto = openApiService.getMatchInfo(matchId);
                matchDto.getInfo().getParticipants().forEach(participantDto -> {
                    if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                        PersistMatchDataDto persistMatchDataDto = new PersistMatchDataDto(matchDto, participantDto);
                        saveRightCacheDatas.add(persistMatchDataDto);
                        cacheDatas.add(PersistMatchDataDto.convertToCacheMatchDataDto(persistMatchDataDto));
                        addKdaData(totalKdaData, persistMatchDataDto);
                        addChampionData(totalChampion, persistMatchDataDto);
                        if (matchDto.getInfo().getQueueId() == 420) {
                            addKdaData(soloKdaData, persistMatchDataDto);
                            addChampionData(soloChampion, persistMatchDataDto);
                        } else if (matchDto.getInfo().getQueueId() == 440) {
                            addKdaData(teamKdaData, persistMatchDataDto);
                            addChampionData(teamChampion, persistMatchDataDto);
                        }
                    }
                });
            }
        }

        List<PersistChampionDto> totalChampionDto = convertToChampionList(totalChampion);
        List<PersistChampionDto> soloChampionDto = convertToChampionList(soloChampion);
        List<PersistChampionDto> teamChampionDto = convertToChampionList(teamChampion);

        matchIdCacheRepository.putMatchId(saveRightCacheDatas, riotUser.getSubId(), false);
        matchIdCacheRepository.deleteAndUpdateKda(PersistKdaDto.of(totalKdaData), PersistKdaDto.of(soloKdaData), PersistKdaDto.of(teamKdaData), riotUser.getSubId());
        matchIdCacheRepository.deleteAndUpdateChampion(totalChampionDto, soloChampionDto, teamChampionDto, riotUser.getSubId());
        return cacheDatas;
    }

    private List<PersistChampionDto> convertToChampionList(Map<String, int[]> championDtos) {
        return championDtos.entrySet().stream()
                .map(entry -> PersistChampionDto.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(PersistChampionDto::getMatchCount))
                .toList();
    }

    private Map<String, int[]> convertToChampionMap(List<CacheChampionDto> championDtos) {
        return championDtos.stream().collect(Collectors.toMap(
                CacheChampionDto::getChampionName,
                champion -> new int[] {
                        champion.getKills(),
                        champion.getDeaths(),
                        champion.getAssists(),
                        champion.getMatchCount(),
                        champion.getWins(),
                        champion.getLosses()
                }
        ));
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

        CacheKdaDto totalKda = matchIdCacheRepository.getTotalKda(riotUser.getSubId());
        CacheKdaDto soloKda = matchIdCacheRepository.getSoloKda(riotUser.getSubId());
        CacheKdaDto teamKda = matchIdCacheRepository.getTeamKda(riotUser.getSubId());

        // 매치 데이터를 조회하여 새로운 데이터만 캐시에 저장
        List<PersistMatchDataDto> myData = new ArrayList<>();
        List<MatchId> newMatchIds = new ArrayList<>();

        int[] newTotalKda = new int[]{totalKda.getKills(), totalKda.getDeaths(), totalKda.getAssists(), totalKda.getMatchCount(), totalKda.getWins(), totalKda.getLosses()};
        int[] newSoloKda = new int[]{soloKda.getKills(), soloKda.getDeaths(), soloKda.getAssists(), soloKda.getMatchCount(), soloKda.getWins(), soloKda.getLosses()};
        int[] newTeamKda = new int[]{teamKda.getKills(), teamKda.getDeaths(), teamKda.getAssists(), teamKda.getMatchCount(), teamKda.getWins(), teamKda.getLosses()};

        Map<String, int[]> totalChampion = convertToChampionMap(matchIdCacheRepository.getTotalChampion(riotUser.getSubId()));
        Map<String, int[]> soloChampion = convertToChampionMap(matchIdCacheRepository.getSoloChampion(riotUser.getSubId()));
        Map<String, int[]> teamChampion = convertToChampionMap(matchIdCacheRepository.getTeamChampion(riotUser.getSubId()));

        for(String matchId : newIds) {
            MatchDto matchDto = openApiService.getMatchInfo(matchId);
            matchDto.getInfo().getParticipants().forEach(participantDto -> {
                if (participantDto.getPuuid().equals(riotUser.getPuuid())) {
                    myData.add(new PersistMatchDataDto(matchDto, participantDto));
                    newMatchIds.add(new MatchId(riotUser, matchDto.getMetadata().getMatchId()));
                    addKdaData(newTotalKda, new PersistMatchDataDto(matchDto, participantDto));
                    addChampionData(totalChampion, new PersistMatchDataDto(matchDto, participantDto));
                    if (matchDto.getInfo().getQueueId() == 420) {
                        addKdaData(newSoloKda, new PersistMatchDataDto(matchDto, participantDto));
                        addChampionData(soloChampion, new PersistMatchDataDto(matchDto, participantDto));
                    } else if (matchDto.getInfo().getQueueId() == 440) {
                        addKdaData(newTeamKda, new PersistMatchDataDto(matchDto, participantDto));
                        addChampionData(teamChampion, new PersistMatchDataDto(matchDto, participantDto));
                    }
                }
            });
        }

        List<PersistChampionDto> totalChampionDto = convertToChampionList(totalChampion);
        List<PersistChampionDto> soloChampionDto = convertToChampionList(soloChampion);
        List<PersistChampionDto> teamChampionDto = convertToChampionList(teamChampion);

        matchIdRepository.saveAll(newMatchIds);
        Collections.reverse(myData);
        matchIdCacheRepository.putMatchId(myData, riotUser.getSubId(), true);
        matchIdCacheRepository.deleteAndUpdateKda(PersistKdaDto.of(newTotalKda), PersistKdaDto.of(newSoloKda), PersistKdaDto.of(newTeamKda), riotUser.getSubId());
        matchIdCacheRepository.deleteAndUpdateChampion(totalChampionDto, soloChampionDto, teamChampionDto, riotUser.getSubId());
    }
}
