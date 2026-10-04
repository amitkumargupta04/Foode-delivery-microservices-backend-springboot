package com.fooddelivery.account_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    String uploadFile(MultipartFile file);
    List<String> uploadFiles(List<MultipartFile> files);
}
