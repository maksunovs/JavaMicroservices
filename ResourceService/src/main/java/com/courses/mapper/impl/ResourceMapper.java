package com.courses.mapper.impl;

import com.courses.client.dto.StorageType;
import com.courses.dto.ResourceDto;
import com.courses.dto.ResourceResponse;
import com.courses.entity.Resource;
import com.courses.mapper.IResourceMapper;
import com.courses.service.impl.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ResourceMapper implements IResourceMapper {
    @Autowired
    private ResourceService resourceService;
    @Override
    public Resource dtoToEntity(ResourceDto resourceDto) {
        Resource resource = new Resource();
        resource.setId(resourceDto.getId());
        resource.setAudioBytes(Base64.getDecoder().decode(resourceDto.getAudioBytes()));
        resource.setStorage(StorageType.valueOf(resourceDto.getStorage()));
        return resource;
    }

    @Override
    public ResourceDto entityToDto(Resource resource) {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId());
        resourceDto.setAudioBytes(new String(Base64.getEncoder().encode(resource.getAudioBytes())));
        resourceDto.setStorage(resource.getStorage().name());
        return resourceDto;
    }

    @Override
    public ResourceResponse entityToResponse(Resource resource) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(resource.getId());
        resourceResponse.setSourcePath(resource.getSourcePath());
        resourceResponse.setStorage(resource.getStorage().name());
        return resourceResponse;
    }
}
