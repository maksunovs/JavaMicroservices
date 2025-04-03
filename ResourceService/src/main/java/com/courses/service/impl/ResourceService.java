package com.courses.service.impl;

import com.courses.entity.Resource;
import com.courses.exception.EntityNotFoundException;
import com.courses.exception.NotAudioFileException;
import com.courses.exception.SongServiceException;
import com.courses.exception.error.ErrorResponse;
import com.courses.parser.AudioFileParser;
import com.courses.repository.IResourceRepository;
import com.courses.service.IResourceService;
import com.courses.service.SongWebService;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ResourceService implements IResourceService {
    public static final String AUDIO_CONTENT_TYPE = "audio/mpeg";
    @Autowired
    private IResourceRepository resourceRepository;
    @Autowired
    private AudioFileParser parser;

    @Autowired
    private SongWebService songService;

    @Override
    public List<Resource> findAll() {
        return (List<Resource>) resourceRepository.findAll();
    }

    @Override
    public Resource saveResource(Resource resource) {
        validate(resource);
        resource = resourceRepository.save(resource);
        songService.mapToSongJson(resource);
        try {
            Response songServiceResponse = songService.callSaveSongMetadata(songService.mapToSongJson(resource));
            try (ResponseBody body = songServiceResponse.body()) {
                if (HttpStatus.CREATED.value() != songServiceResponse.code()) {
                    resourceRepository.deleteById(resource.getId());
                    throw new SongServiceException(new ErrorResponse(HttpStatus.valueOf(songServiceResponse.code()), "Song service failure: " + body.string()));
                }
            }
        } catch (IOException e) {
            resourceRepository.deleteById(resource.getId());
            throw new RuntimeException(e);
        }
        return resource;
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(new ErrorResponse(HttpStatus.NOT_FOUND, "Resource with ID " + id + " was not found")));
    }

    @Override
    public void deleteById(Long id) {
        resourceRepository.deleteById(id);
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

