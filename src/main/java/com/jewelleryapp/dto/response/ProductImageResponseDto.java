package com.jewelleryapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductImageResponseDto {
    private UUID id;
    private UUID productId;
    private String imageUrl;
    private boolean isPrimary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
