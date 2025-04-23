package com.courses.java.mapper.impl;

import com.courses.java.dto.SongDto;
import com.courses.java.entity.Song;
import com.courses.java.mapper.ISongMapper;
import com.courses.java.service.impl.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SongMapper implements ISongMapper {

    @Autowired
    private SongService songService;
    public Song dtoToEntity(SongDto songDto) {
        Song song = songService.createSong();
        song.setId(songDto.getId());
        song.setName(songDto.getName());
        song.setAlbum(songDto.getAlbum());
        song.setLength(songDto.getLength());
        song.setAlbum(songDto.getAlbum());
        song.setArtist(songDto.getArtist());
        song.setResourceId(songDto.getResourceId());
        song.setYear(songDto.getYear());
        song.setGenre(songDto.getGenre());
        return song;
    }

    public SongDto entityToDto(Song song) {
        SongDto songDto = new SongDto();
        songDto.setId(song.getId());
        songDto.setName(song.getName());
        songDto.setArtist(song.getArtist());
        songDto.setAlbum(song.getAlbum());
        songDto.setLength(song.getLength());
        songDto.setResourceId(song.getResourceId());
        songDto.setYear(song.getYear());
        songDto.setGenre(song.getGenre());
        return songDto;
    }
}
