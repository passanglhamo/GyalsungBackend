package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.dto.HospitalBookingDateDto;
import com.microservice.erp.domain.entities.HospitalBookingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface IHospitalBookingDateRepository extends JpaRepository<HospitalBookingDate, BigInteger> {
    List<HospitalBookingDate> findAllByHospitalId(BigInteger dzoHosId);
    HospitalBookingDate findByHospitalIdAndAppointmentDate(BigInteger dzoHosId,
                                                              Date appointmentDate);

}
