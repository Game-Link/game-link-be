package com.gamelink.backend.infra.riot.model.dto.response;

import com.gamelink.backend.infra.riot.model.dto.MatchDto;
import com.gamelink.backend.infra.riot.model.dto.ParticipantDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

// TODO : 채팅방에서 프로필 클릭 시 보여줄 데이터
@Getter
@Setter
public class PersistMatchDataDto {

    @Schema(description = "매치 Id")
    private final String matchId;

    // TODO : 챔피언 URL로 변경
    @Schema(description = "매치에서 플레이 한 챔피언 이름", example = "아트록스")
    private final String championName;

    @Schema(description = "더블킬 횟수", example = "1")
    private final int doubleKills;

    @Schema(description = "첫 번째 킬 여부")
    private final boolean firstBloodKill;

    @Schema(description = "플러이어의 팀 포지션", example = "TOP")
    private final String teamPosition;

    @Schema(description = "펜타킬 횟수", example = "0")
    private final int pentaKills;

    @Schema(description = "어시스트 수", example = "5")
    private final int assists;

    @Schema(description = "사망 횟수", example = "3")
    private final int deaths;

    @Schema(description = "킬 수", example = "5")
    private final int kills;

    @Schema(description = "KDA", example = "3.3")
    private final float kda;

    @Schema(description = "첫 번째 킬 어시스트 여부")
    private final boolean firstBloodAssist;

    @Schema(description = "첫 번째 타워 어시스트 여부")
    private final boolean firstTowerAssist;

    @Schema(description = "첫 번째 타워 킬 여부")
    private final boolean firstTowerKill;

    @Schema(description = "분당 골드 획득량", example = "300")
    private final float goldPerMinute;

    @Schema(description = "게임이 조기 항복로 종료되었는지 여부")
    private final boolean gameEndedInEarlySurrender;

    @Schema(description = "게임이 항복으로 종료되었는지 여부")
    private final boolean gameEndedInSurrender;

    @Schema(description = "게임에서 플레이한 총 시간 (초 단위)")
    private final int timePlayed;

    @Schema(description = "처치한 총 미니언 수 (팀 미니언 및 중립 미니언 포함)")
    private final int totalMinionsKilled;

    @Schema(description = "플레이어가 게임에서 승리했는지 여부")
    private final boolean win;

    @Schema(description = "솔로킬 횟수")
    private final int soloKills;

    @Schema(description = "레전더리(전설적인 킬 기록) 횟수")
    private final int legendaryCount;

    @Schema(description = "분당 피해량")
    private final float damagePerMinute;

    @Schema(description = "드래곤 처치 횟수")
    private final int dragonTakedowns;

    @Schema(description = "훔친 에픽 몬스터 횟수")
    private final int epicMonsterSteals;

    @Schema(description = "바론 처치 횟수")
    private final int baronTakedowns;

    @Schema(description = "공허 몬스터 처치 횟수")
    private final int voidMonsterKill;

    @Schema(description = "퍼펙트 드래곤 소울 획득 횟수")
    private final int perfectDragonSoulsTaken;

    @Schema(description = "장로 드래곤 멀티킬 횟수")
    private final int elderDragonMultikills;

    @Schema(description = "킬 관여율")
    private final float killParticipation;

    public PersistMatchDataDto(MatchDto matchDto, ParticipantDto participantDto) {
        this.matchId = matchDto.getMetadata().getMatchId();
        this.championName = participantDto.getChampionName();
        this.doubleKills = participantDto.getDoubleKills();
        this.firstBloodKill = participantDto.isFirstBloodKill();
        this.teamPosition = participantDto.getTeamPosition();
        this.pentaKills = participantDto.getPentaKills();
        this.assists = participantDto.getAssists();
        this.deaths = participantDto.getDeaths();
        this.kills = participantDto.getKills();
        this.kda = participantDto.getChallenges().getKda();
        this.firstBloodAssist = participantDto.isFirstBloodAssist();
        this.firstTowerAssist = participantDto.isFirstTowerAssist();
        this.firstTowerKill = participantDto.isFirstTowerKill();
        this.goldPerMinute = participantDto.getChallenges().getGoldPerMinute();
        this.gameEndedInEarlySurrender = participantDto.isGameEndedInEarlySurrender();
        this.gameEndedInSurrender = participantDto.isGameEndedInSurrender();
        this.timePlayed = participantDto.getTimePlayed();
        this.totalMinionsKilled = participantDto.getTotalMinionsKilled();
        this.win = participantDto.isWin();
        this.soloKills = participantDto.getChallenges().getSoloKills();
        this.legendaryCount = participantDto.getChallenges().getLegendaryCount();
        this.damagePerMinute = participantDto.getChallenges().getDamagePerMinute();
        this.dragonTakedowns = participantDto.getChallenges().getDragonTakedowns();
        this.epicMonsterSteals = participantDto.getChallenges().getEpicMonsterSteals();
        this.baronTakedowns = participantDto.getChallenges().getBaronTakedowns();
        this.voidMonsterKill = participantDto.getChallenges().getVoidMonsterKill();
        this.perfectDragonSoulsTaken = participantDto.getChallenges().getPerfectDragonSoulsTaken();
        this.elderDragonMultikills = participantDto.getChallenges().getElderDragonMultikills();
        this.killParticipation = participantDto.getChallenges().getKillParticipation();
    }

    public static CacheMatchDataDto convertToCacheMatchDataDto(PersistMatchDataDto match) {
        return new CacheMatchDataDto(
                match.getMatchId(),
                match.getChampionName(),
                match.getDoubleKills(),
                match.isFirstBloodKill(),
                match.getTeamPosition(),
                match.getPentaKills(),
                match.getAssists(),
                match.getDeaths(),
                match.getKills(),
                match.getKda(),
                match.isFirstBloodAssist(),
                match.isFirstTowerAssist(),
                match.isFirstTowerKill(),
                match.getGoldPerMinute(),
                match.isGameEndedInEarlySurrender(),
                match.isGameEndedInSurrender(),
                match.getTimePlayed(),
                match.getTotalMinionsKilled(),
                match.isWin(),
                match.getSoloKills(),
                match.getLegendaryCount(),
                match.getDamagePerMinute(),
                match.getDragonTakedowns(),
                match.getEpicMonsterSteals(),
                match.getBaronTakedowns(),
                match.getVoidMonsterKill(),
                match.getPerfectDragonSoulsTaken(),
                match.getElderDragonMultikills(),
                match.getKillParticipation()
        );
    }
}
