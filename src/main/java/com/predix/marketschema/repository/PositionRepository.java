package com.predix.marketschema.repository;

import com.predix.marketschema.domain.entity.PositionEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PositionRepository extends JpaRepository<PositionEntity, UUID> {

    @Query("""
            SELECT p FROM PositionEntity p
            WHERE (:marketId IS NULL OR p.marketId = :marketId)
              AND (:userId IS NULL OR p.userId = :userId)
            ORDER BY p.updatedAt DESC
            """)
    List<PositionEntity> findByFilters(
            @Param("marketId") UUID marketId,
            @Param("userId") String userId);
}
