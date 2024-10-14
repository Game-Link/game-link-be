package com.gamelink.backend.debug.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RiotAccessController {

    @Value("${riot.access-key}")
    private String riotAccessKey;

    /**
     * 라이엇 API 접근을 위한 키를 리턴
     */
    @GetMapping("/riot.txt")
    public String getRiotAccess() {
        return riotAccessKey;
    }
}
