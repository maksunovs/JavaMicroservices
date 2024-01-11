package com.courses.mapper.impl;

import com.courses.dto.ResourceDto;
import com.courses.entity.Resource;
import com.courses.mapper.IResourceMapper;
import com.courses.service.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper implements IResourceMapper {
    @Autowired
    private ResourceService resourceService;
    @Override
    public Resource dtoToEntity(ResourceDto songDto) {
        Resource resource = resourceService.createResource();
        resource.setId(songDto.getId());
        resource.setAudioBytes(songDto.getAudioBytes());
        return resource;
    }

    @Override
    public ResourceDto entityToDto(Resource song) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(song.getId());
        resourceDto.setAudioBytes(song.getAudioBytes());
        return resourceDto;
    }
}
