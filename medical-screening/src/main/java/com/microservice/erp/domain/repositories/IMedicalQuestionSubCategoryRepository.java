package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalQuestionSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Repository
public interface IMedicalQuestionSubCategoryRepository extends JpaRepository<MedicalQuestionSubCategory, BigInteger> {
}
