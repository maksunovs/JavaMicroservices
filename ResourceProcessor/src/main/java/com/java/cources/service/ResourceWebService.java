package com.java.cources.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResourceWebService {
    @Value("${api.gateway.url}")
    private String API_GATEWAY_URL;

    public Response callGetResourceById(Long id) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(String.format(API_GATEWAY_URL + "/resources/%d", id))
                .get()
                .build();


        return client.newCall(request).execute();
    }
}
