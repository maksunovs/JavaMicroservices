package com.courses.repository;

import com.courses.entity.Resource;
import org.springframework.data.repository.CrudRepository;


public interface IResourceRepository extends CrudRepository<Resource, Long> {
}
