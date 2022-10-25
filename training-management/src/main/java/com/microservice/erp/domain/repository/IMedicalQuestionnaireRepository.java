package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IMedicalQuestionnaireRepository extends JpaRepository<MedicalQuestionnaire, Long> {
}
