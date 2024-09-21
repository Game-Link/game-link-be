package com.gamelink.backend.infra.riot.model.entity.queuetype;

import com.gamelink.backend.infra.riot.model.dto.response.LeagueEntryDto;
import com.gamelink.backend.infra.riot.model.entity.Queue;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamRank extends Queue {

    @NotNull
    private String tier;

    @NotNull
    private String rankLevel;

    public TeamRank(RiotUser riotUser, String tier, String rankLevel, int leaguePoints, int wins, int losses, boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        super(riotUser, leaguePoints, wins, losses, veteran, inactive, freshBlood, hotStreak);
        this.tier = tier;
        this.rankLevel = rankLevel;
    }

    public void changeInfo(LeagueEntryDto dto) {
        super.changeInfo(dto);
        this.tier = dto.getTier();
        this.rankLevel = dto.getRank();
    }

    public static TeamRank convertFromLeagueEntryDto(RiotUser finalRiotUser, LeagueEntryDto dto) {
        return new TeamRank(
                finalRiotUser,
                dto.getTier(),
                dto.getRank(),
                dto.getLeaguePoints(),
                dto.getWins(),
                dto.getLosses(),
                dto.isVeteran(),
                dto.isInactive(),
                dto.isFreshBlood(),
                dto.isHotStreak()
        );
    }
}
