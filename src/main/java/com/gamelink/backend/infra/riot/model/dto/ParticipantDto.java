package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDto {
    @Schema(description = "Yellow crossed swords - '올인' 핑")
    private int allInPings;
    @Schema(description = "Green flag - '도움 요청' 핑")
    private int assistMePings;
    @Schema(description = "어시스트 수")
    private int assists;
    @Schema(description = "바론 처치 횟수")
    private int baronKills;
    @Schema(description = "현상금 레벨")
    private int bountyLevel;
    @Schema(description = "챔피언 경험치")
    private int champExperience;
    @Schema(description = "챔피언 레벨")
    private int champLevel;
    @Schema(description = "챔피언 ID")
    private int championId;
    @Schema(description = "챔피언 이름")
    private String championName;
    @Schema(description = "Blue generic ping (ALT+click) - '명령' 핑")
    private int commandPings;
    @Schema(description = "Kayn 변신 상태 (0: 없음, 1: Slayer, 2: Assassin)")
    private int championTransform;
    @Schema(description = "구매한 소모품 수")
    private int consumablesPurchased;
    @Schema(description = "챌린지 데이터")
    private ChallengesDto challenges;
    @Schema(description = "건물에 입힌 피해량")
    private int damageDealtToBuildings;
    @Schema(description = "오브젝트에 입힌 피해량")
    private int damageDealtToObjectives;
    @Schema(description = "포탑에 입힌 피해량")
    private int damageDealtToTurrets;
    @Schema(description = "자신이 방어한 피해량")
    private int damageSelfMitigated;
    @Schema(description = "사망 횟수")
    private int deaths;
    @Schema(description = "배치한 제어 와드 수")
    private int detectorWardsPlaced;
    @Schema(description = "더블킬 횟수")
    private int doubleKills;
    @Schema(description = "드래곤 처치 횟수")
    private int dragonKills;
    @Schema(description = "게임 진행 가능 여부")
    private boolean eligibleForProgression;
    @Schema(description = "Yellow questionmark - '적 실종' 핑")
    private int enemyMissingPings;
    @Schema(description = "Red eyeball - '적 와드' 핑")
    private int enemyVisionPings;
    @Schema(description = "첫 번째 킬 어시스트 여부")
    private boolean firstBloodAssist;
    @Schema(description = "첫 번째 킬 여부")
    private boolean firstBloodKill;
    @Schema(description = "첫 번째 포탑 어시스트 여부")
    private boolean firstTowerAssist;
    @Schema(description = "첫 번째 포탑 처치 여부")
    private boolean firstTowerKill;
    @Schema(description = "게임이 조기 서렌더로 종료되었는지 여부")
    private boolean gameEndedInEarlySurrender;
    @Schema(description = "게임이 서렌더로 종료되었는지 여부")
    private boolean gameEndedInSurrender;
    @Schema(description = "'대기' 핑")
    private int holdPings;
    @Schema(description = "Yellow circle with horizontal line - '철수' 핑")
    private int getBackPings;
    @Schema(description = "획득한 골드")
    private int goldEarned;
    @Schema(description = "사용한 골드")
    private int goldSpent;
    @Schema(description = "플레이어의 개별 포지션")
    private String individualPosition;
    @Schema(description = "억제기 처치 수")
    private int inhibitorKills;
    @Schema(description = "억제기 파괴 횟수")
    private int inhibitorTakedowns;
    @Schema(description = "잃은 억제기 수")
    private int inhibitorsLost;
    @Schema(description = "아이템 슬롯 0")
    private int item0;
    @Schema(description = "아이템 슬롯 1")
    private int item1;
    @Schema(description = "아이템 슬롯 2")
    private int item2;
    @Schema(description = "아이템 슬롯 3")
    private int item3;
    @Schema(description = "아이템 슬롯 4")
    private int item4;
    @Schema(description = "아이템 슬롯 5")
    private int item5;
    @Schema(description = "아이템 슬롯 6 (주로 장신구)")
    private int item6;
    @Schema(description = "구매한 아이템 수")
    private int itemsPurchased;
    @Schema(description = "킬 연속 기록")
    private int killingSprees;
    @Schema(description = "킬 횟수")
    private int kills;
    @Schema(description = "플레이어가 속한 라인")
    private String lane;
    @Schema(description = "최대 치명타 피해량")
    private int largestCriticalStrike;
    @Schema(description = "가장 긴 킬 연속 기록")
    private int largestKillingSpree;
    @Schema(description = "가장 큰 다중 킬")
    private int largestMultiKill;
    @Schema(description = "가장 오래 생존한 시간")
    private int longestTimeSpentLiving;
    @Schema(description = "입힌 마법 피해량")
    private int magicDamageDealt;
    @Schema(description = "챔피언에게 입힌 마법 피해량")
    private int magicDamageDealtToChampions;
    @Schema(description = "받은 마법 피해량")
    private int magicDamageTaken;
    @Schema(description = "미션 데이터")
    private MissionsDto missions;
    @Schema(description = "처치한 중립 미니언 수")
    private int neutralMinionsKilled;
    @Schema(description = "Green ward - '와드 필요' 핑")
    private int needVisionPings;
    @Schema(description = "넥서스 처치 횟수")
    private int nexusKills;
    @Schema(description = "넥서스 파괴 횟수")
    private int nexusTakedowns;
    @Schema(description = "잃은 넥서스 수")
    private int nexusLost;
    @Schema(description = "훔친 오브젝트 수")
    private int objectivesStolen;
    @Schema(description = "오브젝트를 훔친 어시스트 횟수")
    private int objectivesStolenAssists;
    @Schema(description = "Blue arrow pointing at ground - '가는 중' 핑")
    private int onMyWayPings;
    @Schema(description = "참가자 ID")
    private int participantId;
    @Schema(description = "플레이어 점수 0 (미사용 필드)")
    private int playerScore0;
    @Schema(description = "플레이어 점수 1 (미사용 필드)")
    private int playerScore1;
    @Schema(description = "플레이어 점수 2 (미사용 필드)")
    private int playerScore2;
    @Schema(description = "플레이어 점수 3 (미사용 필드)")
    private int playerScore3;
    @Schema(description = "플레이어 점수 4 (미사용 필드)")
    private int playerScore4;
    @Schema(description = "플레이어 점수 5 (미사용 필드)")
    private int playerScore5;
    @Schema(description = "플레이어 점수 6 (미사용 필드)")
    private int playerScore6;
    @Schema(description = "플레이어 점수 7 (미사용 필드)")
    private int playerScore7;
    @Schema(description = "플레이어 점수 8 (미사용 필드)")
    private int playerScore8;
    @Schema(description = "플레이어 점수 9 (미사용 필드)")
    private int playerScore9;
    @Schema(description = "펜타킬 횟수")
    private int pentaKills;
    @Schema(description = "플레이어의 특성 및 룬 데이터")
    private PerksDto perks;
    @Schema(description = "입힌 물리 피해량")
    private int physicalDamageDealt;
    @Schema(description = "챔피언에게 입힌 물리 피해량")
    private int physicalDamageDealtToChampions;
    @Schema(description = "받은 물리 피해량")
    private int physicalDamageTaken;
    @Schema(description = "플레이어의 최종 순위")
    private int placement;
    @Schema(description = "첫 번째 강화 아이템")
    private int playerAugment1;
    @Schema(description = "두 번째 강화 아이템")
    private int playerAugment2;
    @Schema(description = "세 번째 강화 아이템")
    private int playerAugment3;
    @Schema(description = "네 번째 강화 아이템")
    private int playerAugment4;
    @Schema(description = "플레이어의 서브 팀 ID")
    private int playerSubteamId;
    @Schema(description = "Green minion - '푸시' 핑")
    private int pushPings;
    @Schema(description = "플레이어의 프로필 아이콘 ID")
    private int profileIcon;
    @Schema(description = "플레이어의 PUUID (고유 식별자)")
    private String puuid;
    @Schema(description = "쿼드라킬 횟수")
    private int quadraKills;
    @Schema(description = "플레이어의 게임 내 이름")
    private String riotIdGameName;
    @Schema(description = "Riot ID 이름")
    private String riotIdName;
    @Schema(description = "Riot ID 태그라인")
    private String riotIdTagline;
    @Schema(description = "역할 (탑, 정글, 미드 등)")
    private String role;
    @Schema(description = "게임에서 구매한 시야 와드 수")
    private int sightWardsBoughtInGame;
    @Schema(description = "첫 번째 스펠 사용 횟수")
    private int spell1Casts;
    @Schema(description = "두 번째 스펠 사용 횟수")
    private int spell2Casts;
    @Schema(description = "세 번째 스펠 사용 횟수")
    private int spell3Casts;
    @Schema(description = "네 번째 스펠 사용 횟수")
    private int spell4Casts;
    @Schema(description = "서브 팀 내 순위")
    private int subteamPlacement;
    @Schema(description = "첫 번째 소환사 주문 사용 횟수")
    private int summoner1Casts;
    @Schema(description = "첫 번째 소환사 주문 ID")
    private int summoner1Id;
    @Schema(description = "두 번째 소환사 주문 사용 횟수")
    private int summoner2Casts;
    @Schema(description = "두 번째 소환사 주문 ID")
    private int summoner2Id;
    @Schema(description = "소환사 ID")
    private String summonerId;
    @Schema(description = "소환사 레벨")
    private int summonerLevel;
    @Schema(description = "소환사 이름")
    private String summonerName;
    @Schema(description = "팀이 조기 서렌더를 했는지 여부")
    private boolean teamEarlySurrendered;
    @Schema(description = "팀 ID")
    private int teamId;
    @Schema(description = "플레이어의 팀 포지션")
    private String teamPosition;
    @Schema(description = "상대 챔피언에게 가한 군중제어(CC) 시간")
    private int timeCCingOthers;
    @Schema(description = "게임에서 플레이한 총 시간 (초 단위)")
    private int timePlayed;
    @Schema(description = "아군 정글에서 처치한 중립 미니언 수")
    private int totalAllyJungleMinionsKilled;
    @Schema(description = "입힌 총 피해량")
    private int totalDamageDealt;
    @Schema(description = "챔피언에게 입힌 총 피해량")
    private int totalDamageDealtToChampions;
    @Schema(description = "팀원에게 제공한 방어막의 총 피해 흡수량")
    private int totalDamageShieldedOnTeammates;
    @Schema(description = "받은 총 피해량")
    private int totalDamageTaken;
    @Schema(description = "적 정글에서 처치한 중립 미니언 수")
    private int totalEnemyJungleMinionsKilled;
    @Schema(description = "적, 자신 또는 아군에게 제공한 총 치유량")
    private int totalHeal;
    @Schema(description = "팀원에게 제공한 총 치유량")
    private int totalHealsOnTeammates;
    @Schema(description = "처치한 총 미니언 수 (팀 미니언 및 중립 미니언 포함)")
    private int totalMinionsKilled;
    @Schema(description = "적에게 가한 총 군중제어(CC) 시간")
    private int totalTimeCCDealt;
    @Schema(description = "사망한 총 시간")
    private int totalTimeSpentDead;
    @Schema(description = "치유한 총 유닛 수")
    private int totalUnitsHealed;
    @Schema(description = "트리플킬 횟수")
    private int tripleKills;
    @Schema(description = "입힌 총 고정 피해량")
    private int trueDamageDealt;
    @Schema(description = "챔피언에게 입힌 총 고정 피해량")
    private int trueDamageDealtToChampions;
    @Schema(description = "받은 총 고정 피해량")
    private int trueDamageTaken;
    @Schema(description = "처치한 포탑 수")
    private int turretKills;
    @Schema(description = "포탑 파괴 횟수")
    private int turretTakedowns;
    @Schema(description = "잃은 포탑 수")
    private int turretsLost;
    @Schema(description = "획득한 Unreal Kills (게임 내 특별한 상황에서 발생하는 킬)")
    private int unrealKills;
    @Schema(description = "게임에서 얻은 총 시야 점수")
    private int visionScore;
    @Schema(description = "시야 제거 핑 (와드를 제거한 핑)")
    private int visionClearedPings;
    @Schema(description = "게임 중 구매한 제어 와드 수")
    private int visionWardsBoughtInGame;
    @Schema(description = "파괴한 와드 수")
    private int wardsKilled;
    @Schema(description = "배치한 와드 수")
    private int wardsPlaced;
    @Schema(description = "플레이어가 게임에서 승리했는지 여부")
    private boolean win;
}
