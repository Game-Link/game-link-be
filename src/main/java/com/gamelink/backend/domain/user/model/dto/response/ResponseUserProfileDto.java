package com.gamelink.backend.domain.user.model.dto.response;

import com.gamelink.backend.domain.user.model.dto.ProfileImageDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerSoloRankDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerTeamRankDto;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseUserProfileDto {

    @Schema(description = "닉네임", example = "강렬한 피오라")
    private final String nickname;

    @Schema(description = "소환사 아이콘 이미지 URL")
    private final String summonerIconUrl;

    @Schema(description = "프로필 이미지 URL", example = "https://opgg-static.akamaized.net/images/profile_icons/profileIcon1.jpg")
    private final List<ProfileImageDto> profileImageUrl;

    @Schema(description = "솔로 랭크 정보")
    private final ResponseSummonerSoloRankDto soloRank;

    @Schema(description = "자유 랭크 정보")
    private final ResponseSummonerTeamRankDto teamRank;

    @Schema(description = "매치 데이터 요약 정보")
    private final ResponseSummarizeMatchDataDto summarizeMatchData;

    public ResponseUserProfileDto(RiotUser riotUser, String summonerIconUrl, AWSObjectStorageService s3service, ResponseSummonerSoloRankDto soloRank, ResponseSummonerTeamRankDto teamRank, ResponseSummarizeMatchDataDto matchDataDto) {
        this.nickname = riotUser.getUser().getNickname();
        this.summonerIconUrl = summonerIconUrl;
        this.profileImageUrl = ProfileImageDto.listOf(s3service, riotUser.getUser().getProfileImages());
        this.soloRank = soloRank;
        this.teamRank = teamRank;
        this.summarizeMatchData = matchDataDto;
    }
}
