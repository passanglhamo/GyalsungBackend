package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IMedicalQuestionCategoryRepository extends JpaRepository<MedicalQuestionCategory, BigInteger> {
    List<MedicalQuestionCategory> findAllByStatus(String status);
}
