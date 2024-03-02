package com.cources.java.mapper;

import com.cources.java.controller.dto.StorageDto;
import com.cources.java.domain.entity.Storage;
import com.cources.java.domain.entity.StorageType;
import org.springframework.stereotype.Component;

@Component
public class StorageMapper {
    public StorageDto toDto(Storage storage){
        return new StorageDto(storage.getId(), storage.getStorageType().name(), storage.getPath());
    }

    public Storage toEntity(StorageDto storageDto){
        return new Storage(storageDto.getId(), StorageType.valueOf(storageDto.getStorageType()), storageDto.getPath());
    }
}
