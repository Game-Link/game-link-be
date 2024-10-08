package com.gamelink.backend.infra.s3.model.dto;

import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
public class UploadedImage {
    private final String imageId;

    private final String originalImageName;

    private final MediaType mimeType;

    private final ImageRequest image;

    public UploadedImage(String imageId, ImageRequest image) {
        this.imageId = imageId;
        this.originalImageName = image.getOriginalImageName();
        this.mimeType = image.getContentType();
        this.image = image;
    }
}