package com.courses.mapper;

import com.courses.dto.ResourceDto;
import com.courses.dto.ResourceResponse;
import com.courses.entity.Resource;

public interface IResourceMapper {
    public Resource dtoToEntity(ResourceDto resourceDto) ;

    @Deprecated
    public ResourceDto entityToDto(Resource resource);

    public ResourceResponse entityToResponse(Resource resource);
}
