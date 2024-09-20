package com.gamelink.backend.infra.riot.repository;

import com.gamelink.backend.global.base.CustomJpaRepository;
import com.gamelink.backend.infra.riot.model.entity.RiotUser;
import com.gamelink.backend.infra.riot.repository.custom.CustomRiotUserRepository;

public interface RiotUserRepository extends CustomJpaRepository<RiotUser, Long>, CustomRiotUserRepository {
}
