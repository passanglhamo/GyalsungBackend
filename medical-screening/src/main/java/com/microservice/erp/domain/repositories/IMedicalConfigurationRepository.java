package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

public interface IMedicalConfigurationRepository extends JpaRepository<MedicalConfiguration, BigInteger> {
    Optional<MedicalConfiguration> findByHospitalIdAndAppointmentDate(Integer hospitalId, Date appointmentDate);
}
