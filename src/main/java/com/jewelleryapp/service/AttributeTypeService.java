package com.jewelleryapp.service;

import com.jewelleryapp.dto.request.AttributeTypeRequestDto;
import com.jewelleryapp.dto.response.AttributeTypeResponseDto;
import com.jewelleryapp.entity.AttributeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface AttributeTypeService {

    AttributeTypeResponseDto createAttributeType(AttributeTypeRequestDto requestDto);

    Page<AttributeTypeResponseDto> getAllAttributeTypes(Specification<AttributeType> spec, Pageable pageable);

    AttributeTypeResponseDto getAttributeTypeById(Integer id);

    AttributeTypeResponseDto updateAttributeType(Integer id, AttributeTypeRequestDto requestDto);

    void deleteAttributeType(Integer id);
}