package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.MedicalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface IHospitalBookingDateRepository extends JpaRepository<MedicalConfiguration, BigInteger> {
    List<MedicalConfiguration> findAllByHospitalId(BigInteger dzoHosId);
    MedicalConfiguration findByHospitalIdAndAppointmentDate(Integer dzoHosId,
                                                            Date appointmentDate);

}
