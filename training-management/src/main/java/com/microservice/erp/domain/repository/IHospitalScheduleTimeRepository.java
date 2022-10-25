package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Dzongkhag;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.List;


@Repository
public interface IHospitalScheduleTimeRepository extends JpaRepository<HospitalScheduleTime,Long> {

    Collection<HospitalScheduleTime> findAllByHospitalId(Long dzoHosId);
    Collection<HospitalScheduleTime> findByHospitalIdOrderByAppointmentDateAsc(Long hospitalId);


}
