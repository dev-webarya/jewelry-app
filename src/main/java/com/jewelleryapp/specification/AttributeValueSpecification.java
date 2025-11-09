package com.jewelleryapp.specification;

import com.jewelleryapp.entity.AttributeType;
import com.jewelleryapp.entity.AttributeValue;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AttributeValueSpecification {

    public static Specification<AttributeValue> hasValue(String value) {
        return (root, query, cb) -> {
            if (value == null || value.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("value")), "%" + value.toLowerCase() + "%");
        };
    }

    public static Specification<AttributeValue> hasAttributeTypeId(Integer attributeTypeId) {
        return (root, query, cb) -> {
            if (attributeTypeId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("attributeType").get("id"), attributeTypeId);
        };
    }

    public static Specification<AttributeValue> hasAttributeTypeName(String attributeTypeName) {
        return (root, query, cb) -> {
            if (attributeTypeName == null || attributeTypeName.isEmpty()) {
                return cb.conjunction();
            }
            Join<AttributeValue, AttributeType> typeJoin = root.join("attributeType");
            return cb.like(cb.lower(typeJoin.get("name")), "%" + attributeTypeName.toLowerCase() + "%");
        };
    }
}