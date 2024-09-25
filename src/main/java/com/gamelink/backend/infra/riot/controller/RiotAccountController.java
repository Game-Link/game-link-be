package com.gamelink.backend.infra.riot.controller;

import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.auth.role.UserAuth;
import com.gamelink.backend.infra.riot.model.dto.MatchDto;
import com.gamelink.backend.infra.riot.model.dto.ParticipantDto;
import com.gamelink.backend.infra.riot.model.dto.request.RequestRiotAccountDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerInfoDto;
import com.gamelink.backend.infra.riot.service.RiotAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riot/lol/account")
@Tag(name = "Riot 계정", description = "Riot 계정 정보 조회 API")
@Slf4j
public class RiotAccountController {

    private final RiotAccountService riotAccountService;

    /**
     * Riot 계정 정보 신규 등록
     *
     * <p>Riot 계정 정보를 최신화 하기 위해서는 새로고침 api를 호출해야 합니다.</p>
     */
    @PostMapping("/register")
    @UserAuth
    public void registerRiotAccountInfo(@Valid @RequestBody RequestRiotAccountDto request,
                                        AppAuthentication auth) {
        riotAccountService.registerRiotAccountInfo(request.getGameName(), request.getTagLine(), UUID.fromString(auth.getUserSubId()));
    }

    /**
     * 자신의 Riot 계정 정보 조회
     *
     * <p>게임 이름과 태그로 Riot 계정 정보 가져오기 </p>
     * <p>Riot 계정 정보를 최신화하기 위해서는 새로고침 api를 호출해야합니다.</p>
     */
    @GetMapping
    @UserAuth
    // TODO : profileId를 사진으로 대체하여 URL 링크로 변경해야함
    public ResponseSummonerInfoDto getRiotUserInfo(AppAuthentication auth) {
        return riotAccountService.getRiotUserInfo(UUID.fromString(auth.getUserSubId()));
    }

    /**
     * Riot 계정 정보 새로고침
     *
     * <p>Riot의 사용자 계정 정보를 새로고침하는 API입니다.</p>
     */
    @PatchMapping("/refresh")
    public void refreshRiotAccountInfo(@RequestParam UUID userId) {
        riotAccountService.refreshRiotAccountInfo(userId);
    }

    /**
     * Riot 계정 아이디 / 태그 변경
     *
     * <p>둘 중에 하나만 입력되고 변경 가능합니다.</p>
     */
    @PatchMapping
    @UserAuth
    public void changeRiotAccountInfo(@Valid @RequestBody RequestRiotAccountDto request,
                                      AppAuthentication auth) {
        riotAccountService.changeRiotAccountInfo(request.getGameName(), request.getTagLine(), UUID.fromString(auth.getUserSubId()));
    }

    /**
     * Riot 계정 전적 정보 조회
     */
    @GetMapping("/match")
    @UserAuth
    public void getRiotMatchInfo(AppAuthentication auth) {
        List<MatchDto> riotMatchInfo = riotAccountService.getRiotMatchInfo(UUID.fromString(auth.getUserSubId()));
    }
}
