package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.StockItemRequestDto;
import com.jewelleryapp.dto.response.StockItemResponseDto;
import com.jewelleryapp.entity.StockItem;
import com.jewelleryapp.service.StockItemService;
import com.jewelleryapp.specification.StockItemSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stock-items")
@RequiredArgsConstructor
public class StockItemController {

    private final StockItemService stockItemService;

    @PostMapping
    public ResponseEntity<StockItemResponseDto> createStockItem(@Valid @RequestBody StockItemRequestDto requestDto) {
        StockItemResponseDto createdDto = stockItemService.createStockItem(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItemResponseDto> getStockItemById(@PathVariable UUID id) {
        StockItemResponseDto dto = stockItemService.getStockItemById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<StockItemResponseDto>> getAllStockItems(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID storeId,
            @RequestParam(required = false) String productSku,
            @RequestParam(required = false) Boolean isCentral,
            @RequestParam(required = false) Integer maxQuantity,
            Pageable pageable) {

        Specification<StockItem> spec = Specification.where(StockItemSpecification.hasProductId(productId))
                .and(StockItemSpecification.hasStoreId(storeId))
                .and(StockItemSpecification.hasProductSku(productSku))
                .and(StockItemSpecification.isCentralWarehouse(isCentral))
                .and(StockItemSpecification.hasQuantityLessThan(maxQuantity));

        Page<StockItemResponseDto> dtos = stockItemService.getAllStockItems(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItemResponseDto> updateStockItem(@PathVariable UUID id, @Valid @RequestBody StockItemRequestDto requestDto) {
        StockItemResponseDto updatedDto = stockItemService.updateStockItem(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockItem(@PathVariable UUID id) {
        stockItemService.deleteStockItem(id);
        return ResponseEntity.noContent().build();
    }
}