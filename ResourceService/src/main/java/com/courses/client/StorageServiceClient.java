package com.courses.client;

import com.courses.client.dto.StorageDto;
import com.courses.client.dto.StorageListDto;
import com.squareup.okhttp.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class StorageServiceClient {
    @Value("${api.gateway.url}")
    private String API_GATEWAY_URL;

    private StorageListDto storageListDtoStub;

    @CircuitBreaker(name = "getStorages", fallbackMethod = "fallbackForGetStorages")
    public StorageListDto getStorages() {
        log.info("Reading storages...");
        RestTemplate restTemplate = new RestTemplate();
        StorageListDto storageListDto = restTemplate.getForObject(API_GATEWAY_URL + "/storages", StorageListDto.class);
        storageListDtoStub = storageListDto;
        return storageListDto;
    }

    public StorageListDto fallbackForGetStorages(Exception e) {
        log.error("Executing fallbackForGetStorages(): {}", e.getMessage());
        return Optional.ofNullable(storageListDtoStub).orElseThrow(()->new RuntimeException("Not stub was created for storages."));
    }
}
