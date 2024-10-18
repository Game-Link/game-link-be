package com.gamelink.backend.infra.riot.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheKdaDto {

    private final int wins;

    private final int losses;

    private final int kills;

    private final int deaths;

    private final int assists;

    private final int matchCount;

    private final int cs;
}
