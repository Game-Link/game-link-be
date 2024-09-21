package com.gamelink.backend.infra.riot.model.entity;

import com.gamelink.backend.global.base.BaseEntity;
import com.gamelink.backend.infra.riot.model.RankQueueStatus;
import com.gamelink.backend.infra.riot.model.RankType;
import com.gamelink.backend.infra.riot.model.dto.response.LeagueEntryDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Entity
@Table(name = "rank_queue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankQueue extends BaseEntity {

    @JoinColumn(name = "riot_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RiotUser riotUser;

    @Enumerated(STRING)
    private RankType rankType;

    @NotNull
    private int leaguePoints;

    private String tier;

    private String rankLevel;

    @NotNull
    private int wins;

    @NotNull
    private int losses;

    @NotNull
    private boolean veteran;

    @NotNull
    private boolean inactive;

    @NotNull
    private boolean freshBlood;

    @NotNull
    private boolean hotStreak;

    @Enumerated(STRING)
    private RankQueueStatus status;

    public RankQueue(RiotUser riotUser, RankType rankType, int leaguePoints, String tier, String rankLevel, int wins, int losses, boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        this.riotUser = riotUser;
        this.rankType = rankType;
        this.leaguePoints = leaguePoints;
        this.tier = tier;
        this.rankLevel = rankLevel;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
        this.status = RankQueueStatus.ACTIVE;
    }

    public static RankQueue convertFromLeagueEntryDto(RiotUser riotUser, LeagueEntryDto dto) {
        return new RankQueue(
                riotUser,
                dto.getQueueType().equals("RANKED_SOLO_5x5") ? RankType.SOLO_RANK : RankType.TEAM_RANK,
                dto.getLeaguePoints(),
                dto.getTier(),
                dto.getRank(),
                dto.getWins(),
                dto.getLosses(),
                dto.isVeteran(),
                dto.isInactive(),
                dto.isFreshBlood(),
                dto.isHotStreak()
        );
    }

    public void changeRiotUser(RiotUser finalRiotUser) {
        if (this.riotUser != null) {
            this.riotUser.getQueues().remove(this);
        }

        this.riotUser = finalRiotUser;
        this.riotUser.getQueues().add(this);
    }

    public void changeInfo(LeagueEntryDto dto) {
        this.leaguePoints = dto.getLeaguePoints();
        this.wins = dto.getWins();
        this.losses = dto.getLosses();
        this.veteran = dto.isVeteran();
        this.inactive = dto.isInactive();
        this.freshBlood = dto.isFreshBlood();
        this.hotStreak = dto.isHotStreak();
    }

    public boolean isSoloRank() {
        return this.rankType == RankType.SOLO_RANK;
    }

    public boolean isTeamRank() {
        return this.rankType == RankType.TEAM_RANK;
    }

    public void changeToInactive() {
        this.status = RankQueueStatus.INACTIVE;
    }

    public boolean isActive() {
        return this.status == RankQueueStatus.ACTIVE;
    }
}
