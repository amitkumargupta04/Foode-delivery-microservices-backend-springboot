package com.fooddelivery.account_service.controller;

import com.fooddelivery.account_service.dto.response.StandardResponse;
import com.fooddelivery.account_service.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "File Upload APIs")
public class AccountController {

    private final S3Service s3Service;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Upload single file")
    public ResponseEntity<StandardResponse<String>> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        String fileUrl = s3Service.uploadFile(file);
        StandardResponse<String> response = new StandardResponse<>(
                true,
                "File Uploaded Successfully!",
                HttpStatus.OK.value(),
                fileUrl,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(
            value = "/uploads",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Upload multiple files")
    public ResponseEntity<StandardResponse<List<String>>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files
    ) {
        List<String> fileUrls = s3Service.uploadFiles(files);
        StandardResponse<List<String>> response = new StandardResponse<>(
                true,
                "Files Uploaded Successfully!",
                HttpStatus.OK.value(),
                fileUrls    ,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
