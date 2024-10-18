package com.gamelink.backend.infra.riot.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheChampionDto {

    private final String championName;

    private final int kills;

    private final int deaths;

    private final int assists;

    private final int matchCount;

    private final int wins;

    private final int losses;
}
