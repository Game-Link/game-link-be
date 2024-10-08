package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.domain.user.model.dto.ProfileImageDto;
import com.gamelink.backend.domain.user.model.entity.ProfileImage;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Schema(description = "소환사 정보")
public class ResponseSummonerInfoDto {

    @Schema(description = "유저 ID")
    private final UUID userId;

    @Schema(description = "닉네임")
    private final String nickname;

    @Schema(description = "프로필 이미지 URL")
    private final List<ProfileImageDto> profileImageUrl;

    @Schema(description = "이메일")
    private final String email;

    @Schema(description = "게임 아이디")
    private final String puuid;

    @Schema(description = "소환사 Id")
    private final String summonerId;

    @Schema(description = "소환사 이름")
    private final String summonerName;

    @Schema(description = "소환사 태그")
    private final String summonerTag;

    @Schema(description = "소환사 아이콘 이미지 Url")
    private final String summonerIconUrl;

    @Schema(description = "소환사의 마지막 수정날짜")
    private final  LocalDateTime revisionDate;

    @Schema(description = "소환사 레벨")
    private final Long summonerLevel;

    @Schema(description = "소환사 솔로 랭크 정보")
    private final ResponseSummonerSoloRankDto soloRank;

    @Schema(description = "소환사 팀 랭크 정보")
    private final ResponseSummonerTeamRankDto teamRank;

    public ResponseSummonerInfoDto(User user, AWSObjectStorageService s3service, RiotUser riotUser, ResponseSummonerSoloRankDto soloRank, ResponseSummonerTeamRankDto teamRank, String summonerIconUrl) {
        this.userId = user.getSubId();
        this.nickname = user.getNickname();
        this.profileImageUrl = ProfileImageDto.listOf(s3service, user.getProfileImages());
        this.email = user.getEmail();
        this.puuid = riotUser.getPuuid();
        this.summonerId = riotUser.getSummonerId();
        this.summonerName = riotUser.getSummonerName();
        this.summonerTag = riotUser.getSummonerTag();
        this.summonerIconUrl = summonerIconUrl;
        this.revisionDate = riotUser.getRevisionDate();
        this.summonerLevel = riotUser.getSummonerLevel();
        this.soloRank = soloRank;
        this.teamRank = teamRank;
    }
}
