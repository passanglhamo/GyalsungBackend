package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Geog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGeogRepository extends JpaRepository<Geog, String> {
    List<Geog> findByDzongkhagIdOrderByGeogNameAsc(Integer dzongkhagId);

    Geog findByGeogId(Integer presentGeogId);
}
