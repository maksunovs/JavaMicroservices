package com.java.cources.parser;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AudioFileParserTest {

    private static final AudioFileParser parser = new AudioFileParser();
    private static final String AUDIO_FILE_PATH = "src/test/resources/audio/Metallica - Nothing Else Matters.mp3";

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


    @Test
    void testParseMetadata() throws IOException {
        InputStream is = new FileInputStream(Paths.get(AUDIO_FILE_PATH).toFile());
        Map<String, String> propertiesMap = parser.parseMetadata(is);
        assertEquals(NAME_VALUE, propertiesMap.get(NAME_KEY));
        assertEquals(ALBUM_VALUE, propertiesMap.get(ALBUM_KEY));
        assertEquals(ARTIST_VALUE, propertiesMap.get(ARTIST_KEY));
        assertEquals(YEAR_VALUE, propertiesMap.get(YEAR_KEY));
        assertEquals(LENGTH_VALUE, propertiesMap.get(LENGTH_KEY));
        is.close();
    }
}