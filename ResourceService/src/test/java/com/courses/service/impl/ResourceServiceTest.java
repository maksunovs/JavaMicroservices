package com.courses.service.impl;

import com.courses.entity.Resource;
import com.courses.exception.NotAudioFileException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServiceTest {
    private static final String ENCODED_STRING_BASE64 = "VGVzdCBzdHJpbmcgdG8gdGVzdCBiYXNlIDY0IGVuY29kaW5n";
    private final ResourceService resourceService = new ResourceService();

// Integration or component test
//    @Test
//    void testSaveResource() {
//    }

    // Integration test
//    @Test
//    void testDeleteById() {
//    }

    @Test
    void testStreamToBytes() throws IOException {
        byte[] audioBytes = Base64.getDecoder().decode(ENCODED_STRING_BASE64);
        InputStream is = new ByteArrayInputStream(audioBytes);
        byte[] processedAudioBytes = resourceService.streamToBytes(is);
        assertArrayEquals(audioBytes, processedAudioBytes);
    }

    @Test
    void testValidateEmptyResource() {
        Exception exception = assertThrows(RuntimeException.class, () -> resourceService.validate(new Resource()));

        String expectedMessage = "Resource is null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testValidateInvalidResourceType(){
        byte[] audioBytes = Base64.getDecoder().decode(ENCODED_STRING_BASE64);
        Resource resource = new Resource();
        resource.setAudioBytes(audioBytes);
        Exception exception = assertThrows(NotAudioFileException.class, () -> resourceService.validate(resource));

        String expectedMessage = "Provided data is not audio file:";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}