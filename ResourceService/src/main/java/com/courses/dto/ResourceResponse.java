package com.courses.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResourceResponse {
    private Long Id;
    @NotEmpty
    private String sourcePath;
    private String storage;
}
