package com.courses.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class S3StorageService {

    @Value("${s3.storage.url}")
    private String S3_STORAGE_URL;

    public String uploadFile(File file) {
        return null;
    }
}
