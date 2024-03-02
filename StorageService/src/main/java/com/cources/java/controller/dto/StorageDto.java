package com.cources.java.controller.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageDto {
    private Long id;
    @NotEmpty
    private String storageType;
    @NotEmpty
    private String bucket;
    @NotEmpty
    private String path;
}
