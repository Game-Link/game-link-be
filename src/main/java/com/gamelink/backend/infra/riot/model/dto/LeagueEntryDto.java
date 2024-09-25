package com.gamelink.backend.infra.riot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeagueEntryDto {

    private String leagueId;

    private String summonerId;

    private String queueType;

    private String tier;

    private String rank;

    private int leaguePoints;

    private int wins;

    private int losses;

    private boolean hotStreak;

    private boolean veteran;

    private boolean freshBlood;

    private boolean inactive;

    private MiniSeriesDto miniSeries;
}
