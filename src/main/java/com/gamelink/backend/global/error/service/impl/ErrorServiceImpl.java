package com.gamelink.backend.global.error.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.base.ResponsePage;
import com.gamelink.backend.global.error.domain.dto.request.RequestCreateErrorLog;
import com.gamelink.backend.global.error.domain.dto.response.ResponseErrorDetail;
import com.gamelink.backend.global.error.domain.dto.response.ResponseErrorInfo;
import com.gamelink.backend.global.error.domain.dto.response.ResponseTraceLogInfo;
import com.gamelink.backend.global.error.domain.model.ErrorSource;
import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import com.gamelink.backend.global.error.exception.ErrorLogNotFoundException;
import com.gamelink.backend.global.error.exception.NotGrantedException;
import com.gamelink.backend.global.error.repository.ErrorRepository;
import com.gamelink.backend.global.error.repository.spec.ErrorLogSpec;
import com.gamelink.backend.global.error.service.ErrorService;
import com.gamelink.backend.global.util.ResponsePageUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorServiceImpl implements ErrorService {
    private final ErrorRepository errorRepository;

    private final ObjectMapper objectMapper;

    private String logDirectoryPath;

    @PostConstruct
    public void init() {
        logDirectoryPath = new File("error_log").getAbsolutePath();
        File logDir = new File(logDirectoryPath);
        if (!logDir.exists()) {
            logDir.mkdir();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ResponsePage<ResponseErrorInfo> getAllErrorLog(ErrorSource source, Pageable pageable, AppAuthentication auth) {
        if (!auth.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }
        Specification<ErrorLogEntity> spec = ErrorLogSpec.withSource(source);
        Pageable sorted = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ErrorLogEntity> page = errorRepository.findAll(spec, sorted);

        List<ResponseErrorInfo> list = page.stream()
                .map(error -> new ResponseErrorInfo(error, (error.getSource() == ErrorSource.SERVER) ? error.getSubId().toString() : error.getFrontTrackingId()))
                .toList();

        return ResponsePageUtil.toResponsePage(list, pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements());
    }

    @Transactional
    @Override
    public void createErrorLog(RequestCreateErrorLog request) {
        ErrorLogEntity errorLog = ErrorLogEntity.convertFromCreateReq(request);
        errorRepository.save(errorLog);
    }

    @Override
    public ResponseErrorDetail findOne(Long id, AppAuthentication auth) {
        if (!auth.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }
        ErrorLogEntity errorLog = errorRepository.findById(id).orElseThrow(ErrorLogNotFoundException::new);
        if (errorLog.getSource() == ErrorSource.CLIENT) {
            return new ResponseErrorDetail(errorLog, null);
        } else {
            return new ResponseErrorDetail(
                    errorLog, new ResponseTraceLogInfo(errorLog)
            );
        }
    }

    @Transactional
    @Override
    public void deleteAll(AppAuthentication auth) throws IOException {
        List<ErrorLogEntity> list = errorRepository.findAll();
        backupErrorLog(list);
        if (!auth.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }
        errorRepository.deleteAll();
    }

    @Transactional
    @Override
    public void saveErrorLogEntity(ErrorLogEntity entity) {
        errorRepository.save(entity);
    }

    private void backupErrorLog(List<ErrorLogEntity> list) throws IOException {
        LocalDateTime min = list.stream()
                .map(ErrorLogEntity::getCreatedAt)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
        String minTime = changeFormat(min);
        LocalDateTime max = list.stream()
                .map(ErrorLogEntity::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
        String maxTime = changeFormat(max);

        File logDir = new File(logDirectoryPath);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        try {
            String fileName = String.format("%s/%s~%s.json", logDirectoryPath, minTime, maxTime);
            File file = new File(fileName);
            objectMapper.writeValue(file, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String changeFormat(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH:mm:ss");
        return time.format(formatter);
    }
}
