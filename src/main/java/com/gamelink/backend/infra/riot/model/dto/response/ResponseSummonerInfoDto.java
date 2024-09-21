package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.global.util.TimeUtil;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Schema(description = "소환사 정보")
public class ResponseSummonerInfoDto {

    @Schema(description = "유저 ID")
    private final UUID userId;

    @Schema(description = "게임 아이디")
    private final String puuid;

    @Schema(description = "소환사 Id")
    private final String summonerId;

    @Schema(description = "소환사 이름")
    private final String summonerName;

    @Schema(description = "소환사 태그")
    private final String summonerTag;

    @Schema(description = "소환사 아이콘 아이디")
    private final int profileIconId;

    @Schema(description = "소환사의 마지막 수정날짜")
    private final  LocalDateTime revisionDate;

    @Schema(description = "소환사 레벨")
    private final Long summonerLevel;

    @Schema(description = "소환사 솔로 랭크 정보")
    private final ResponseSummonerSoloRankDto soloRank;

    @Schema(description = "소환사 팀 랭크 정보")
    private final ResponseSummonerTeamRankDto teamRank;

    public ResponseSummonerInfoDto(RiotUser riotUser, ResponseSummonerSoloRankDto soloRank, ResponseSummonerTeamRankDto teamRank) {
        this.userId = riotUser.getUser().getSubId();
        this.puuid = riotUser.getPuuid();
        this.summonerId = riotUser.getSummonerId();
        this.summonerName = riotUser.getSummonerName();
        this.summonerTag = riotUser.getSummonerTag();
        this.profileIconId = riotUser.getProfileIconId();
        this.revisionDate = riotUser.getRevisionDate();
        this.summonerLevel = riotUser.getSummonerLevel();
        this.soloRank = soloRank;
        this.teamRank = teamRank;
    }
}
