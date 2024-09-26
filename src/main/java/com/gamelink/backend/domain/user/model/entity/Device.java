package com.gamelink.backend.domain.user.model.entity;

import com.gamelink.backend.domain.user.model.dto.request.RequestDeviceInfo;
import com.gamelink.backend.domain.user.model.dto.request.RequestKakaoOAuthLogin;
import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "device")
public class Device extends BaseEntity {

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    private String uniqueId;

    private String deviceId;

    private String deviceName;

    private String model;

    public Device(User user, String uniqueId, String deviceId, String deviceName, String model) {
        this.user = user;
        this.uniqueId = uniqueId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.model = model;
    }

    public static Device convertFromLoginRequest(User user, RequestDeviceInfo request) {
        return new Device(
                user,
                request.getUniqueId(),
                request.getDeviceId(),
                request.getDeviceName(),
                request.getModel()
        );
    }
}
