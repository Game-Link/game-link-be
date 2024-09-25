package com.gamelink.backend.infra.riot.model.entity;

import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.base.BaseEntity;
import com.gamelink.backend.infra.riot.model.dto.AccountDto;
import com.gamelink.backend.infra.riot.model.dto.SummonerDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "riot_user")
@Entity
public class RiotUser extends BaseEntity {

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    private String puuid;

    @NotNull
    private String summonerId;

    @NotNull
    private String summonerName;

    @NotNull
    private String summonerTag;

    @NotNull
    private int profileIconId;

    private LocalDateTime revisionDate;

    @NotNull
    private Long summonerLevel;

    @OneToMany(mappedBy = "riotUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RankQueue> queues = new ArrayList<>();

    public RiotUser(User user, String puuid, String summonerId, String summonerName, String summonerTag, int profileIconId, LocalDateTime revisionDate, Long summonerLevel) {
        this.user = user;
        this.puuid = puuid;
        this.summonerId = summonerId;
        this.summonerName = summonerName;
        this.summonerTag = summonerTag;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }

    /**
     * RiotUser 정보 변경
     */
    public void changeInfo(AccountDto accountDto, SummonerDto summonerDto) {
        this.puuid = accountDto.getPuuid();
        this.summonerId = summonerDto.getId();
        this.summonerName = accountDto.getGameName();
        this.summonerTag = accountDto.getTagLine();
        this.profileIconId = summonerDto.getProfileIconId();
        this.revisionDate = LocalDateTime.now();
        this.summonerLevel = summonerDto.getSummonerLevel();
    }

    public static RiotUser convertFromAccountAndSummonerDto(User user, AccountDto accountDto, SummonerDto summonerDto) {
        return new RiotUser(
                user,
                accountDto.getPuuid(),
                summonerDto.getId(),
                accountDto.getGameName(),
                accountDto.getTagLine(),
                summonerDto.getProfileIconId(),
                LocalDateTime.now(),
                summonerDto.getSummonerLevel()
        );
    }
}
