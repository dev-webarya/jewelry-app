package com.jewelleryapp.repository;

import com.jewelleryapp.entity.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeTypeRepository extends JpaRepository<AttributeType, Integer>, JpaSpecificationExecutor<AttributeType> {
}