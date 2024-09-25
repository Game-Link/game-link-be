package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountDto {
    @Schema(description = "게임 아이디")
    private String puuid;
    @Schema(description = "소환사 이름")
    private String gameName;
    @Schema(description = "소환사 태그")
    private String tagLine;
}
