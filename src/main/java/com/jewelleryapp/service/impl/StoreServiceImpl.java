package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.StoreRequestDto;
import com.jewelleryapp.dto.response.StoreResponseDto;
import com.jewelleryapp.entity.Store;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.StoreMapper;
import com.jewelleryapp.repository.StoreRepository;
import com.jewelleryapp.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto requestDto) {
        Store store = storeMapper.toEntity(requestDto);
        Store savedStore = storeRepository.save(store);
        return storeMapper.toDto(savedStore);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreResponseDto> getAllStores(Specification<Store> spec, Pageable pageable) {
        return storeRepository.findAll(spec, pageable)
                .map(storeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreResponseDto getStoreById(UUID id) {
        return storeRepository.findById(id)
                .map(storeMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
    }

    @Override
    @Transactional
    public StoreResponseDto updateStore(UUID id, StoreRequestDto requestDto) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));

        storeMapper.updateEntityFromDto(requestDto, existingStore);

        Store updatedStore = storeRepository.save(existingStore);
        return storeMapper.toDto(updatedStore);
    }

    @Override
    @Transactional
    public void deleteStore(UUID id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
        // This will fail if StockItems are associated.
        storeRepository.delete(store);
    }
}