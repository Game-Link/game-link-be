package com.gamelink.backend.infra.riot.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestRiotAccountDto {

    @Schema(description = "게임 이름")
    private String gameName;

    @Schema(description = "소환사 태그")
    private String tagLine;
}
