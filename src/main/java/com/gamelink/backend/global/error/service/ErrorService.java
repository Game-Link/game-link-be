package com.gamelink.backend.global.error.service;

import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.base.ResponsePage;
import com.gamelink.backend.global.error.domain.dto.request.RequestCreateErrorLog;
import com.gamelink.backend.global.error.domain.dto.response.ResponseErrorDetail;
import com.gamelink.backend.global.error.domain.dto.response.ResponseErrorInfo;
import com.gamelink.backend.global.error.domain.model.ErrorSource;
import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ErrorService {
    ResponsePage<ResponseErrorInfo> getAllErrorLog(ErrorSource source, Pageable pageable, AppAuthentication auth);

    void createErrorLog(RequestCreateErrorLog request);

    ResponseErrorDetail findOne(Long id, AppAuthentication auth);

    void deleteAll(AppAuthentication auth) throws IOException;

    void saveErrorLogEntity(ErrorLogEntity entity);
}
