package com.courses.service;

import com.courses.entity.Resource;

import java.util.List;

public interface IResourceService {

    public List<Resource> findAll();
    public Resource saveResource(Resource resource);
    public Resource findById(Long id);
    public void deleteById(Long id);
    public Resource createResource();
}
