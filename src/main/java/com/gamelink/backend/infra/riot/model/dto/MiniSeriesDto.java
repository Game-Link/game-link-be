package com.gamelink.backend.infra.riot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MiniSeriesDto {

    private int losses;

    private String progress;

    private int target;

    private int wins;
}
