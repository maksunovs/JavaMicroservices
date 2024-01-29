package com.courses.service.impl;

import com.courses.entity.Resource;
import com.courses.exception.EntityNotFoundException;
import com.courses.exception.NotAudioFileException;
import com.courses.exception.error.ErrorResponse;
import com.courses.repository.IResourceRepository;
import com.courses.service.IResourceService;
import com.courses.service.S3StorageService;
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
import java.util.Optional;

@Service
public class ResourceService implements IResourceService {
    private static final String AUDIO_CONTENT_TYPE = "audio/mpeg";
    private static final Logger LOGGER = Logger.getLogger(ResourceService.class);
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

    @Override
    public List<Resource> findAll() {
        return (List<Resource>) resourceRepository.findAll();
    }

    @Override
    public Resource saveResource(final Resource resource) {
        Resource savedResource;
        validate(resource);
        String filePath = s3StorageService.uploadFile(resource.getAudioBytes());
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
            s3StorageService.removeFile(filePath);
            throw e;
        }
        return savedResource;
    }

    @Override
    public Resource findById(Long id) {
        LOGGER.info("Searching resource by ID...");
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(new ErrorResponse(HttpStatus.NOT_FOUND, "Resource with ID " + id + " was not found")));
        try (InputStream is = s3StorageService.readFile(bucketName, resource.getSourcePath())) {
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
        resource.ifPresent(value -> s3StorageService.removeFile(value.getSourcePath()));
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

