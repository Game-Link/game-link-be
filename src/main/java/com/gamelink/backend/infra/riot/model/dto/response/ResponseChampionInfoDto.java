package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.infra.riot.model.dto.CacheChampionDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseChampionInfoDto {

    private final String championName;

    private final double kills;

    private final double deaths;

    private final double assists;

    private final double winRate;

    private final int wins;

    private final int losses;

    public ResponseChampionInfoDto(CacheChampionDto cacheDto) {
        this.championName = cacheDto.getChampionName();
        this.kills = (cacheDto.getKills() != 0) ? (double) cacheDto.getKills() / cacheDto.getMatchCount() : 0;
        this.deaths = (cacheDto.getDeaths() != 0) ? (double) cacheDto.getDeaths() / cacheDto.getMatchCount() : 0;
        this.assists = (cacheDto.getAssists() != 0) ? (double) cacheDto.getAssists() / cacheDto.getMatchCount() : 0;
        this.winRate = (cacheDto.getMatchCount() != 0) ? (double) cacheDto.getWins() / cacheDto.getMatchCount() : 0;
        this.wins = cacheDto.getWins();
        this.losses = cacheDto.getLosses();
    }

    public static List<ResponseChampionInfoDto> list3BestOf(List<CacheChampionDto> cacheDtos) {
        return cacheDtos.stream().limit(3).map(ResponseChampionInfoDto::new).toList();
    }
}
