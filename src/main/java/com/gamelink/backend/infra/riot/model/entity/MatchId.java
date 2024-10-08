package com.gamelink.backend.infra.riot.model.entity;

import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_id")
public class MatchId extends BaseEntity {

    @JoinColumn(name = "riot_user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RiotUser riotUser;

    private String matchId;

    public MatchId(RiotUser riotUser, String matchId) {
        this.riotUser = riotUser;
        this.matchId = matchId;
    }
}
