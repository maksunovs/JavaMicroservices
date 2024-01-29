package com.java.cources.service;

import com.java.cources.parser.AudioFileParser;
import com.squareup.okhttp.*;
import org.apache.tika.metadata.Metadata;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Map;


@Component
public class SongWebService {
    @Autowired
    private AudioFileParser parser;

    @Value("${api.gateway.url}")
    private String API_GATEWAY_URL;

    public JSONObject mapToSongJson(Map<String, String> properties) {
        return new JSONObject()
                .put("name", properties.get("dc:title"))
                .put("album", properties.get("xmpDM:album"))
                .put("artist", properties.get("xmpDM:artist"))
                .put("year", properties.get("xmpDM:releaseDate"))
                .put("resourceId", properties.get("resourceId"))
                .put("length", convertSecondsToTime(Double.parseDouble(properties.get("xmpDM:duration"))));
    }

    public String convertSecondsToTime(double sec) {
        return String.format("%d:%d", (int) sec / 60, Math.round(sec % 60));
    }

    public Response callSaveSongMetadata(Map<String, String> songMetadata) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, mapToSongJson(songMetadata).toString());
        Request request = new Request.Builder()
                .url(API_GATEWAY_URL + "/songs")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }
}
