package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.domain.user.model.dto.ProfileImageDto;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.infra.riot.model.dto.CacheChampionDto;
import com.gamelink.backend.infra.riot.model.dto.CacheKdaDto;
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

    @Schema(description = "전체 게임 승리 횟수")
    private final int wins;

    @Schema(description = "전체 게임 패배 횟수")
    private final int losses;

    @Schema(description = "전체 게임 승률")
    private final double winRate;

    @Schema(description = "전체 게임 평균 K/DA")
    private final double kda;

    @Schema(description = "전체 게임 평균 킬")
    private final double avgKills;

    @Schema(description = "전체 게임 평균 데스")
    private final double avgDeaths;

    @Schema(description = "전체 게임 평균 어시스트")
    private final double avgAssists;

    @Schema(description = "전체 게임 평균 CS")
    private final double avgCs;

    @Schema(description = "전체 게임 베스트 3 챔피언 정보")
    private final List<ResponseChampionInfoDto> best3champions;

    @Schema(description = "소환사 솔로 랭크 정보")
    private final ResponseSummonerSoloRankDto soloRank;

    @Schema(description = "소환사 팀 랭크 정보")
    private final ResponseSummonerTeamRankDto teamRank;

    public ResponseSummonerInfoDto(User user, CacheKdaDto totalKdaDto, List<CacheChampionDto> totalChampion, AWSObjectStorageService s3service, RiotUser riotUser, ResponseSummonerSoloRankDto soloRank, ResponseSummonerTeamRankDto teamRank, String summonerIconUrl) {
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
        this.wins = totalKdaDto.getWins();
        this.losses = totalKdaDto.getLosses();
        this.winRate = (totalKdaDto.getWins() + totalKdaDto.getLosses()) == 0 ? 0 : (double) totalKdaDto.getWins() / (totalKdaDto.getWins() + totalKdaDto.getLosses());
        this.kda = (totalKdaDto.getKills() + totalKdaDto.getAssists() != 0) ? (double) (totalKdaDto.getKills() + totalKdaDto.getAssists()) / totalKdaDto.getDeaths() : 0;
        this.avgKills = (totalKdaDto.getKills() != 0) ? (double) totalKdaDto.getKills() / totalKdaDto.getMatchCount() : 0;
        this.avgDeaths = (totalKdaDto.getDeaths() != 0) ? (double) totalKdaDto.getDeaths() / totalKdaDto.getMatchCount() : 0;
        this.avgAssists = (totalKdaDto.getAssists() != 0 ) ? (double) totalKdaDto.getAssists() / totalKdaDto.getMatchCount() : 0;
        this.avgCs = (totalKdaDto.getCs() != 0) ? (double) totalKdaDto.getCs() / totalKdaDto.getMatchCount() : 0;
        this.best3champions = ResponseChampionInfoDto.list3BestOf(totalChampion);
        this.soloRank = soloRank;
        this.teamRank = teamRank;
    }
}
