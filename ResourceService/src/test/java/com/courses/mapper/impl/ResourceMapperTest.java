package com.courses.mapper.impl;

import com.courses.client.dto.StorageType;
import com.courses.dto.ResourceDto;
import com.courses.dto.ResourceResponse;
import com.courses.entity.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class ResourceMapperTest {
    private static final String ENCODED_STRING_BASE64 = "VGVzdCBzdHJpbmcgdG8gdGVzdCBiYXNlIDY0IGVuY29kaW5n";
    private static final byte[] audioBytes = Base64.getDecoder().decode(ENCODED_STRING_BASE64);
    private static final ResourceMapper mapper = new ResourceMapper();

    @Test
    void testDtoToEntity() {
        ResourceDto dto = new ResourceDto();
        dto.setId(1L);
        dto.setAudioBytes(ENCODED_STRING_BASE64);
        dto.setStorage(StorageType.PERMANENT.name());
        Resource newResource = mapper.dtoToEntity(dto);
        assertArrayEquals(audioBytes, newResource.getAudioBytes());
        assertEquals(1L, newResource.getId());
        assertEquals(StorageType.PERMANENT, newResource.getStorage());
    }

    @Test
    void testEntityToDto() {
        Resource entity = new Resource();
        entity.setId(1L);
        entity.setAudioBytes(audioBytes);
        entity.setStorage(StorageType.PERMANENT);
        ResourceDto dto = mapper.entityToDto(entity);
        assertEquals(1L, dto.getId());
        assertEquals(ENCODED_STRING_BASE64, dto.getAudioBytes());
        assertEquals(StorageType.PERMANENT.name(), dto.getStorage());
    }

}