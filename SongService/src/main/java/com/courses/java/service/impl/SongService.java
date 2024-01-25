package com.courses.java.service.impl;

import com.courses.java.entity.Song;
import com.courses.java.exception.EntityNotFoundException;
import com.courses.java.exception.error.ErrorResponse;
import com.courses.java.repository.ISongRepository;
import com.courses.java.service.ISongService;
import com.courses.java.service.ResourceWebService;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SongService implements ISongService {

    @Autowired
    private ISongRepository songRepository;

    @Autowired
    private ResourceWebService resourceWebService;

    public List<Song> findAll() {
        return (List<Song>) songRepository.findAll();
    }

    public Song saveSong(Song song) {
//        validateSong(song);
        return songRepository.save(song);
    }

    public Song findById(Long id) {
        return songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(new ErrorResponse(HttpStatus.NOT_FOUND, "Song metadata with ID " + id + " was not found.")));
    }

    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

    public Song createSong() {
        return new Song();
    }

    public List<Song> findByResourceId(Long id) {
        return songRepository.findByResourceId(id);
    }

    public void validateSong(Song song) {
        Long resourceId = song.getResourceId();
        if (resourceId != null) {
//            try {
//                Response response = resourceWebService.callGetResourceById(resourceId);
//                try (ResponseBody body = response.body()) {
//                    if (HttpStatus.OK.value() != response.code()) {
//                        throw new EntityNotFoundException(new ErrorResponse(HttpStatus.valueOf(response.code()), body.string()));
//                    } else if (song.getId() == null && !findByResourceId(song.getResourceId()).isEmpty()) {
//                        throw new EntityNotFoundException(new ErrorResponse(HttpStatus.BAD_REQUEST, "Provided resource ID is already in use."));
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
            if (song.getId() == null && !findByResourceId(song.getResourceId()).isEmpty()) {
                throw new EntityNotFoundException(new ErrorResponse(HttpStatus.BAD_REQUEST, "Provided resource ID is already in use."));
            }
        } else {
            throw new IllegalArgumentException("Resource ID cannot be null");
        }
    }
}
