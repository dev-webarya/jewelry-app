package com.jewelleryapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StockItemResponseDto {
    private UUID id;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Enriched Data ---
    private UUID productId;
    private String productName;
    private String productSku;

    private UUID storeId; // Null for central warehouse
    private String storeName; // Null for central warehouse
}