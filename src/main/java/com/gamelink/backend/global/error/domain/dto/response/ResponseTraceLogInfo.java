package com.gamelink.backend.global.error.domain.dto.response;

import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseTraceLogInfo {
    @Schema(description = "에러 발생 지점", example = "2024-07-21T00:59:29.775Z ERROR 1 --- [nio-8080-exec-5] f.b.t.global.error.ControllerAdvisor     : A problem has occurred in controller advice: [id=3176421d-9c97-41e5-a6c6-17e6c71731e3]")
    private final String errorPoint;
    @Schema(description = "에러 코드 메시지", example = "notsupport.http-method")
    private final String errorCode;
    @Schema(description = "에러 발생 원인", example = "Caused by: org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'GET' is not supported")
    private final String causedBy;
    @Schema(description = "에러가 발생된 서비스 포인트", example = "feasta.backend.template.application.service.impl.CertificationManageServiceImpl.uploadCertification(CertificationManageServiceImpl.java:60)")
    private final String servicePoint;

    public ResponseTraceLogInfo(ErrorLogEntity errorLog) {
        this.errorPoint = errorLog.getErrorPoint();
        this.errorCode = errorLog.getErrorCode();
        this.causedBy = errorLog.getCausedBy();
        this.servicePoint = errorLog.getServicePoint();
    }
}
