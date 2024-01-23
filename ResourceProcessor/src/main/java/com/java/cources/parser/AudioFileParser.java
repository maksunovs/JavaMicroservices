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

@Component
public class AudioFileParser {

    private static final String AUDIO_CONTENT_TYPE = "audio/mpeg";

    public Metadata parseMetadata(InputStream is) {
        try {
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(is, handler, metadata, parseCtx);
            is.close();
            return metadata;
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject mapToSongJson(Metadata metadata) {
        return new JSONObject()
                .put("name", metadata.get("dc:title"))
                .put("album", metadata.get("xmpDM:album"))
                .put("artist", metadata.get("xmpDM:artist"))
                .put("year", metadata.get("xmpDM:releaseDate"))
                .put("length", convertSecondsToTime(Double.parseDouble(metadata.get("xmpDM:duration"))));
    }

    private String convertSecondsToTime(double sec) {
        return String.format("%d:%d",Math.round(sec / 60), Math.round(sec % 60));
    }
}
