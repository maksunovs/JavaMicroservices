package com.courses.java.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SongDto {

    @Min(0L)
    @Max(Long.MAX_VALUE)
    private Long id;

    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String artist;

    @Size(max = 100)
    private String album;

    @NotBlank
    @Size(max = 10)
    private String length;

    @NotNull
    @Min(0L)
    @Max(Long.MAX_VALUE)
    private Long resourceId;

    @Min(0)
    @Max(9999)
    private int year;
    
    private String genre;

}
