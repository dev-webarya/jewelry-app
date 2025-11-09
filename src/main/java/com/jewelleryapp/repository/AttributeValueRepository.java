package com.jewelleryapp.repository;

import com.jewelleryapp.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer>, JpaSpecificationExecutor<AttributeValue> {
}