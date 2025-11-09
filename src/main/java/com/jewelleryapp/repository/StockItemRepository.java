package com.jewelleryapp.repository;

import com.jewelleryapp.entity.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, UUID>, JpaSpecificationExecutor<StockItem> {

    // Find stock for a product at a specific store (null storeId for central)
    Optional<StockItem> findByProductIdAndStoreId(UUID productId, UUID storeId);

    // Find central warehouse stock
    Optional<StockItem> findByProductIdAndStoreIdIsNull(UUID productId);
}