package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.StockItemRequestDto;
import com.jewelleryapp.dto.response.StockItemResponseDto;
import com.jewelleryapp.entity.Product;
import com.jewelleryapp.entity.StockItem;
import com.jewelleryapp.entity.Store;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.StockItemMapper;
import com.jewelleryapp.repository.ProductRepository;
import com.jewelleryapp.repository.StockItemRepository;
import com.jewelleryapp.repository.StoreRepository;
import com.jewelleryapp.service.StockItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockItemServiceImpl implements StockItemService {

    private final StockItemRepository stockItemRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StockItemMapper stockItemMapper;

    @Override
    @Transactional
    public StockItemResponseDto createStockItem(StockItemRequestDto requestDto) {
        // Check for existing item with the same (product, store) composite key
        Optional<StockItem> existing;
        if (requestDto.getStoreId() == null) {
            existing = stockItemRepository.findByProductIdAndStoreIdIsNull(requestDto.getProductId());
        } else {
            existing = stockItemRepository.findByProductIdAndStoreId(requestDto.getProductId(), requestDto.getStoreId());
        }

        if (existing.isPresent()) {
            throw new DataIntegrityViolationException("StockItem for this product and store already exists. Use update (PUT) instead.");
        }

        StockItem stockItem = stockItemMapper.toEntity(requestDto);

        // Link Product
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));
        stockItem.setProduct(product);

        // Link Store (if provided)
        if (requestDto.getStoreId() != null) {
            Store store = storeRepository.findById(requestDto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", "id", requestDto.getStoreId()));
            stockItem.setStore(store);
        }

        StockItem savedItem = stockItemRepository.save(stockItem);
        return stockItemMapper.toDto(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockItemResponseDto> getAllStockItems(Specification<StockItem> spec, Pageable pageable) {
        return stockItemRepository.findAll(spec, pageable)
                .map(stockItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StockItemResponseDto getStockItemById(UUID id) {
        return stockItemRepository.findById(id)
                .map(stockItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));
    }

    @Override
    @Transactional
    public StockItemResponseDto updateStockItem(UUID id, StockItemRequestDto requestDto) {
        StockItem existingItem = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));

        // You generally shouldn't allow changing the product/store of a stock item.
        // If you do, you must re-check the unique constraint.
        if (!existingItem.getProduct().getId().equals(requestDto.getProductId()) ||
                (existingItem.getStore() != null && !existingItem.getStore().getId().equals(requestDto.getStoreId())) ||
                (existingItem.getStore() == null && requestDto.getStoreId() != null)) {
            throw new IllegalArgumentException("Cannot change the product or store of an existing StockItem. Delete and create a new one.");
        }

        stockItemMapper.updateEntityFromDto(requestDto, existingItem);
        StockItem updatedItem = stockItemRepository.save(existingItem);
        return stockItemMapper.toDto(updatedItem);
    }

    @Override
    @Transactional
    public void deleteStockItem(UUID id) {
        StockItem stockItem = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));
        stockItemRepository.delete(stockItem);
    }
}