package com.gamelink.backend.infra.riot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerksDto {

    private PerkStatsDto statPerks;

    private List<PerkStyleDto> styles;
}
