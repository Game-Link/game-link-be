package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.Queue;

public interface QueueRepository extends CustomJpaRepository<Queue, Long> {
}
