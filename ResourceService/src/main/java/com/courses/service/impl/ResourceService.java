package com.courses.service.impl;

import com.courses.entity.Resource;
import com.courses.exception.EntityNotFoundException;
import com.courses.exception.NotAudioFileException;
import com.courses.exception.SongServiceException;
import com.courses.exception.error.ErrorResponse;
import com.courses.parser.AudioFileParser;
import com.courses.repository.IResourceRepository;
import com.courses.service.IResourceService;
import com.courses.service.S3StorageService;
import com.courses.service.SongWebService;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceService implements IResourceService {
    private static final String AUDIO_CONTENT_TYPE = "audio/mpeg";
    @Autowired
    private IResourceRepository resourceRepository;
    @Autowired
    private AudioFileParser parser;

    @Autowired
    private SongWebService songService;

    @Autowired
    private S3StorageService s3StorageService;

    @Override
    public List<Resource> findAll() {
        return (List<Resource>) resourceRepository.findAll();
    }

    @Override
    @Transactional
    public Resource saveResource(Resource resource) {
        validate(resource);
        String filePath = s3StorageService.uploadFile(resource.getInputStream());
        resource.setSourcePath(filePath);
        try {
            resource = resourceRepository.save(resource);
        } catch (Exception e) {
            s3StorageService.removeFile(filePath);
            throw e;
        }
        return resource;
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(new ErrorResponse(HttpStatus.NOT_FOUND, "Resource with ID " + id + " was not found")));
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

