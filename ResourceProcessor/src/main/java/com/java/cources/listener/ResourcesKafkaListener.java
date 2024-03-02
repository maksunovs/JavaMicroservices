package com.java.cources.listener;

import com.drew.lang.StringUtil;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.java.cources.parser.AudioFileParser;
import com.java.cources.service.ResourceWebService;
import com.java.cources.service.SongWebService;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import okhttp3.internal.http2.Http2;
import org.json.JSONObject;
import org.netpreserve.jwarc.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Map;

@Component
@KafkaListener(id = "1", topics = "new-resources")
public class ResourcesKafkaListener {

    private static final String PROCESSED_TOPIC = "processed-resource";
    @Autowired
    private AudioFileParser parser;

    @Autowired
    private ResourceWebService resourceWebService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private SongWebService songWebService;

    @KafkaHandler
    public void handleString(String resourceId) {
        System.out.println("Resource id received: " + resourceId);
        try {

            // Retrieve resource with file
            Response resourcesResponse = resourceWebService.callGetResourceById(Long.parseLong(resourceId));
            Response songsResponse;
            try (ResponseBody resourcesResponseBody = resourcesResponse.body()) {
                if (HttpURLConnection.HTTP_OK == resourcesResponse.code()) {
                    JSONObject jsonObject = new JSONObject(resourcesResponseBody.string());
                    String base64bytes = jsonObject.getString("audioBytes");
                    byte[] audioBytes = Base64.getDecoder().decode(base64bytes);
                    // read audio bytes
                    try (InputStream is = new ByteArrayInputStream(audioBytes)) {
                        // parse audio metadata
                        Map<String, String> songPropertiesMap = (parser.parseMetadata(is));
                        // put resource id to map
                        songPropertiesMap.put("resourceId", resourceId);
                        // call web service to save song
                        songsResponse = songWebService.callSaveSongMetadata(songPropertiesMap);
                        try (ResponseBody songsResponseBody = songsResponse.body()) {
                            if (HttpURLConnection.HTTP_CREATED != songsResponse.code()
                                    && HttpURLConnection.HTTP_OK != songsResponse.code()) {
                                throw new RuntimeException("Song service error: " + songsResponseBody.string());
                            }
                            kafkaTemplate.send(PROCESSED_TOPIC, resourceId);
                        }
                    }
                } else {
                    System.out.println(resourcesResponse.code() + ": " + resourcesResponseBody.string());
                }
            }

        } catch (NumberFormatException e) {
            throw new RuntimeException("Request resource is invalid: " + resourceId);
        } catch (IOException e) {
            throw new RuntimeException("Request to resource service failed: ", e);
        }

    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Unkown type received: " + object);
    }
}