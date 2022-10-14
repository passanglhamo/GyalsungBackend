package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Dzongkhag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDzongkhagRepository extends JpaRepository<Dzongkhag, String> {
    Dzongkhag findByDzongkhagId(Integer presentDzongkhagId);
}
