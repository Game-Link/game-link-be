package com.gamelink.backend.global.error.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateErrorLog {
    @Schema(description = "에러 발생 시간", example = "2021-08-01T00:00:00")
    private LocalDateTime time;
    @Schema(description = "에러 발생 URL 하위 경로")
    private String path;
    @Schema(description = "에러 발생 환경")
    private String source;
    @Schema(description = "에러 메시지")
    private String message;
    @Schema(description = "유저 에이전트")
    private String userAgent;
    @Schema(description = "에러 발생한 호스트 URL")
    private String host;
    @Schema(description = "digest 값")
    private String trackingId;
}
