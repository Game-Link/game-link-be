package com.gamelink.backend.domain.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeviceInfo {

    private String uniqueId;

    private String model;

    private String deviceId;

    private String deviceName;
}
