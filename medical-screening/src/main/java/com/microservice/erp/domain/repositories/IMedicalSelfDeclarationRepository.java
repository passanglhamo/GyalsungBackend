package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicalSelfDeclarationRepository extends JpaRepository<MedicalSelfDeclaration, Long> {
}
