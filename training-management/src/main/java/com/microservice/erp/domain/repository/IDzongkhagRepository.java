package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Dzongkhag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@Repository
public interface IDzongkhagRepository extends JpaRepository<Dzongkhag,Integer> {
    List<Dzongkhag> findAllByOrderByDzongkhagNameAsc();
}
