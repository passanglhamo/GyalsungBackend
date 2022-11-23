package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

public interface IMedicalSelfDeclarationRepository extends JpaRepository<MedicalSelfDeclaration, BigInteger> {
    List<MedicalSelfDeclaration> findByUserIdOrderByMedicalQuestionNameAsc(BigInteger userId);

    @Transactional
    void deleteAllByUserId(BigInteger userId);
}
