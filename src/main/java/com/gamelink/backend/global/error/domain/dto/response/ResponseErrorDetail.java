package com.gamelink.backend.global.error.domain.dto.response;

import com.gamelink.backend.global.error.domain.model.ErrorSource;
import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ResponseErrorDetail {
    @Schema(description = "에러 발생 환경", example = "SERVER")
    private final String source;
    @Schema(description = "에러 로그 Id", example = "1")
    private final Long id;
    @Schema(description = "트래킹 Id", example = "509840914 | c74b07a6-890e-4aff-g034-5232e4810f9d")
    private final String trackingId;
    @Schema(description = "에러 발생 시간", example = "2024.07.21 10:16:27.51 GMT+09:00")
    private final String time;
    @Schema(description = "에러 발생 경로", example = "/test/log")
    private final String path;
    @Schema(description = "에러 메시지", example = "에러 로그가 존재하지 않습니다.")
    private final String message;
    @Schema(description = "에러가 발생한 도메인", example = "localhost")
    private final String host;

    @Schema(description = "유저 에이전트", example = "User-Agent")
    private final String userAgent;

    @Schema(description = "에러 상태 코드", example = "404")
    private final Integer statusCode;
    @Schema(description = "에러 상태", example = "NOT_FOUND")
    private final String status;
    @Schema(description = "에러 코드", example = "BadRequestException")
    private final String code;

    private final ResponseTraceLogInfo traceLog;

    public ResponseErrorDetail(ErrorLogEntity errorLog, ResponseTraceLogInfo traceLog) {
        this.source = errorLog.getSource().toString();
        this.id = errorLog.getId();
        this.trackingId = (errorLog.getSource() == ErrorSource.SERVER) ? errorLog.getSubId().toString() : errorLog.getFrontTrackingId();
        this.time = changeFormat(errorLog.getCreatedAt());
        this.path = errorLog.getOccurPath();
        this.message = errorLog.getMessage();
        this.host = errorLog.getHost();
        this.userAgent = errorLog.getUserAgent();
        this.statusCode = (errorLog.getSource() == ErrorSource.SERVER) ? errorLog.getStatusCode() : null;
        this.status = (errorLog.getSource() == ErrorSource.SERVER) ? errorLog.getStatus() : null;
        this.code = (errorLog.getSource() == ErrorSource.SERVER) ? errorLog.getCode() : null;
        this.traceLog = traceLog;
    }

    private String changeFormat(LocalDateTime createdAt) {
        ZonedDateTime zonedDateTime = createdAt.atZone(ZoneId.systemDefault());
        return DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss.SS 'GMT'XXX")
                .format(zonedDateTime);
    }
}