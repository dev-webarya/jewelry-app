package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.AttributeValueRequestDto;
import com.jewelleryapp.dto.response.AttributeValueResponseDto;
import com.jewelleryapp.entity.AttributeType;
import com.jewelleryapp.entity.AttributeValue;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.AttributeValueMapper;
import com.jewelleryapp.repository.AttributeTypeRepository;
import com.jewelleryapp.repository.AttributeValueRepository;
import com.jewelleryapp.service.AttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeTypeRepository attributeTypeRepository; // To link the type
    private final AttributeValueMapper attributeValueMapper;

    @Override
    @Transactional
    public AttributeValueResponseDto createAttributeValue(AttributeValueRequestDto requestDto) {
        // Find the parent AttributeType
        AttributeType attributeType = attributeTypeRepository.findById(requestDto.getAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("AttributeType", "id", requestDto.getAttributeTypeId()));

        AttributeValue attributeValue = attributeValueMapper.toEntity(requestDto);
        attributeValue.setAttributeType(attributeType); // Set the relationship

        AttributeValue savedAttributeValue = attributeValueRepository.save(attributeValue);
        return attributeValueMapper.toDto(savedAttributeValue);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttributeValueResponseDto> getAllAttributeValues(Specification<AttributeValue> spec, Pageable pageable) {
        return attributeValueRepository.findAll(spec, pageable)
                .map(attributeValueMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AttributeValueResponseDto getAttributeValueById(Integer id) {
        return attributeValueRepository.findById(id)
                .map(attributeValueMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));
    }

    @Override
    @Transactional
    public AttributeValueResponseDto updateAttributeValue(Integer id, AttributeValueRequestDto requestDto) {
        AttributeValue existingValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));

        // Find the parent AttributeType
        AttributeType attributeType = attributeTypeRepository.findById(requestDto.getAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("AttributeType", "id", requestDto.getAttributeTypeId()));

        // Update basic fields
        attributeValueMapper.updateEntityFromDto(requestDto, existingValue);
        existingValue.setAttributeType(attributeType); // Update the relationship

        AttributeValue updatedValue = attributeValueRepository.save(existingValue);
        return attributeValueMapper.toDto(updatedValue);
    }

    @Override
    @Transactional
    public void deleteAttributeValue(Integer id) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AttributeValue", "id", id));
        // This will fail if products are associated.
        attributeValueRepository.delete(attributeValue);
    }
}