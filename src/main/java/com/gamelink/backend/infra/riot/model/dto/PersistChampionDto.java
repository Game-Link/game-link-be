package com.gamelink.backend.infra.riot.model.dto;

import lombok.Getter;

@Getter
public class PersistChampionDto {

    private final String championName;

    private final int kills;

    private final int deaths;

    private final int assists;

    private final int matchCount;

    private final int wins;

    private final int losses;

    public PersistChampionDto(String championName, int kills, int deaths, int assists, int matchCount, int wins, int losses) {
        this.championName = championName;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.matchCount = matchCount;
        this.wins = wins;
        this.losses = losses;
    }

    public static PersistChampionDto of(String championName, int[] sumData) {
        return new PersistChampionDto(championName, sumData[0], sumData[1], sumData[2], sumData[3], sumData[4], sumData[5]);
    }

    public static CacheChampionDto of(PersistChampionDto persistChampionDto) {
        return new CacheChampionDto(persistChampionDto.getChampionName(), persistChampionDto.getKills(), persistChampionDto.getDeaths(), persistChampionDto.getAssists(), persistChampionDto.getMatchCount(), persistChampionDto.getWins(), persistChampionDto.getLosses());
    }
}
