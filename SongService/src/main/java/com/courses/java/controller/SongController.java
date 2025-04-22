package com.courses.java.controller;

import com.courses.java.dto.SongDto;
import com.courses.java.entity.Song;
import com.courses.java.mapper.impl.SongMapper;
import com.courses.java.service.impl.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Validated
@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private ObjectMapper objectMapper;

//    @GetMapping
//    public List<SongDto> getAll() {
//        return songService.findAll().stream().map(a -> (songMapper.entityToDto(a))).toList();
//    }

    @GetMapping
    public List<SongDto> getByResourceId(@RequestParam(name = "resourceId", required = false) Long resourceId) {
        return resourceId == null ? songService.findAll().stream().map(a -> (songMapper.entityToDto(a))).toList() : songService.findByResourceId(resourceId).stream().map(songMapper::entityToDto).toList();
    }
    
    @GetMapping("/{id}")
    public SongDto getById(@PathVariable @Min(0L) @Max(Long.MAX_VALUE) Long id) {
        return songMapper.entityToDto(songService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SongDto save(@Valid @RequestBody SongDto songDto) {
        Song song = songMapper.dtoToEntity(songDto);
        Long id = songService.saveSong(song).getId();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("id", id);
        return songMapper.entityToDto(song);
    }

    @DeleteMapping
    public ObjectNode delete(@RequestParam("ids")
                             @Length(max = 200, message = "Parameter length mustn't be more then 200 characters")
                             @Pattern(regexp = "^(\\d{1,10}|(\\d{1,10},(\\d{1,10},){0,20}\\d{1,10}))$", message = "Value must be comma-separated ten-digit numbers")
                             String idsString) {
        List<Long> ids = Arrays.stream(idsString.split(",")).map(Long::parseLong).toList();
        ids.forEach(songService::deleteById);
        ArrayNode arrayNode = objectMapper.createObjectNode()
                .putArray("ids");
        ids.forEach(arrayNode::add);
        ObjectNode node = objectMapper.createObjectNode();
        node.set("ids", arrayNode);
        return node;
    }
}
