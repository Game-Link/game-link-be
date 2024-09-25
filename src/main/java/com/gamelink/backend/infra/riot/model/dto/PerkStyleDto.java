package com.gamelink.backend.infra.riot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PerkStyleDto {

    private String description;
    private List<PerkStyleSelectionDto> selections;
    private int style;
}
