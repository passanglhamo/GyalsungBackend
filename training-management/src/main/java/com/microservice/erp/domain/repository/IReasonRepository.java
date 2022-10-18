package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "*")
@Repository
public interface IReasonRepository extends JpaRepository<Reason, Long> {

    List<Reason> findAllByStatus(Character status);
}
