package com.courses.entity;

import com.courses.client.dto.StorageType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

@Entity
@Data
public class Resource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String sourcePath;
    @Transient
    private byte[] audioBytes;
    @Enumerated(EnumType.STRING)
    private StorageType storage;

}
