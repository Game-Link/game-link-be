package com.gamelink.backend.domain.user.model.dto;

import com.gamelink.backend.domain.user.model.entity.ProfileImage;
import com.gamelink.backend.infra.s3.service.AWSObjectStorageService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class ProfileImageDto {

    @Schema(description = "프로필 이미지 Id", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID id;

    @Schema(description = "프로필 이미지 url")
    private final String url;

    private final String originalName;

    private final String mimeType;

    public ProfileImageDto(ProfileImage image, AWSObjectStorageService s3service) {
        this.id = image.getSubId();
        this.url = s3service.getImageUrl(image.getImageId());
        this.originalName = image.getImageName();

        String imageMimeType = image.getMimeType();
        this.mimeType = Objects.requireNonNullElse(imageMimeType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    public static List<ProfileImageDto> listOf(AWSObjectStorageService s3service, List<ProfileImage> entities) {
        return entities.stream()
                .map(image -> new ProfileImageDto(image, s3service))
                .collect(Collectors.toList());
    }
}
