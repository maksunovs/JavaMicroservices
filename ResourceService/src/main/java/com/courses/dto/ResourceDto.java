package com.courses.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class ResourceDto {
    private Long Id;
    @NotEmpty
    private String audioBytes;
    private String storage;
}
