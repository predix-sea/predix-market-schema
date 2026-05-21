package com.predix.marketschema.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MarketCodeSequenceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public long nextVal() {
        Number result = (Number) entityManager
                .createNativeQuery("SELECT nextval('market_code_seq')")
                .getSingleResult();
        return result.longValue();
    }
}
