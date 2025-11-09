package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.CollectionRequestDto;
import com.jewelleryapp.dto.response.CollectionResponseDto;
import com.jewelleryapp.entity.Collection;
import com.jewelleryapp.service.CollectionService;
import com.jewelleryapp.specification.CollectionSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<CollectionResponseDto> createCollection(@Valid @RequestBody CollectionRequestDto requestDto) {
        CollectionResponseDto createdDto = collectionService.createCollection(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponseDto> getCollectionById(@PathVariable Integer id) {
        CollectionResponseDto dto = collectionService.getCollectionById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<CollectionResponseDto>> getAllCollections(
            @RequestParam(required = false) String name,
            Pageable pageable) {

        Specification<Collection> spec = Specification.where(CollectionSpecification.hasName(name));

        Page<CollectionResponseDto> dtos = collectionService.getAllCollections(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollectionResponseDto> updateCollection(@PathVariable Integer id, @Valid @RequestBody CollectionRequestDto requestDto) {
        CollectionResponseDto updatedDto = collectionService.updateCollection(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(@PathVariable Integer id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
}