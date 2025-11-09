package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.AttributeTypeRequestDto;
import com.jewelleryapp.dto.response.AttributeTypeResponseDto;
import com.jewelleryapp.entity.AttributeType;
import com.jewelleryapp.service.AttributeTypeService;
import com.jewelleryapp.specification.AttributeTypeSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/attribute-types")
@RequiredArgsConstructor
public class AttributeTypeController {

    private final AttributeTypeService attributeTypeService;

    @PostMapping
    public ResponseEntity<AttributeTypeResponseDto> createAttributeType(@Valid @RequestBody AttributeTypeRequestDto requestDto) {
        AttributeTypeResponseDto createdDto = attributeTypeService.createAttributeType(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttributeTypeResponseDto> getAttributeTypeById(@PathVariable Integer id) {
        AttributeTypeResponseDto dto = attributeTypeService.getAttributeTypeById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<AttributeTypeResponseDto>> getAllAttributeTypes(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Specification<AttributeType> spec = Specification.where(AttributeTypeSpecification.hasName(name));

        Page<AttributeTypeResponseDto> dtos = attributeTypeService.getAllAttributeTypes(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttributeTypeResponseDto> updateAttributeType(@PathVariable Integer id, @Valid @RequestBody AttributeTypeRequestDto requestDto) {
        AttributeTypeResponseDto updatedDto = attributeTypeService.updateAttributeType(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttributeType(@PathVariable Integer id) {
        attributeTypeService.deleteAttributeType(id);
        return ResponseEntity.noContent().build();
    }
}