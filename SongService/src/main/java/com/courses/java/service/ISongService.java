package com.courses.java.service;

import com.courses.java.entity.Song;

import java.util.List;

public interface ISongService {
    public List<Song> findAll();

    public Song saveSong(Song song);

    public Song findById(Long id);

    public void deleteById(Long id);

    public void validateSong(Song song);

    public List<Song> findByResourceId(Long id);
}
