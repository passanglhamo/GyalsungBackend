package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface IRegistrationDateInfoRepository extends JpaRepository<RegistrationDateInfo, BigInteger> {
    RegistrationDateInfo findByStatus(char status);

    boolean existsByRegistrationYear(String registrationYear);

    boolean existsByRegistrationYearAndRegistrationDateIdNot(String registrationYear, BigInteger id);

    List<RegistrationDateInfo> findAllByOrderByRegistrationYearDesc();

    boolean existsByStatus(char status);

    boolean existsByStatusAndRegistrationDateIdNot(char status, BigInteger id);


    Optional<RegistrationDateInfo> findByRegistrationDateId(BigInteger registrationDateId);

    RegistrationDateInfo findFirstByOrderByRegistrationDateIdDesc();
}
