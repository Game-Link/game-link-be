package com.gamelink.backend.infra.riot.controller;

import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.auth.role.UserAuth;
import com.gamelink.backend.infra.riot.model.dto.request.RequestRiotAccountDto;
import com.gamelink.backend.infra.riot.model.dto.response.CacheMatchDataDto;
import com.gamelink.backend.infra.riot.model.dto.response.ResponseSummonerInfoDto;
import com.gamelink.backend.infra.riot.service.RiotAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    // TODO : SSO 인증을 통한 Riot 계정 등록으로 변경
    public void registerRiotAccountInfo(@Valid @RequestBody RequestRiotAccountDto request,
                                        AppAuthentication auth) {
        riotAccountService.registerRiotAccountInfo(request.getGameName(), request.getTagLine(), UUID.fromString(auth.getUserSubId()));
    }

    /**
     * 자신의 Riot 계정 정보 조회 및 내 정보 조회
     *
     * <p>자신의 Riot 계정 정보 및 내 정보를 조회할 수 있습니다.</p>
     * <p>Riot 계정 정보를 최신화하기 위해서는 새로고침 api를 호출해야합니다.</p>
     */
    @GetMapping
    @UserAuth
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
     *
     * <p>start는 5의 배수로 Riot Api에서 매치Id를 가져올 위치를 지정합니다. ex)0, 5, 10, 15... </p>
     */
    @GetMapping("/match")
    @UserAuth
    public List<CacheMatchDataDto> getRiotMatchInfo(AppAuthentication auth,
                                                    @RequestParam(defaultValue = "0") int start) {
        return riotAccountService.getRiotMatchInfo(UUID.fromString(auth.getUserSubId()), start);
    }

    /**
     * Riot 계정 전적 정보 새로고침
     *
     * <p>중복되지 않은 최신 매치 데이터만을 새로고침하기 때문에 Riot 계정 전적 정보 조회 기능을 우선으로 호출해야 합니다.</p>
     */
    @PatchMapping("/match/refresh")
    @UserAuth
    public void refreshRiotMatchInfo(AppAuthentication auth) {
        riotAccountService.refreshRiotMatchInfo(UUID.fromString(auth.getUserSubId()));
    }
}
