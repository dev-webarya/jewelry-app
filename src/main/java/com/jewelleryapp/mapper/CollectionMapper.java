package com.jewelleryapp.mapper;

import com.jewelleryapp.dto.request.CollectionRequestDto;
import com.jewelleryapp.dto.response.CollectionResponseDto;
import com.jewelleryapp.entity.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CollectionMapper {

    CollectionResponseDto toDto(Collection collection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Collection toEntity(CollectionRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CollectionRequestDto requestDto, @MappingTarget Collection collection);
}