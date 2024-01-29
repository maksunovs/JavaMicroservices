package com.java.cources.service;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SongWebServiceTest {

    private static final Map<String, String> propertiesMap = new HashMap<>();
    private static final SongWebService songWebService = new SongWebService();
    private static final String ALBUM_KEY = "xmpDM:album";
    private static final String ARTIST_KEY = "xmpDM:artist";
    private static final String YEAR_KEY = "xmpDM:releaseDate";
    private static final String NAME_KEY = "dc:title";
    private static final String LENGTH_KEY = "xmpDM:duration";

    private static final String ALBUM_VALUE = "";
    private static final String ARTIST_VALUE = "Metallica";
    private static final String YEAR_VALUE = "1997";
    private static final String NAME_VALUE = "Nothing Else Matters";
    private static final String LENGTH_VALUE = "388.36639404296875";

    private static final String ALBUM_JSON = "album";
    private static final String ARTIST_JSON = "artist";
    private static final String YEAR_JSON = "year";
    private static final String NAME_JSON = "name";
    private static final String LENGTH_JSON = "length";

   static {
        propertiesMap.put(ALBUM_KEY, ALBUM_VALUE);
        propertiesMap.put(ARTIST_KEY, ARTIST_VALUE);
        propertiesMap.put(YEAR_KEY, YEAR_VALUE);
        propertiesMap.put(NAME_KEY, NAME_VALUE);
        propertiesMap.put(LENGTH_KEY, LENGTH_VALUE);
        propertiesMap.put("resourceId", "1");
    }

    @Test
    void testMapToSongJson() {
        JSONObject jsonObject = songWebService.mapToSongJson(propertiesMap);
        assertEquals(ALBUM_VALUE, jsonObject.get(ALBUM_JSON).toString());
        assertEquals(ARTIST_VALUE, jsonObject.get(ARTIST_JSON).toString());
        assertEquals(YEAR_VALUE, jsonObject.get(YEAR_JSON).toString());
        assertEquals(NAME_VALUE, jsonObject.get(NAME_JSON).toString());
        assertEquals("6:28", jsonObject.get(LENGTH_JSON).toString());
    }

    @Test
    void testConvertSecondsToTime() {
       assertEquals("3:30", songWebService.convertSecondsToTime(210));
    }
}