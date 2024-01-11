package com.courses.java.repository;

import com.courses.java.entity.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISongRepository extends CrudRepository<Song, Long> {
    public List<Song> findByResourceId(Long id);
}
