package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.OrderEntity;
import com.predix.marketschema.domain.enums.OrderStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("""
            SELECT o FROM OrderEntity o
            WHERE (:marketId IS NULL OR o.marketId = :marketId)
              AND (:userId IS NULL OR o.userId = :userId)
              AND (:status IS NULL OR o.status = :status)
            ORDER BY o.createdAt DESC
            """)
    Page<OrderEntity> findByFilters(
            @Param("marketId") UUID marketId,
            @Param("userId") String userId,
            @Param("status") OrderStatus status,
            Pageable pageable);
}
