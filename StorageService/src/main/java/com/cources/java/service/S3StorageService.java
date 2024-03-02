package com.cources.java.service;

import com.cources.java.domain.entity.Storage;
import com.cources.java.domain.entity.StorageType;
import io.minio.*;
import jakarta.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Retryable
public class S3StorageService {

    private static final String PERMANENT_BUCKET="permanent-bucket";
    private static final String STAGING_BUCKET="staging-bucket";

    @Value("${s3.storage.url}")
    private String S3_STORAGE_URL;
    @Value("${s3.storage.bucket}")
    private String bucketName;
    @Value("${s3.storage.credentials.user}")
    private String s3User;
    @Value("${s3.storage.credentials.password}")
    private String s3password;
    private MinioClient minioClient;
    @Autowired
    private StorageService service;
    private static final Logger LOGGER = Logger.getLogger(S3StorageService.class);
    @PostConstruct
    void init() {
        minioClient =
                MinioClient.builder()
                        .endpoint(S3_STORAGE_URL)
                        .credentials(s3User, s3password)
                        .build();

        createBucket();

    }

    private void createBucket() {
        try {
            // permanent bucket initialization
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(PERMANENT_BUCKET).build())) {
                minioClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(PERMANENT_BUCKET)
                                .build());
                service.save(new Storage(null, StorageType.PERMANENT, PERMANENT_BUCKET, "/"));
            }
            // staging bucket initialization
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(STAGING_BUCKET).build())) {
                minioClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(STAGING_BUCKET)
                                .build());
                service.save(new Storage(null, StorageType.STAGING, STAGING_BUCKET, "/"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
