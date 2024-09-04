package com.gamelink.backend.global.auth.role.service.impl;

import com.gamelink.backend.global.auth.UserRole;
import com.gamelink.backend.global.auth.jwt.JwtAuthentication;
import com.gamelink.backend.global.auth.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("RoleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Override
    public boolean matchWithRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            UserRole roleOfMine = ((JwtAuthentication) authentication).getUserRole();
            return roleOfMine.contains(role);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AccessDeniedException("invalid.role");
        }
    }
}
