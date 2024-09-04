package com.gamelink.backend.global.error.domain.model;

import lombok.Getter;

@Getter
public enum ErrorSource {
    CLIENT,
    SERVER;

    public static ErrorSource fromString(String source) {
        return switch (source) {
            case "CLIENT" -> CLIENT;
            case "SERVER" -> SERVER;
            default -> throw new IllegalArgumentException("Unexpected value: " + source);
        };
    }
}
