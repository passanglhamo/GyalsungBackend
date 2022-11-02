package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalScheduleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface IHospitalScheduleDateRepository extends JpaRepository<HospitalScheduleDate,Long> {

    Collection<HospitalScheduleDate> findAllByHospitalId(Long dzoHosId);
    Collection<HospitalScheduleDate> findByHospitalIdOrderByAppointmentDateAsc(Long hospitalId);


}
