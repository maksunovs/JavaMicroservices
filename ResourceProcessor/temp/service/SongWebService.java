package com.java.cources.service;

import com.java.cources.parser.AudioFileParser;
import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
public class SongWebService {
    @Autowired
    private AudioFileParser parser;

    @Value("${api.gateway.url}")
    private String API_GATEWAY_URL;

//    public String mapToSongJson(Resource resource) {
//        try (InputStream is = new ByteArrayInputStream(resource.getAudioBytes())) {
//            Metadata metadata = parser.parseMetadata(is);
//            String jsonString = new JSONObject()
//                    .put("name", metadata.get("dc:title"))
//                    .put("album", metadata.get("xmpDM:album"))
//                    .put("artist", metadata.get("xmpDM:artist"))
//                    .put("resourceId", resource.getId())
//                    .put("year", metadata.get("xmpDM:releaseDate"))
//                    .put("length", convertSecondsToTime(Double.parseDouble(metadata.get("xmpDM:duration"))))
//                    .toString();
//            return jsonString;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    private String convertSecondsToTime(double sec) {
        return String.format("%d:%d", Math.round(sec / 60), Math.round(sec % 60));
    }

    public Response callSaveSongMetadata(String jsonBody) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody);
        Request request = new Request.Builder()
                .url(API_GATEWAY_URL + "/songs")
                .post(body)
                .build();

        return client.newCall(request).execute();
    }
}
