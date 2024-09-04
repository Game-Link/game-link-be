package com.gamelink.backend.global.auth.role;

import com.gamelink.backend.global.auth.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecurityRequirement(name = JwtProvider.AUTHORIZATION)
@PreAuthorize("@RoleService.matchWithRole('ROLE_ADMIN')")
public @interface AdminAuth {
}
