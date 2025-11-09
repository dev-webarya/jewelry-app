package com.jewelleryapp.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreResponseDto {
    private UUID id;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String operatingHours;
    private String contactPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}