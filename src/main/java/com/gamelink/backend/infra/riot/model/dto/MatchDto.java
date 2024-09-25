package com.gamelink.backend.infra.riot.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchDto {

    @Schema(description = "경기의 메타데이터")
    private MetadataDto metadata;

    @Schema(description = "경기의 정보")
    private InfoDto info;
}
