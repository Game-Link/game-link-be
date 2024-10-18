package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.infra.riot.model.dto.CacheChampionDto;
import com.gamelink.backend.infra.riot.model.dto.CacheKdaDto;
import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "소환사 팀 랭크 정보")
public class ResponseSummonerTeamRankDto {

    @Schema(description = "티어 이미지 Url")
    private final String rankImageUrl;

    @Schema(description = "티어")
    private final String tier;

    @Schema(description = "랭크")
    private final String rank;

    @Schema(description = "리그 포인트")
    private final int leaguePoints;

    @Schema(description = "승리 횟수")
    private final int wins;

    @Schema(description = "패배 횟수")
    private final int losses;

    @Schema(description = "승률")
    private final double winRate;

    @Schema(description = "평균 K/DA")
    private final double kda;

    @Schema(description = "평균 킬")
    private final double avgKills;

    @Schema(description = "평균 데스")
    private final double avgDeaths;

    @Schema(description = "평균 어시스트")
    private final double avgAssists;

    @Schema(description = "전체 게임 평균 CS")
    private final double avgCs;

    @Schema(description = "베스트 3 챔피언 정보")
    private final List<ResponseChampionInfoDto> best3champions;

    @Schema(description = "장기간 유지 여부")
    private final boolean veteran;

    @Schema(description = "비활성 여부")
    private final boolean inactive;

    @Schema(description = "신규 여부")
    private final boolean freshBlood;

    @Schema(description = "연승 여부")
    private final boolean hotStreak;

    public ResponseSummonerTeamRankDto(RankQueue rankQueue, String rankImageUrl, CacheKdaDto teamKdaDto, List<CacheChampionDto> teamChampion) {
        this.rankImageUrl = rankImageUrl;
        this.tier = rankQueue.getTier();
        this.rank = rankQueue.getRankLevel();
        this.leaguePoints = rankQueue.getLeaguePoints();
        this.wins = rankQueue.getWins();
        this.losses = rankQueue.getLosses();
        this.winRate = (teamKdaDto.getWins() != 0) ? (double) rankQueue.getWins() / (rankQueue.getWins() + rankQueue.getLosses()) : 0;
        this.kda = (teamKdaDto.getKills() + teamKdaDto.getAssists() == 0) ? 0 : (double) (teamKdaDto.getKills() + teamKdaDto.getAssists()) / teamKdaDto.getDeaths();
        this.avgKills = (teamKdaDto.getKills() != 0) ? (double) teamKdaDto.getKills() / teamKdaDto.getMatchCount() : 0;
        this.avgDeaths = (teamKdaDto.getDeaths() != 0) ? (double) teamKdaDto.getDeaths() / teamKdaDto.getMatchCount() : 0;
        this.avgAssists = (teamKdaDto.getAssists() != 0) ? (double) teamKdaDto.getAssists() / teamKdaDto.getMatchCount() : 0;
        this.avgCs = (teamKdaDto.getCs() != 0) ? (double) teamKdaDto.getCs() / teamKdaDto.getMatchCount() : 0;
        this.best3champions = ResponseChampionInfoDto.list3BestOf(teamChampion);
        this.veteran = rankQueue.isVeteran();
        this.inactive = rankQueue.isInactive();
        this.freshBlood = rankQueue.isFreshBlood();
        this.hotStreak = rankQueue.isHotStreak();
    }
}
