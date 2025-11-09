package com.jewelleryapp.repository;

import com.jewelleryapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    // JpaSpecificationExecutor adds findOne(Specification), findAll(Specification),
    // findAll(Specification, Pageable), findAll(Specification, Sort), count(Specification)
}