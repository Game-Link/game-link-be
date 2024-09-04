package com.gamelink.backend.global.auth.jwt;

import com.gamelink.backend.domain.user.model.UserStatus;
import com.gamelink.backend.global.auth.UserRole;

public class GuestAuthentication extends JwtAuthentication {

    public GuestAuthentication(String userSubId, UserRole userRole, UserStatus userStatus) {
        super(userSubId, userRole, userStatus);
    }

    public static GuestAuthentication getAuthentication() {
        return new GuestAuthentication(null, UserRole.GUEST, UserStatus.ACTIVE);
    }
}
