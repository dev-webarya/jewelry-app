package com.jewelleryapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttributeValueResponseDto {
    private Integer id;
    private Integer attributeTypeId;
    private String attributeTypeName; // Include name for context
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}