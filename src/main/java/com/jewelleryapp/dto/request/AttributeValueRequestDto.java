package com.jewelleryapp.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeValueRequestDto {

    @NotNull(message = "AttributeType ID cannot be null")
    private Integer attributeTypeId;

    @NotBlank(message = "Attribute value cannot be blank")
    private String value;
}