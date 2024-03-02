package com.cources.java.controller;

import com.cources.java.controller.dto.StorageDto;
import com.cources.java.controller.dto.StorageListDto;
import com.cources.java.mapper.StorageMapper;
import com.cources.java.service.StorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/storages")
public class StorageController {

    @Autowired
    private StorageService service;

    @Autowired
    private StorageMapper mapper;

    @GetMapping
    public StorageListDto findAll() {
        return new StorageListDto(service.findAll().stream().map(s->mapper.toDto(s)).toList());
    }

    @GetMapping("/{id}")
    public StorageDto findById(@PathVariable("id") @Min(0L) @Max(Long.MAX_VALUE) Long id) {
        return mapper.toDto(service.findById(id));
    }

    @PostMapping
    public StorageDto save(@RequestBody @Valid StorageDto storageDto) {
        return mapper.toDto(service.save(mapper.toEntity(storageDto)));
    }

    @DeleteMapping
    public void delete(@RequestParam("ids") @Length(max = 200, message = "Parameter length mustn't be more then 200 characters")
                      @Pattern(regexp = "^(\\d{1,10}|(\\d{1,10},(\\d{1,10},){0,20}\\d{1,10}))$", message = "Value must be comma-separated ten-digit numbers") String idsString) {
        List<Long> ids = Arrays.stream(idsString.split(",")).map(Long::parseLong).toList();
        ids.forEach(service::delete);
    }
}

