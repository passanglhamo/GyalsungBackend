package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IRegistrationDateInfoRepository extends JpaRepository<RegistrationDateInfo, BigInteger> {
    RegistrationDateInfo findByStatus(char status);

    boolean existsByRegistrationYear(String registrationYear);

    boolean existsByRegistrationYearAndIdNot(String registrationYear, BigInteger id);

    List<RegistrationDateInfo> findAllByOrderByRegistrationYearDesc();

    boolean existsByStatus(char status);

    boolean existsByStatusAndIdNot(char status, BigInteger id);


}
