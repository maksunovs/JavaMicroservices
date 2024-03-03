package com.courses.service;

import com.courses.handler.RestResponseEntityExceptionHandler;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Retryable
public class S3StorageService {

    @Value("${s3.storage.url}")
    private String S3_STORAGE_URL;

    @Value("${s3.storage.bucket}")
    private String bucketName;
    @Value("${s3.storage.credentials.user}")
    private String s3User;
    @Value("${s3.storage.credentials.password}")
    private String s3password;
    private MinioClient minioClient;
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


    public String uploadFile(byte[] file, String bucket, String path) {
        LOGGER.info("Uploading file to S3 storage...");
        try (InputStream is = new ByteArrayInputStream(file)) {
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(path)
                    .stream(is, file.length, -1)
                    .contentType("audio/mp3")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public String moveFile(String sourceBucket, String targetBucket, String from, String to) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(targetBucket)
                            .object(to)
                            .source(
                                    CopySource.builder()
                                            .bucket(sourceBucket)
                                            .object(from)
                                            .build())
                            .build());
            removeFile(sourceBucket, from);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return to;
    }

    public InputStream readFile(String bucket, String path) {
        LOGGER.info("Reading file from S3 storage...");
        InputStream stream = null;
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucket)
                                .object(path)
                                .build());
            }
            // Read data from stream
        } catch (Exception e) {
            throw new RuntimeException("Error happened during the file reading: ", e);
        }
        return stream;
    }


    public void removeFile(String bucket, String path) {
        LOGGER.info("Removing file from S3 storage...");
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(path).build());
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
