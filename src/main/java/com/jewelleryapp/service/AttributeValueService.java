package com.jewelleryapp.service;

import com.jewelleryapp.dto.request.AttributeValueRequestDto;
import com.jewelleryapp.dto.response.AttributeValueResponseDto;
import com.jewelleryapp.entity.AttributeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface AttributeValueService {

    AttributeValueResponseDto createAttributeValue(AttributeValueRequestDto requestDto);

    Page<AttributeValueResponseDto> getAllAttributeValues(Specification<AttributeValue> spec, Pageable pageable);

    AttributeValueResponseDto getAttributeValueById(Integer id);

    AttributeValueResponseDto updateAttributeValue(Integer id, AttributeValueRequestDto requestDto);

    void deleteAttributeValue(Integer id);
}