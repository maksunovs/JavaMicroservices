package com.courses.listener;

import com.courses.client.dto.StorageType;
import com.courses.entity.Resource;
import com.courses.repository.IResourceRepository;
import com.courses.service.S3StorageService;
import com.courses.service.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "1", topics = "processed-resource")
public class ResourcesKafkaListener {

    private static final String PERMANENT_BUCKET = "permanent-bucket";
    private static final String STAGING_BUCKET = "staging-bucket";

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private IResourceRepository resourceRepository;
    @KafkaHandler
    public void handleString(String resourceId) {
        System.out.println("Resource with ID " + resourceId+" was successfully processed. Moving file to PERMANENT storage.");
        try {
            Resource resource = resourceService.findById(Long.parseLong(resourceId));
            String newPath = s3StorageService.uploadFile(resource.getAudioBytes(), PERMANENT_BUCKET, resource.getSourcePath());
            s3StorageService.removeFile(STAGING_BUCKET, resource.getSourcePath());
            resource.setStorage(StorageType.PERMANENT);
            resource.setSourcePath(newPath);
            resourceRepository.save(resource);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Request resource is invalid: " + resourceId);
        }

    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unknown type received: " + object);
    }
}