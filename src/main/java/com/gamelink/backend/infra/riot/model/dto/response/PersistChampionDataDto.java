package com.gamelink.backend.infra.riot.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PersistChampionDataDto {

    @Schema(description = "챔피언 이름")
    private final String championName;

    @Schema(description = "챔피언 이미지 URL")
    private final String championImageUrl;

    @Schema(description = "챔피언 플레이 횟수")
    private final int playCount;

    @Schema(description = "챔피언 승리 횟수")
    private final int winCount;

    @Schema(description = "챔피언 패배 횟수")
    private final int loseCount;

    @Schema(description = "챔피언 승률")
    private final double winRate;

    @Schema(description = "챔피언 KDA")
    private final double kda;

    @Schema(description = "챔피언 킬")
    private final double kill;

    @Schema(description = "챔피언 데스")
    private final double death;

    @Schema(description = "챔피언 어시스트")
    private final double assist;

    public PersistChampionDataDto(String championName, String championImageUrl, int playCount, int winCount, int loseCount, double winRate, double kda, double kill, double death, double assist) {
        this.championName = championName;
        this.championImageUrl = championImageUrl;
        this.playCount = playCount;
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.winRate = winRate;
        this.kda = kda;
        this.kill = kill;
        this.death = death;
        this.assist = assist;
    }
}
