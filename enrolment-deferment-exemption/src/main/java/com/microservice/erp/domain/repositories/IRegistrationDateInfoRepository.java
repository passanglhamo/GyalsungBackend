package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface IRegistrationDateInfoRepository extends JpaRepository<RegistrationDateInfo, Long> {
    RegistrationDateInfo findByStatus(char status);
}
