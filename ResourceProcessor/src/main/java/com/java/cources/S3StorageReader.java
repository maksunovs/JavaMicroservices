package com.java.cources;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.springframework.stereotype.Component;
import java.io.InputStream;

public class S3StorageReader {
    private final MinioClient minioClient;

    public S3StorageReader(String endpointUrl, String user, String password) {
        minioClient =
                MinioClient.builder()
                        .endpoint(endpointUrl)
                        .credentials(user, password)
                        .build();
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
}
