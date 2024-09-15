package com.gamelink.backend.domain.user.repository.custom.impl;

import com.gamelink.backend.domain.user.exception.NotSingleResultException;
import com.gamelink.backend.domain.user.model.entity.Device;
import com.gamelink.backend.domain.user.repository.custom.CustomDeviceRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomDeviceRepositoryImpl implements CustomDeviceRepository {

    private final EntityManager entityManager;


    @Override
    public Optional<Device> findOneByUserSubId(UUID userSubId) {
        List<Device> result = entityManager.createQuery(
                "select d from Device d where d.user.subId = :userSubId",
                Device.class
        )
                .setParameter("userSubId", userSubId)
                .getResultList();

        if (result.size() > 1) {
            throw new NotSingleResultException();
        } else if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }
}
