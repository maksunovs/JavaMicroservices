package com.java.cources.parser;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class AudioFileParser {

    private static final String AUDIO_CONTENT_TYPE = "audio/mpeg";

    public Map<String, String> parseMetadata(InputStream is) {
        try {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(is, handler, metadata, parseCtx);
            Map<String, String> metadataMap = new HashMap<>();
            Arrays.stream(metadata.names()).forEach(key -> metadataMap.put(key, metadata.get(key)));
            return metadataMap;
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException(e);
        }
    }
}
