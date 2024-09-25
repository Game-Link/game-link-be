package com.gamelink.backend.infra.s3.service;

import com.gamelink.backend.infra.s3.model.dto.FileRequest;
import com.gamelink.backend.infra.s3.model.dto.UploadedFile;

import java.util.ArrayList;
import java.util.List;

public interface FileUploadService {

    ArrayList<UploadedFile> uploadedFiles(List<FileRequest> files);

    UploadedFile uploadFile(FileRequest file);
}
