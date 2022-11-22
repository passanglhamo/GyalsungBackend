package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IMedicalQuestionnaireRepository extends JpaRepository<MedicalQuestionnaire, BigInteger> {
    List<MedicalQuestionnaire> findAllByOrderByMedicalQuestionNameAsc();

}
