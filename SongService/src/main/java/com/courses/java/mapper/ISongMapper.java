package com.courses.java.mapper;

import com.courses.java.dto.SongDto;
import com.courses.java.entity.Song;

public interface ISongMapper {
    public Song dtoToEntity(SongDto songDto) ;

    public SongDto entityToDto(Song song);
}
