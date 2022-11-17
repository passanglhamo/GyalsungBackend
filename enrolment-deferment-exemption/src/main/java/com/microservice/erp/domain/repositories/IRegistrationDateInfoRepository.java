package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRegistrationDateInfoRepository extends JpaRepository<RegistrationDateInfo, Long> {
}
