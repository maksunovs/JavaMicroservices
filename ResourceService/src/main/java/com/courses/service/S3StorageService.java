package com.courses.service;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class S3StorageService {

    @Value("${s3.storage.url}")
    private String S3_STORAGE_URL;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;
    @Value("${S3_USER}")
    private String s3User;
    @Value("${S3_PASSWORD}")
    private String s3password;
    private MinioClient minioClient;

    @PostConstruct
    void init() {
        minioClient =
                MinioClient.builder()
                        .endpoint(S3_STORAGE_URL)
                        .credentials(s3User, s3password)
                        .build();

        createBucket();

    }

    public String uploadFile(InputStream file) {
        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(RandomStringUtils.randomAlphanumeric(8))
                    .stream(file, -1, 10485760)
                    .contentType("audio/mp3")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Resume.mp3";
    }

    public void removeFile(String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createBucket() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(bucketName)
                                .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
