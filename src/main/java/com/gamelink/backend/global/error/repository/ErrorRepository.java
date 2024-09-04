package com.gamelink.backend.global.error.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.global.error.domain.model.entity.ErrorLogEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ErrorRepository extends CustomJpaRepository<ErrorLogEntity, Long>, JpaSpecificationExecutor<ErrorLogEntity> {
}
