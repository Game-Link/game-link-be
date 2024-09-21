package com.gamelink.backend.infra.riot.model;

import lombok.Getter;

@Getter
public enum RankType {
    SOLO_RANK("솔로랭크"),
    TEAM_RANK("자유랭크");

    private final String name;

    RankType(String name) {
        this.name = name;
    }
}
