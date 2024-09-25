package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
    @Schema(description = "밴픽 정보")
    private List<BanDto> bans;
    @Schema(description = "오브젝트 정보")
    private ObjectivesDto objectives;
    @Schema(description = "팀 아이디")
    private int teamId;
    @Schema(description = "팀의 승리 여부")
    private boolean win;
}
