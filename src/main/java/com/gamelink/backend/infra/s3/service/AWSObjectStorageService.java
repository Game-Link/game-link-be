package com.gamelink.backend.infra.s3.service;

import com.gamelink.backend.infra.s3.model.dto.FileRequest;
import com.gamelink.backend.infra.s3.model.dto.ImageRequest;

public interface AWSObjectStorageService {

    void uploadFile(FileRequest file, String objectName);

    void uploadImage(ImageRequest image, String objectName);

    String getImageUrl(String objectName);

    String getFileUrl(String objectName);

    void delete(String objectName);
}
