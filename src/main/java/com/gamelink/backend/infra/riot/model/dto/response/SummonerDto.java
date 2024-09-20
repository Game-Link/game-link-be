package com.gamelink.backend.infra.riot.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummonerDto {

    @Schema(description = "암호화된 AccountId")
    private String accountId;

    @Schema(description = "소환사 아이콘 아이디")
    private int profileIconId;

    @Schema(description = "소환사의 마지막 수정날짜")
    private Long revisionDate;

    @Schema(description = "암호화된 SummonerId")
    private String id;

    @Schema(description = "암호화된 PUUID")
    private String puuid;

    @Schema(description = "소환사 레벨")
    private Long summonerLevel;
}
