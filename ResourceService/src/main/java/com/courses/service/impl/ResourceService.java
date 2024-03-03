package com.courses.service.impl;

import com.courses.client.StorageServiceClient;
import com.courses.client.dto.StorageDto;
import com.courses.client.dto.StorageListDto;
import com.courses.client.dto.StorageType;
import com.courses.entity.Resource;
import com.courses.exception.EntityNotFoundException;
import com.courses.exception.NotAudioFileException;
import com.courses.exception.error.ErrorResponse;
import com.courses.repository.IResourceRepository;
import com.courses.service.IResourceService;
import com.courses.service.S3StorageService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ResourceService implements IResourceService {
    private static final String AUDIO_CONTENT_TYPE = "audio/mpeg";
    private static final Logger LOGGER = Logger.getLogger(ResourceService.class);

    private static final String PERMANENT_BUCKET = "permanent-bucket";
    private static final String STAGING_BUCKET = "staging-bucket";
    @Value("${s3.storage.bucket}")
    private String bucketName;
    @Autowired
    private IResourceRepository resourceRepository;

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private StorageServiceClient storageServiceClient;

    @Override
    public List<Resource> findAll() {
        return (List<Resource>) resourceRepository.findAll();
    }

    @Override
    public Resource saveResource(final Resource resource) {
        Resource savedResource;
        validate(resource);
        StorageListDto storageListDto = storageServiceClient.getStorages();
        StorageDto stageStorage = storageListDto.getStorages()
                .stream().filter(s -> s.getStorageType().equalsIgnoreCase(StorageType.STAGING.name())).toList().get(0);
        String filePath = s3StorageService.uploadFile(resource.getAudioBytes(), stageStorage.getBucket(), stageStorage.getPath()+ RandomStringUtils.randomAlphanumeric(8));
        resource.setSourcePath(filePath);
        try {
            savedResource = retryTemplate.execute(context -> {
                LOGGER.info("Saving resource to DB...");
                return resourceRepository.save(resource);
            });
            retryTemplate.execute(context -> {
                LOGGER.info("Sending message to Kafka...");
                kafkaTemplate.send("new-resources", resource.getId().toString());
                return null;
            });
        } catch (Exception e) {
            s3StorageService.removeFile(STAGING_BUCKET,filePath);
            throw e;
        }
        return savedResource;
    }

    @Override
    public Resource findById(Long id) {
        LOGGER.info("Searching resource by ID...");
        Resource resource = resourceRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException(
                        new ErrorResponse(HttpStatus.NOT_FOUND, "Resource with ID " + id + " was not found")));
        try (InputStream is = s3StorageService.readFile(StorageType.PERMANENT == resource.getStorage() ? PERMANENT_BUCKET : STAGING_BUCKET, resource.getSourcePath())) {
            resource.setAudioBytes(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        resourceRepository.deleteById(id);
        resource.ifPresent(value -> s3StorageService.removeFile(StorageType.PERMANENT == value.getStorage() ? PERMANENT_BUCKET : STAGING_BUCKET, value.getSourcePath()));
    }

    @Override
    public byte[] streamToBytes(InputStream is) {
        byte[] dataBytes;
        try (is) {
            dataBytes = is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataBytes;
    }

    public Resource createResource() {
        return new Resource();
    }

    public void validate(Resource resource) {
        if (resource == null || resource.getAudioBytes() == null) {
            throw new RuntimeException("Resource is null.");
        }
        try (InputStream is = new ByteArrayInputStream(resource.getAudioBytes())) {
            Tika tika = new Tika();
            String providedDataType = tika.detect(is);
            if (!AUDIO_CONTENT_TYPE.equalsIgnoreCase(providedDataType)) {
                throw new NotAudioFileException(new ErrorResponse(HttpStatus.BAD_REQUEST, "Provided data is not audio file: " + providedDataType));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

