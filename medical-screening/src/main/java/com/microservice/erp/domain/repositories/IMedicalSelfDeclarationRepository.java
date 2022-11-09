package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface IMedicalSelfDeclarationRepository extends JpaRepository<MedicalSelfDeclaration, Long> {
    List<MedicalSelfDeclaration> findByUserIdOrderByMedicalQuestionNameAsc(Long userId);

    @Transactional
    void deleteAllByUserId(Long userId);
}
