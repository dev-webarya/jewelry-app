package com.jewelleryapp.dto.response;

import lombok.Data;
import java.util.Set;

@Data
public class CategoryResponseDto {

    private Integer id;
    private String name;
    private Integer parentId;

    // Include subcategories in the response to show the hierarchy
    private Set<CategoryResponseDto> subcategories;
}