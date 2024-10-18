package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoDto {

    @Schema(description = "게임 마무리 상태", example = "gameComplete")
    private String endOfGameResult;
    @Schema(description = "게임 생성 시간 (Unix Timestamp)", example = "1620000000000")
    private Long gameCreation;
    @Schema(description = "게임 진행 시간", example = "1800")
    private Long gameDuration;
    @Schema(description = "게임 종료 시간 (Unix Timestamp)", example = "1620000000000")
    private Long gameEndTimestamp;
    @Schema(description = "게임 아이디", example = "1234567890")
    private long gameId;
    @Schema(description = "게임 모드", example = "CLASSIC: 솔로랭크 / 팀랭크, ARAM: 칼바람, CHERRY: 아레나")
    private String gameMode;
    @Schema(description = "게임 이름", example = "teambuilder-match-7288220655")
    private String gameName;
    @Schema(description = "게임 시작 시간 (Unix Timestamp)", example = "1620000000000")
    private long gameStartTimestamp;
    @Schema(description = "게임 타입", example = "MATCHED_GAME")
    private String gameType;
    @Schema(description = "게임 버전", example = "14.18.618.2357")
    private String gameVersion;
    @Schema(description = "맵 아이디", example = "12")
    private int mapId;
    @Schema(description = "참가자 정보")
    private List<ParticipantDto> participants;
    @Schema(description = "플랫폼 아이디", example = "KR")
    private String platformId;
    @Schema(description = "게임 큐 아이디")
    private int queueId;
    @Schema(description = "팀 정보")
    private List<TeamDto> teams;
    @Schema(description = "게임 토너먼트 코드", example = "NA1-1234567890")
    private String tournamentCode;
}
