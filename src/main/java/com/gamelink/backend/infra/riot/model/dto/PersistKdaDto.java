package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PersistKdaDto {

    @Schema(description = "총 킬수")
    private final int kills;

    @Schema(description = "총 데스수")
    private final int deaths;

    @Schema(description = "총 어시스트수")
    private final int assists;

    @Schema(description = "총 게임 수")
    private final int matchCount;

    @Schema(description = "총 승리 수")
    private final int wins;

    @Schema(description = "총 패배 수")
    private final int losses;

    @Schema(description = "총 CS 수")
    private final int cs;

    public PersistKdaDto(int kills, int deaths, int assists, int matchCount, int wins, int losses, int cs) {
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.matchCount = matchCount;
        this.wins = wins;
        this.losses = losses;
        this.cs = cs;
    }

    public static PersistKdaDto of(int[] sumData) {
        return new PersistKdaDto(sumData[0], sumData[1], sumData[2], sumData[3], sumData[4], sumData[5], sumData[6]);
    }
}
