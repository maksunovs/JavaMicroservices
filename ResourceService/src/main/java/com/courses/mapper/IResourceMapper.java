package com.courses.mapper;

import com.courses.dto.ResourceDto;
import com.courses.entity.Resource;

public interface IResourceMapper {
    public Resource dtoToEntity(ResourceDto songDto) ;

    public ResourceDto entityToDto(Resource song);
}
