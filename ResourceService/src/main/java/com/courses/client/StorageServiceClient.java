package com.courses.client;

import com.courses.client.dto.StorageDto;
import com.courses.client.dto.StorageListDto;
import com.netflix.discovery.converters.Auto;
import com.squareup.okhttp.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @Autowired
    private OAuth2AuthorizedClientService clientService;
    private StorageListDto storageListDtoStub;

    @CircuitBreaker(name = "getStorages", fallbackMethod = "fallbackForGetStorages")
    public StorageListDto getStorages() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();


        log.info("Reading storages...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
        StorageListDto storageListDto = restTemplate.getForObject(API_GATEWAY_URL + "/storages", StorageListDto.class);
        storageListDtoStub = storageListDto;
        return storageListDto;
    }

    public StorageListDto fallbackForGetStorages(Exception e) {
        log.error("Executing fallbackForGetStorages(): {}", e.getMessage());
        return Optional.ofNullable(storageListDtoStub).orElseThrow(() -> new RuntimeException("Not stub was created for storages."));
    }

    private ClientHttpRequestInterceptor
    getBearerTokenInterceptor(String accessToken) {
        ClientHttpRequestInterceptor interceptor =
                new ClientHttpRequestInterceptor() {
                    @Override
                    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                                                        ClientHttpRequestExecution execution) throws IOException {
                        request.getHeaders().add("Authorization", "Bearer " + accessToken);
                        return execution.execute(request, bytes);
                    }
                };
        return interceptor;
    }
}
