package com.courses.client;

import com.courses.client.dto.StorageDto;
import com.courses.client.dto.StorageListDto;
import com.squareup.okhttp.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class StorageServiceClient {
    @Value("${api.gateway.url}")
    private String API_GATEWAY_URL;

    public StorageListDto getStorages() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(API_GATEWAY_URL + "/storages", StorageListDto.class);
    }
}
