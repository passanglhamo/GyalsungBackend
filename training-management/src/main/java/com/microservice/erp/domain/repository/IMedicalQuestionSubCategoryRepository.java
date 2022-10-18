package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IMedicalQuestionSubCategoryRepository extends JpaRepository<MedicalQuestionSubCategory, Integer> {
}
