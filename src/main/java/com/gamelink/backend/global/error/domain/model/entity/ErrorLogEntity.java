package com.gamelink.backend.global.error.domain.model.entity;

import com.gamelink.backend.global.base.BaseEntity;
import com.gamelink.backend.global.error.domain.dto.request.RequestCreateErrorLog;
import com.gamelink.backend.global.error.domain.model.ErrorSource;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "error_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorLogEntity extends BaseEntity {

    private LocalDateTime occuredAt;

    @Enumerated(EnumType.STRING)
    private ErrorSource source;

    private String occurPath;

    @Column(length = 1023)
    private String message;

    private String host;

    private String userAgent;

    private String frontTrackingId;

    private int statusCode;

    private String status;

    private String code;

    @Column(length = 1023)
    private String errorPoint;

    @Column(length = 1023)
    private String errorCode;

    @Column(length = 1023)
    private String causedBy;

    @Column(length = 1023)
    private String servicePoint;

    public ErrorLogEntity(LocalDateTime occuredAt,
                          ErrorSource source,
                          String occurPath,
                          String message,
                          String host,
                          String userAgent,
                          String frontTrackingId,
                          int statusCode,
                          String status,
                          String code,
                          String errorPoint,
                          String errorCode,
                          String causedBy,
                          String servicePoint) {
        this.occuredAt = occuredAt;
        this.source = source;
        this.occurPath = occurPath;
        this.message = message;
        this.host = host;
        this.userAgent = userAgent;
        this.frontTrackingId = frontTrackingId;
        this.statusCode = statusCode;
        this.status = status;
        this.code = code;
        this.errorPoint = errorPoint;
        this.errorCode = errorCode;
        this.causedBy = causedBy;
        this.servicePoint = servicePoint;
    }

    public static ErrorLogEntity convertFromCreateReq(RequestCreateErrorLog request) {
        return new ErrorLogEntity(
                request.getTime(),
                ErrorSource.fromString(request.getSource()),
                request.getPath(),
                request.getMessage(),
                request.getHost(),
                request.getUserAgent(),
                request.getTrackingId(),
                0,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
