package com.gamelink.backend.infra.s3.service;

import com.gamelink.backend.infra.s3.model.dto.ImageRequest;
import com.gamelink.backend.infra.s3.model.dto.UploadedImage;

import java.util.ArrayList;
import java.util.List;

public interface ImageUploadService {

    ArrayList<UploadedImage> uploadedImages(List<ImageRequest> images);

    UploadedImage uploadImage(ImageRequest image);
}
