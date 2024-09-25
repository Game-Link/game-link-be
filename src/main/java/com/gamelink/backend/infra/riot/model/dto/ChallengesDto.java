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
public class ChallengesDto {
    @Schema(description = "12 어시스트 연속 횟수")
    private int _12AssistStreakCount;
    @Schema(description = "바론 버프 동안 골드 차이 우위")
    private int baronBuffGoldAdvantageOverThreshold;
    @Schema(description = "강가 또는 적 진영에서 제어 와드 시간 비율")
    private float controlWardTimeCoverageInRiverOrEnemyHalf;
    @Schema(description = "가장 빠른 바론 처치 시간")
    private int earliestBaron;
    @Schema(description = "가장 빠른 드래곤 처치 시간")
    private int earliestDragonTakedown;
    @Schema(description = "가장 빠른 장로 드래곤 처치 시간")
    private int earliestElderDragon;
    @Schema(description = "초기 라인전에서 골드 우위")
    private int earlyLaningPhaseGoldExpAdvantage;
    @Schema(description = "서포터 퀘스트 완료 시간 기록")
    private int fasterSupportQuestCompletion;
    @Schema(description = "가장 빠른 레전더리 (전설적인 킬 기록)")
    private int fastestLegendary;
    @Schema(description = "AFK 팀원이 있었는지 여부")
    private int hadAfkTeammate;
    @Schema(description = "가장 많은 챔피언 피해량 기록")
    private int highestChampionDamage;
    @Schema(description = "가장 높은 군중제어(CC) 기록")
    private int highestCrowdControlScore;
    @Schema(description = "가장 많은 와드 제거 횟수")
    private int highestWardKills;
    @Schema(description = "초기 정글에서 정글러로 처치한 횟수")
    private int junglerKillsEarlyJungle;
    @Schema(description = "정글러로서 다른 라인에서 얻은 킬 횟수")
    private int killsOnLanersEarlyJungleAsJungler;
    @Schema(description = "라인전에서 골드 우위를 기록한 시간")
    private int laningPhaseGoldExpAdvantage;
    @Schema(description = "레전더리(전설적인 킬 기록) 횟수")
    private int legendaryCount;
    @Schema(description = "라인 상대에 대한 CS 우위")
    private float maxCsAdvantageOnLaneOpponent;
    @Schema(description = "라인 상대보다 가장 높은 레벨 우위")
    private int maxLevelLeadLaneOpponent;
    @Schema(description = "스위퍼 하나로 가장 많은 와드 제거 기록")
    private int mostWardsDestroyedOneSweeper;
    @Schema(description = "사용한 신화 아이템")
    private int mythicItemUsed;
    @Schema(description = "챔피언 선택 단계에서 플레이한 포지션")
    private int playedChampSelectPosition;
    @Schema(description = "후반전 솔로 포탑 처치 횟수")
    private int soloTurretsLategame;
    @Schema(description = "25분 내에 얻은 처치 횟수")
    private int takedownsFirst25Minutes;
    @Schema(description = "순간 이동으로 처치한 횟수")
    private int teleportTakedowns;
    @Schema(description = "세 번째 억제기 파괴 시간")
    private int thirdInhibitorDestroyedTime;
    @Schema(description = "하나의 스위퍼로 3개의 와드를 제거한 횟수")
    private int threeWardsOneSweeperCount;
    @Schema(description = "라인 상대에 대한 시야 점수 우위")
    private float visionScoreAdvantageLaneOpponent;
    @Schema(description = "지옥의 규모 (Infernal Scale) 픽업 횟수")
    private int infernalScalePickup;
    @Schema(description = "주먹 인사 참여 횟수")
    private int fistBumpParticipation;
    @Schema(description = "공허 몬스터 처치 횟수")
    private int voidMonsterKill;
    @Schema(description = "사용한 스킬 횟수")
    private int abilityUses;
    @Schema(description = "15분 이전에 얻은 에이스 횟수")
    private int acesBefore15Minutes;
    @Schema(description = "아군 정글에서 얻은 중립 몬스터 처치 횟수")
    private float alliedJungleMonsterKills;
    @Schema(description = "바론 처치 횟수")
    private int baronTakedowns;
    @Schema(description = "적 진영 반대편 폭발식물 사용 횟수")
    private int blastConeOppositeOpponentCount;
    @Schema(description = "얻은 현상금 골드")
    private int bountyGold;
    @Schema(description = "훔친 버프 수")
    private int buffsStolen;
    @Schema(description = "서포터 퀘스트를 제 시간에 완료한 횟수")
    private int completeSupportQuestInTime;
    @Schema(description = "제어 와드 배치 횟수")
    private int controlWardsPlaced;
    @Schema(description = "분당 피해량")
    private float damagePerMinute;
    @Schema(description = "팀이 받은 총 피해량 중 자신이 받은 비율")
    private float damageTakenOnTeamPercentage;
    @Schema(description = "전령과 춤춘 횟수")
    private int dancedWithRiftHerald;
    @Schema(description = "적 챔피언에게 사망한 횟수")
    private int deathsByEnemyChamps;
    @Schema(description = "작은 틈에서 적 스킬을 피한 횟수")
    private int dodgeSkillShotsSmallWindow;
    @Schema(description = "더블 에이스 횟수")
    private int doubleAces;
    @Schema(description = "드래곤 처치 횟수")
    private int dragonTakedowns;
    @Schema(description = "사용한 전설 아이템 목록")
    private List<Integer> legendaryItemUsed;
    @Schema(description = "팀원에게 제공한 방어막과 치유의 총량")
    private float effectiveHealAndShielding;
    @Schema(description = "상대방의 영혼을 얻은 상태에서 장로 드래곤을 처치한 횟수")
    private int elderDragonKillsWithOpposingSoul;
    @Schema(description = "장로 드래곤 멀티킬 횟수")
    private int elderDragonMultikills;
    @Schema(description = "적 챔피언을 군중제어(CC)로 묶은 횟수")
    private int enemyChampionImmobilizations;
    @Schema(description = "적 정글에서 얻은 중립 몬스터 처치 횟수")
    private float enemyJungleMonsterKills;
    @Schema(description = "적 정글 근처에서 처치한 에픽 몬스터 수")
    private int epicMonsterKillsNearEnemyJungler;
    @Schema(description = "에픽 몬스터가 생성된 후 30초 이내에 처치한 횟수")
    private int epicMonsterKillsWithin30SecondsOfSpawn;
    @Schema(description = "훔친 에픽 몬스터 횟수")
    private int epicMonsterSteals;
    @Schema(description = "강타 없이 에픽 몬스터를 훔친 횟수")
    private int epicMonsterStolenWithoutSmite;
    @Schema(description = "첫 번째 포탑 처치 여부")
    private int firstTurretKilled;
    @Schema(description = "첫 번째 포탑 처치 시간")
    private float firstTurretKilledTime;
    @Schema(description = "완벽한 에이스 횟수")
    private int flawlessAces;
    @Schema(description = "전체 팀 처치 횟수")
    private int fullTeamTakedown;
    @Schema(description = "게임 길이 (분)")
    private float gameLength;
    @Schema(description = "초기 정글에서 다른 라인에서 처치한 횟수")
    private int getTakedownsInAllLanesEarlyJungleAsLaner;
    @Schema(description = "분당 골드 획득량")
    private float goldPerMinute;
    @Schema(description = "넥서스가 열린 상태에서 게임을 마친 횟수")
    private int hadOpenNexus;
    @Schema(description = "아군과 협력하여 적을 제압한 횟수")
    private int immobilizeAndKillWithAlly;
    @Schema(description = "초기 버프 획득 수")
    private int initialBuffCount;
    @Schema(description = "처치한 스카틀 크랩 수")
    private int initialCrabCount;
    @Schema(description = "10분 이전에 얻은 정글 CS 수")
    private float jungleCsBefore10Minutes;
    @Schema(description = "데미지를 입은 에픽 몬스터 근처에서 정글러로 처치한 횟수")
    private int junglerTakedownsNearDamagedEpicMonster;
    @Schema(description = "KDA (킬/데스/어시스트 비율)")
    private float kda;
    @Schema(description = "아군이 숨은 상태에서 적을 처치한 횟수")
    private int killAfterHiddenWithAlly;
    @Schema(description = "적 팀에게서 팀 전체의 데미지를 받았으나 생존한 횟수")
    private int killedChampTookFullTeamDamageSurvived;
    @Schema(description = "킬 연속 기록 횟수")
    private int killingSprees;
    @Schema(description = "킬 관여율")
    private float killParticipation;
    @Schema(description = "적 포탑 근처에서 처치한 횟수")
    private int killsNearEnemyTurret;
    @Schema(description = "초기 정글에서 다른 라인에서 킬을 얻은 횟수")
    private int killsOnOtherLanesEarlyJungleAsLaner;
    @Schema(description = "ARAM 팩에 의해 최근에 치유받은 적을 처치한 횟수")
    private int killsOnRecentlyHealedByAramPack;
    @Schema(description = "자신의 포탑 아래에서 처치한 횟수")
    private int killsUnderOwnTurret;
    @Schema(description = "에픽 몬스터의 도움을 받아 처치한 횟수")
    private int killsWithHelpFromEpicMonster;
    @Schema(description = "적을 팀으로 끌어들여 처치한 횟수")
    private int knockEnemyIntoTeamAndKill;
    @Schema(description = "포탑 방패가 떨어지기 전에 파괴한 포탑 수")
    private int kTurretsDestroyedBeforePlatesFall;
    @Schema(description = "초기 게임에서 명중시킨 스킬샷 수")
    private int landSkillShotsEarlyGame;
    @Schema(description = "10분 동안 처치한 라인 미니언 수")
    private int laneMinionsFirst10Minutes;
    @Schema(description = "잃은 억제기 수")
    private int lostAnInhibitor;
    @Schema(description = "가장 많은 킬 차이 기록")
    private int maxKillDeficit;
    @Schema(description = "메자이 풀 스택을 달성한 시간 기록")
    private int mejaisFullStackInTime;
    @Schema(description = "상대 정글보다 더 많은 적 정글 몬스터 처치 비율")
    private float moreEnemyJungleThanOpponent;
    @Schema(description = "한 개의 스킬로 멀티 킬 횟수")
    private int multiKillOneSpell;
    @Schema(description = "멀티킬 횟수")
    private int multikills;
    @Schema(description = "플래시 사용 후 멀티킬 횟수")
    private int multikillsAfterAggressiveFlash;
    @Schema(description = "전령으로 두 개 이상의 포탑을 파괴한 횟수")
    private int multiTurretRiftHeraldCount;
    @Schema(description = "10분 전에 외부 포탑 처형 횟수")
    private int outerTurretExecutesBefore10Minutes;
    @Schema(description = "수적 열세에서 적을 처치한 횟수")
    private int outnumberedKills;
    @Schema(description = "넥서스 근처에서 수적 열세로 적을 처치한 횟수")
    private int outnumberedNexusKill;
    @Schema(description = "퍼펙트 드래곤 소울을 획득한 횟수")
    private int perfectDragonSoulsTaken;
    @Schema(description = "퍼펙트 게임 횟수")
    private int perfectGame;
    @Schema(description = "아군과 협력하여 적을 처치한 횟수")
    private int pickKillWithAlly;
    @Schema(description = "Poro 폭발 횟수")
    private int poroExplosions;
    @Schema(description = "빠른 클렌즈 사용 횟수")
    private int quickCleanse;
    @Schema(description = "빠른 첫 번째 포탑 파괴 횟수")
    private int quickFirstTurret;
    @Schema(description = "빠른 솔로 킬 횟수")
    private int quickSoloKills;
    @Schema(description = "전령 처치 횟수")
    private int riftHeraldTakedowns;
    @Schema(description = "팀원을 구해낸 횟수")
    private int saveAllyFromDeath;
    @Schema(description = "바위게 처치 횟수")
    private int scuttleCrabKills;
    @Schema(description = "첫 번째 처치 이후 가장 짧은 시간에 에이스를 기록한 시간")
    private float shortestTimeToAceFromFirstTakedown;
    @Schema(description = "피한 스킬샷 수")
    private int skillshotsDodged;
    @Schema(description = "명중시킨 스킬샷 수")
    private int skillshotsHit;
    @Schema(description = "명중시킨 눈덩이 횟수")
    private int snowballsHit;
    @Schema(description = "솔로 바론 처치 횟수")
    private int soloBaronKills;
    @Schema(description = "SWARM 모드에서 아트록스를 처치한 횟수")
    private int SWARM_DefeatAatrox;
    @Schema(description = "SWARM 모드에서 브라이어를 처치한 횟수")
    private int SWARM_DefeatBriar;
    @Schema(description = "SWARM 모드에서 미니 보스들을 처치한 횟수")
    private int SWARM_DefeatMiniBosses;
    @Schema(description = "SWARM 모드에서 무기를 진화시킨 횟수")
    private int SWARM_EvolveWeapon;
    @Schema(description = "SWARM 모드에서 3개의 패시브를 보유한 횟수")
    private int SWARM_Have3Passives;
    @Schema(description = "SWARM 모드에서 적을 처치한 횟수")
    private int SWARM_KillEnemy;
    @Schema(description = "SWARM 모드에서 골드를 획득한 수")
    private float SWARM_PickupGold;
    @Schema(description = "SWARM 모드에서 레벨 50을 달성한 횟수")
    private int SWARM_ReachLevel50;
    @Schema(description = "SWARM 모드에서 15분을 생존한 횟수")
    private int SWARM_Survive15Min;
    @Schema(description = "SWARM 모드에서 5개의 무기를 진화시킨 채로 승리한 횟수")
    private int SWARM_WinWith5EvolvedWeapons;
    @Schema(description = "솔로 킬 횟수")
    private int soloKills;
    @Schema(description = "배치한 스텔스 와드 수")
    private int stealthWardsPlaced;
    @Schema(description = "한 자릿수 HP에서 생존한 횟수")
    private int survivedSingleDigitHpCount;
    @Schema(description = "싸움에서 세 번의 군중제어(CC)를 피한 횟수")
    private int survivedThreeImmobilizesInFight;
    @Schema(description = "첫 번째 포탑을 파괴한 횟수")
    private int takedownOnFirstTurret;
    @Schema(description = "처치 횟수")
    private int takedowns;
    @Schema(description = "레벨 우위를 점한 후 처치한 횟수")
    private int takedownsAfterGainingLevelAdvantage;
    @Schema(description = "정글 미니언 생성 전 처치 횟수")
    private int takedownsBeforeJungleMinionSpawn;
    @Schema(description = "초반 몇 분 동안의 처치 횟수")
    private int takedownsFirstXMinutes;
    @Schema(description = "탑,바텀 U자 지형에서의 처치 횟수")
    private int takedownsInAlcove;
    @Schema(description = "적의 우물에서 처치한 횟수")
    private int takedownsInEnemyFountain;
    @Schema(description = "팀의 바론 처치 횟수")
    private int teamBaronKills;
    @Schema(description = "팀의 총 피해량 중 플레이어가 기여한 비율")
    private float teamDamagePercentage;
    @Schema(description = "팀의 장로 드래곤 처치 횟수")
    private int teamElderDragonKills;
    @Schema(description = "팀의 전령 처치 횟수")
    private int teamRiftHeraldKills;
    @Schema(description = "큰 피해를 받은 후 생존한 횟수")
    private int tookLargeDamageSurvived;
    @Schema(description = "포탑 방패를 파괴한 횟수")
    private int turretPlatesTaken;
    @Schema(description = "전령을 사용하여 파괴한 포탑 수")
    private int turretsTakenWithRiftHerald;
    @Schema(description = "3초 내에 20개의 미니언을 처치한 횟수")
    private int twentyMinionsIn3SecondsCount;
    @Schema(description = "스위퍼 하나로 2개의 와드를 제거한 횟수")
    private int twoWardsOneSweeperCount;
    @Schema(description = "적에게 발견되지 않고 귀환한 횟수")
    private int unseenRecalls;
    @Schema(description = "분당 시야 점수")
    private float visionScorePerMinute;
    @Schema(description = "보호한 와드 수")
    private int wardsGuarded;
    @Schema(description = "제거한 와드 수")
    private int wardTakedowns;
    @Schema(description = "게임 시작 20분 전에 제거한 와드 수")
    private int wardTakedownsBefore20M;
}