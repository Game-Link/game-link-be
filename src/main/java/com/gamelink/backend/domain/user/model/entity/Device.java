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

    // TODO : 다른 필드들은 일단 사용하지 않는 것으로 판단
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    private String uniqueId;

    private String deviceId;

    private String deviceName;

    private String model;

    public Device(User user, String deviceId) {
        this.user = user;
        this.uniqueId = null;
        this.deviceId = deviceId;
        this.deviceName = null;
        this.model = null;
    }

    public static Device convertFromLoginRequest(User user, String deviceId) {
        return new Device(
                user,
                deviceId
        );
    }
}
