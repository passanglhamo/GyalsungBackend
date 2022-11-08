package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalScheduleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface IHospitalScheduleDateRepository extends JpaRepository<HospitalScheduleDate,Long> {

    Collection<HospitalScheduleDate> findAllByHospitalId(Long dzoHosId);
    Collection<HospitalScheduleDate> findByHospitalIdOrderByAppointmentDateAsc(Long hospitalId);

    @Query(value = "select d.* from tms_hospital_schedule_date d inner join tms_hos_schedule_time t\n" +
            "on d.hospital_schedule_date_id = t.hospital_schedule_date_id \n" +
            "where t.booked_by =:userId", nativeQuery = true)
    HospitalScheduleDate getMyBookingDate(Long userId);
}
