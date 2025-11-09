package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.AttributeValueRequestDto;
import com.jewelleryapp.dto.response.AttributeValueResponseDto;
import com.jewelleryapp.entity.AttributeValue;
import com.jewelleryapp.service.AttributeValueService;
import com.jewelleryapp.specification.AttributeValueSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attribute-values")
@RequiredArgsConstructor
public class AttributeValueController {

    private final AttributeValueService attributeValueService;

    @PostMapping
    public ResponseEntity<AttributeValueResponseDto> createAttributeValue(@Valid @RequestBody AttributeValueRequestDto requestDto) {
        AttributeValueResponseDto createdDto = attributeValueService.createAttributeValue(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeValueResponseDto> getAttributeValueById(@PathVariable Integer id) {
        AttributeValueResponseDto dto = attributeValueService.getAttributeValueById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AttributeValueResponseDto>> getAllAttributeValues(
            @RequestParam(required = false) String value,
            @RequestParam(required = false) Integer attributeTypeId,
            @RequestParam(required = false) String attributeTypeName,
            Pageable pageable) {

        Specification<AttributeValue> spec = Specification.where(AttributeValueSpecification.hasValue(value))
                .and(AttributeValueSpecification.hasAttributeTypeId(attributeTypeId))
                .and(AttributeValueSpecification.hasAttributeTypeName(attributeTypeName));

        Page<AttributeValueResponseDto> dtos = attributeValueService.getAllAttributeValues(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttributeValueResponseDto> updateAttributeValue(@PathVariable Integer id, @Valid @RequestBody AttributeValueRequestDto requestDto) {
        AttributeValueResponseDto updatedDto = attributeValueService.updateAttributeValue(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeValue(@PathVariable Integer id) {
        attributeValueService.deleteAttributeValue(id);
        return ResponseEntity.noContent().build();
    }
}