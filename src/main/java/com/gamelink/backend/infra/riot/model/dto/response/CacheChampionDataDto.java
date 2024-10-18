package com.gamelink.backend.infra.riot.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CacheChampionDataDto {

    @Schema(description = "챔피언 이름")
    private String championName;

    @Schema(description = "챔피언 이미지 URL")
    private String championImageUrl;

    @Schema(description = "챔피언 플레이 횟수")
    private int playCount;

    @Schema(description = "챔피언 승리 횟수")
    private int winCount;

    @Schema(description = "챔피언 패배 횟수")
    private int loseCount;

    @Schema(description = "챔피언 승률")
    private double winRate;

    @Schema(description = "챔피언 KDA")
    private double kda;

    @Schema(description = "챔피언 킬")
    private double kill;

    @Schema(description = "챔피언 데스")
    private double death;

    @Schema(description = "챔피언 어시스트")
    private double assist;
}
