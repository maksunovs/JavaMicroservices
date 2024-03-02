package com.cources.java.service;

import com.cources.java.domain.entity.Storage;
import com.cources.java.exception.EntityNotFoundException;
import com.cources.java.exception.error.ErrorResponse;
import com.cources.java.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;

    public Storage findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(new ErrorResponse(HttpStatus.NOT_FOUND, "Storage with ID " + id + " was not found")));
    }

    public List<Storage> findAll() {
        return (List<Storage>) repository.findAll();
    }

    public Storage save(Storage storage) {
        return repository.save(storage);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
