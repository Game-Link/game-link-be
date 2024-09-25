package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BanDto {
    @Schema(description = "밴된 챔피언 아이디")
    private int championId;
    @Schema(description = "밴픽 순서")
    private int pickTurn;
}
