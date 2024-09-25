package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.infra.riot.model.entity.RankQueue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

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

    @Schema(description = "장기간 유지 여부")
    private final boolean veteran;

    @Schema(description = "비활성 여부")
    private final boolean inactive;

    @Schema(description = "신규 여부")
    private final boolean freshBlood;

    @Schema(description = "연승 여부")
    private final boolean hotStreak;

    public ResponseSummonerTeamRankDto(RankQueue rankQueue, String rankImageUrl) {
        this.rankImageUrl = rankImageUrl;
        this.tier = rankQueue.getTier();
        this.rank = rankQueue.getRankLevel();
        this.leaguePoints = rankQueue.getLeaguePoints();
        this.wins = rankQueue.getWins();
        this.losses = rankQueue.getLosses();
        this.veteran = rankQueue.isVeteran();
        this.inactive = rankQueue.isInactive();
        this.freshBlood = rankQueue.isFreshBlood();
        this.hotStreak = rankQueue.isHotStreak();
    }
}
