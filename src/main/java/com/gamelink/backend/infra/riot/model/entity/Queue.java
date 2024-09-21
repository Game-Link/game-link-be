package com.gamelink.backend.infra.riot.model.entity;

import com.gamelink.backend.global.base.BaseEntity;
import com.gamelink.backend.infra.riot.model.dto.response.LeagueEntryDto;
import com.gamelink.backend.infra.riot.model.entity.queuetype.SoloRank;
import com.gamelink.backend.infra.riot.model.entity.queuetype.TeamRank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Table(name = "queue_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Queue extends BaseEntity {

    @JoinColumn(name = "riot_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RiotUser riotUser;

    @NotNull
    private int leaguePoints;

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

    public Queue(RiotUser riotUser, int leaguePoints, int wins, int losses, boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        this.riotUser = riotUser;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
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
        return this instanceof SoloRank;
    }

    public boolean isTeamRank() {
        return this instanceof TeamRank;
    }
}
