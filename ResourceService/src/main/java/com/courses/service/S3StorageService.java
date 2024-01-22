package com.courses.service;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
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

    @PostConstruct
    void init() {
        minioClient =
                MinioClient.builder()
                        .endpoint(S3_STORAGE_URL)
                        .credentials(s3User, s3password)
                        .build();

        createBucket();

    }

    public String uploadFile(byte[] file) {
        String path = RandomStringUtils.randomAlphanumeric(8);
        try(InputStream is = new ByteArrayInputStream(file)) {
            System.out.println(file.length);
            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(is, file.length, -1)
                    .contentType("audio/mp3")
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return path;
    }

    public InputStream readFile(String bucket, String path) {
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
            throw new RuntimeException("Error happened during the file reading: ",e);
        }
        return stream;
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
