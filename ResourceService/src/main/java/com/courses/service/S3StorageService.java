package com.courses.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
public class S3StorageService {

    @Value("${s3.storage.url}")
    private String S3_STORAGE_URL;

    private String bucketName = "test_bucket";
    private MinioClient minioClient;

    @PostConstruct
    void init() {
        minioClient =
                MinioClient.builder()
                        .endpoint(S3_STORAGE_URL)
                        .credentials("minio_user", "minio_password")
                        .build();

        createBucket();

    }

    public String uploadFile(InputStream file) {
        try {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object("Resume.mp3")
                    .stream(file, -1, -1)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Resume.mp3";
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
