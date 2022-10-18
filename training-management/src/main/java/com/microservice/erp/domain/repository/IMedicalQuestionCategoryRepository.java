package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author Rajib Kumer Ghosh
 *
 */
@CrossOrigin(origins = "*")
@Repository
public interface IMedicalQuestionCategoryRepository extends JpaRepository<MedicalQuestionCategory, Long> {
}
