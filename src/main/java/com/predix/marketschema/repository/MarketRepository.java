package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.MarketEntity;
import com.predix.marketschema.domain.enums.MarketStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarketRepository extends JpaRepository<MarketEntity, UUID> {

    @Query("""
            SELECT m FROM MarketEntity m
            WHERE (:status IS NULL OR m.status = :status)
              AND (:category IS NULL OR m.category = :category)
            """)
    Page<MarketEntity> findByFilters(
            @Param("status") MarketStatus status,
            @Param("category") String category,
            Pageable pageable);
}
