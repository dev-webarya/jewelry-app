package com.jewelleryapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttributeTypeResponseDto {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // We don't include the values here to avoid circular dependencies in DTOs.
    // A separate endpoint to get values for an attribute type is better.
}