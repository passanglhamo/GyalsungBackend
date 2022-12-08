package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.HospitalScheduleDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Repository
public interface IHospitalScheduleDateRepository extends JpaRepository<HospitalScheduleDate, BigInteger> {

    Collection<HospitalScheduleDate> findAllByHospitalId(BigInteger dzoHosId);

    boolean existsByAppointmentDateAndHospitalId(Date appointmentDate,BigInteger hospitalId);

    List<HospitalScheduleDate> findAllByAppointmentDateAndHospitalId(Date appointmentDate,BigInteger hospitalId);

    Collection<HospitalScheduleDate> findByHospitalIdOrderByAppointmentDateAsc(BigInteger hospitalId);

    @Query(value = "select d.* from tms_hospital_schedule_date d inner join tms_hos_schedule_time t\n" +
            "on d.hospital_schedule_date_id = t.hospital_schedule_date_id \n" +
            "where t.booked_by =:userId", nativeQuery = true)
    HospitalScheduleDate getMyBookingDate(BigInteger userId);

    @Query(value = "select t.* \n" +
            "from tms_hospital_schedule_date d\n" +
            "inner join tms_hos_schedule_time t\n" +
            "on d.hospital_schedule_date_id = t.hospital_schedule_date_id \n" +
            "WHERE d.appointment_date=:appointmentDate\n" +
            "AND :startTime BETWEEN t.start_time AND t.end_time", nativeQuery = true)
    HospitalScheduleDate getHospitalScheduleByAppDateAndTime(Date appointmentDate,Date startTime);
}
