package com.gamelink.backend.domain.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCreateUserProfileImage {

    @Schema(description = "프로필 이미지")
    private MultipartFile profileImage;
}

