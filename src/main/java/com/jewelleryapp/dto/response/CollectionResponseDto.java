package com.jewelleryapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CollectionResponseDto {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}