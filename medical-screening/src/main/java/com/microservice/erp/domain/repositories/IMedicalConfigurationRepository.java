package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMedicalConfigurationRepository extends JpaRepository<MedicalConfiguration, BigInteger> {
    @Query(value = "SELECT * FROM med_medical_configuration m " +
            "WHERE m.hospital_id = :hospitalId " +
            "AND DATE(m.appointment_date) = DATE(:appointmentDate)", nativeQuery = true)
    Optional<MedicalConfiguration> findByHospitalIdAndAppointmentDate(Integer hospitalId, Date appointmentDate);

    List<MedicalConfiguration> findAllByHospitalId(Integer hospitalId);


    boolean existsByHospitalIdAndAppointmentDateAndIdNot(Integer hospitalId, Date appointmentDate, BigInteger id);
}
