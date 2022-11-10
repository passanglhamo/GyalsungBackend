package com.microservice.erp.domain.helper;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional()
public abstract class BaseDao {
    @PersistenceContext
    protected EntityManager entityManager;
}




























