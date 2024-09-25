package com.gamelink.backend.infra.riot.model.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetadataDto {

    private String dataVersion;
    private String matchId;
    private List<String> participants;
}
