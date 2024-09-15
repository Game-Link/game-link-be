package com.gamelink.backend.domain.user.repository.custom;

import com.gamelink.backend.domain.user.model.entity.Device;

import java.util.Optional;
import java.util.UUID;

public interface CustomDeviceRepository {
    Optional<Device> findOneByUserSubId(UUID userSubId);
}
