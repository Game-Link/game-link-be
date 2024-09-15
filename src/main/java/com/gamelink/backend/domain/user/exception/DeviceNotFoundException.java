package com.gamelink.backend.domain.user.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class DeviceNotFoundException extends LocalizedMessageException {

    public DeviceNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.device");
    }
}
