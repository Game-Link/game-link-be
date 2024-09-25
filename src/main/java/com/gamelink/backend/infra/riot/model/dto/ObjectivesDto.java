package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectivesDto {

    @Schema(description = "팀이 처치한 바론 횟수와 첫 바론 처치 여부")
    private ObjectiveDto baron;

    @Schema(description = "팀이 처치한 챔피언 횟수")
    private ObjectiveDto champion;

    @Schema(description = "팀이 처치한 드래곤 횟수와 첫 드래곤 처치 여부")
    private ObjectiveDto dragon;

    @Schema(description = "팀이 처치한 하수인(호드) 수")
    private ObjectiveDto horde;

    @Schema(description = "팀이 파괴한 억제기 횟수와 첫 억제기 파괴 여부")
    private ObjectiveDto inhibitor;

    @Schema(description = "팀이 처치한 전령 횟수")
    private ObjectiveDto riftHerald;

    @Schema(description = "팀이 파괴한 포탑 횟수와 첫 포탑 파괴 여부")
    private ObjectiveDto tower;
}
