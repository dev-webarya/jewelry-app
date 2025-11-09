package com.jewelleryapp.service;

import com.jewelleryapp.dto.request.CollectionRequestDto;
import com.jewelleryapp.dto.response.CollectionResponseDto;
import com.jewelleryapp.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CollectionService {

    CollectionResponseDto createCollection(CollectionRequestDto requestDto);

    Page<CollectionResponseDto> getAllCollections(Specification<Collection> spec, Pageable pageable);

    CollectionResponseDto getCollectionById(Integer id);

    CollectionResponseDto updateCollection(Integer id, CollectionRequestDto requestDto);

    void deleteCollection(Integer id);
}