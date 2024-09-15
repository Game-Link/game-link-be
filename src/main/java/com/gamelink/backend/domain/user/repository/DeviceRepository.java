package com.gamelink.backend.domain.user.repository;

import com.gamelink.backend.domain.user.model.entity.Device;
import com.gamelink.backend.domain.user.repository.custom.CustomDeviceRepository;
import com.gamelink.backend.global.base.CustomJpaRepository;

public interface DeviceRepository extends CustomJpaRepository<Device, Long>, CustomDeviceRepository {
}
