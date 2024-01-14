package com.courses.entity;

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
    private byte[] audioBytes;
    private String sourcePath;
    @Transient
    private InputStream inputStream;

}
