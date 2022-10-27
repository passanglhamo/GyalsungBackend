package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IMedicalQuestionCategoryRepository extends JpaRepository<MedicalQuestionCategory, Long> {
    List<MedicalQuestionCategory> findAllByStatus(String status);
}
